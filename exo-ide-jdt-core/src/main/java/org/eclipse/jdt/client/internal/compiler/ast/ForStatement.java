/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contribution for bug 319201 - [null] no warning when unboxing SingleNameReference causes NPE
 *******************************************************************************/
package org.eclipse.jdt.client.internal.compiler.ast;

import org.eclipse.jdt.client.internal.compiler.ASTVisitor;
import org.eclipse.jdt.client.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.client.internal.compiler.codegen.BranchLabel;
import org.eclipse.jdt.client.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.client.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.client.internal.compiler.flow.LoopingFlowContext;
import org.eclipse.jdt.client.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.client.internal.compiler.impl.Constant;
import org.eclipse.jdt.client.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.client.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.client.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.client.internal.compiler.lookup.TypeIds;

public class ForStatement extends Statement
{

   public Statement[] initializations;

   public Expression condition;

   public Statement[] increments;

   public Statement action;

   // when there is no local declaration, there is no need of a new scope
   // scope is positioned either to a new scope, or to the "upper"scope (see resolveType)
   public BlockScope scope;

   private BranchLabel breakLabel, continueLabel;

   public ForStatement(Statement[] initializations, Expression condition, Statement[] increments, Statement action,
      boolean neededScope, int s, int e)
   {

      this.sourceStart = s;
      this.sourceEnd = e;
      this.initializations = initializations;
      this.condition = condition;
      this.increments = increments;
      this.action = action;
      // remember useful empty statement
      if (action instanceof EmptyStatement)
         action.bits |= ASTNode.IsUsefulEmptyStatement;
      if (neededScope)
      {
         this.bits |= ASTNode.NeededScope;
      }
   }

   public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo)
   {
      this.breakLabel = new BranchLabel();
      this.continueLabel = new BranchLabel();
      int initialComplaintLevel =
         (flowInfo.reachMode() & FlowInfo.UNREACHABLE) != 0 ? Statement.COMPLAINED_FAKE_REACHABLE
            : Statement.NOT_COMPLAINED;

      // process the initializations
      if (this.initializations != null)
      {
         for (int i = 0, count = this.initializations.length; i < count; i++)
         {
            flowInfo = this.initializations[i].analyseCode(this.scope, flowContext, flowInfo);
         }
      }
      Constant cst = this.condition == null ? null : this.condition.constant;
      boolean isConditionTrue = cst == null || (cst != Constant.NotAConstant && cst.booleanValue() == true);
      boolean isConditionFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);

      cst = this.condition == null ? null : this.condition.optimizedBooleanConstant();
      boolean isConditionOptimizedTrue = cst == null || (cst != Constant.NotAConstant && cst.booleanValue() == true);
      boolean isConditionOptimizedFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);

      // process the condition
      LoopingFlowContext condLoopContext = null;
      FlowInfo condInfo = flowInfo.nullInfoLessUnconditionalCopy();
      if (this.condition != null)
      {
         if (!isConditionTrue)
         {
            condInfo =
               this.condition.analyseCode(this.scope, (condLoopContext =
                  new LoopingFlowContext(flowContext, flowInfo, this, null, null, this.scope)), condInfo);
            if ((this.condition.implicitConversion & TypeIds.UNBOXING) != 0)
            {
               this.condition.checkNPE(currentScope, flowContext, flowInfo);
            }
         }
      }

      // process the action
      LoopingFlowContext loopingContext;
      UnconditionalFlowInfo actionInfo;
      if (this.action == null
         || (this.action.isEmptyBlock() && currentScope.compilerOptions().complianceLevel <= ClassFileConstants.JDK1_3))
      {
         if (condLoopContext != null)
            condLoopContext.complainOnDeferredFinalChecks(this.scope, condInfo);
         if (isConditionTrue)
         {
            if (condLoopContext != null)
            {
               condLoopContext.complainOnDeferredNullChecks(currentScope, condInfo);
            }
            return FlowInfo.DEAD_END;
         }
         else
         {
            if (isConditionFalse)
            {
               this.continueLabel = null; // for(;false;p());
            }
            actionInfo = condInfo.initsWhenTrue().unconditionalCopy();
            loopingContext =
               new LoopingFlowContext(flowContext, flowInfo, this, this.breakLabel, this.continueLabel, this.scope);
         }
      }
      else
      {
         loopingContext =
            new LoopingFlowContext(flowContext, flowInfo, this, this.breakLabel, this.continueLabel, this.scope);
         FlowInfo initsWhenTrue = condInfo.initsWhenTrue();

         if (isConditionFalse)
         {
            actionInfo = FlowInfo.DEAD_END;
         }
         else
         {
            actionInfo = initsWhenTrue.unconditionalCopy();
            if (isConditionOptimizedFalse)
            {
               actionInfo.setReachMode(FlowInfo.UNREACHABLE_OR_DEAD);
            }
         }
         if (this.action.complainIfUnreachable(actionInfo, this.scope, initialComplaintLevel) < Statement.COMPLAINED_UNREACHABLE)
         {
            actionInfo = this.action.analyseCode(this.scope, loopingContext, actionInfo).unconditionalInits();
         }

         // code generation can be optimized when no need to continue in the loop
         if ((actionInfo.tagBits & loopingContext.initsOnContinue.tagBits & FlowInfo.UNREACHABLE_OR_DEAD) != 0)
         {
            this.continueLabel = null;
         }
         else
         {
            if (condLoopContext != null)
            {
               condLoopContext.complainOnDeferredFinalChecks(this.scope, condInfo);
            }
            actionInfo = actionInfo.mergedWith(loopingContext.initsOnContinue);
            loopingContext.complainOnDeferredFinalChecks(this.scope, actionInfo);
         }
      }
      // for increments
      FlowInfo exitBranch = flowInfo.copy();
      // recover null inits from before condition analysis
      LoopingFlowContext incrementContext = null;
      if (this.continueLabel != null)
      {
         if (this.increments != null)
         {
            incrementContext = new LoopingFlowContext(flowContext, flowInfo, this, null, null, this.scope);
            FlowInfo incrementInfo = actionInfo;
            for (int i = 0, count = this.increments.length; i < count; i++)
            {
               incrementInfo = this.increments[i].analyseCode(this.scope, incrementContext, incrementInfo);
            }
            incrementContext.complainOnDeferredFinalChecks(this.scope, actionInfo = incrementInfo.unconditionalInits());
         }
         exitBranch.addPotentialInitializationsFrom(actionInfo).addInitializationsFrom(condInfo.initsWhenFalse());
      }
      else
      {
         exitBranch.addInitializationsFrom(condInfo.initsWhenFalse());
         if (this.increments != null)
         {
            if (initialComplaintLevel == Statement.NOT_COMPLAINED)
            {
               currentScope.problemReporter().fakeReachable(this.increments[0]);
            }
         }
      }
      // nulls checks
      if (condLoopContext != null)
      {
         condLoopContext.complainOnDeferredNullChecks(currentScope, actionInfo);
      }
      loopingContext.complainOnDeferredNullChecks(currentScope, actionInfo);
      if (incrementContext != null)
      {
         incrementContext.complainOnDeferredNullChecks(currentScope, actionInfo);
      }
      if (loopingContext.hasEscapingExceptions())
      { // https://bugs.eclipse.org/bugs/show_bug.cgi?id=321926
         FlowInfo loopbackFlowInfo = flowInfo.copy();
         if (this.continueLabel != null)
         { // we do get to the bottom
            loopbackFlowInfo.mergedWith(actionInfo.unconditionalCopy());
         }
         loopingContext.simulateThrowAfterLoopBack(loopbackFlowInfo);
      }
      // end of loop
      FlowInfo mergedInfo =
         FlowInfo.mergedOptimizedBranches((loopingContext.initsOnBreak.tagBits & FlowInfo.UNREACHABLE) != 0
            ? loopingContext.initsOnBreak : flowInfo.addInitializationsFrom(loopingContext.initsOnBreak), // recover upstream null
                                                                                                          // info
            isConditionOptimizedTrue, exitBranch, isConditionOptimizedFalse, !isConditionTrue /*
                                                                                               * for ( ; ; ) { } while ( true ) ;
                                                                                               * unreachable ( ) ;
                                                                                               */);
      // Variables initialized only for the purpose of the for loop can be removed for further flow info
      // https://bugs.eclipse.org/bugs/show_bug.cgi?id=359495
      if (this.initializations != null)
      {
         for (int i = 0; i < this.initializations.length; i++)
         {
            Statement init = this.initializations[i];
            if (init instanceof LocalDeclaration)
            {
               LocalVariableBinding binding = ((LocalDeclaration)init).binding;
               mergedInfo.resetAssignmentInfo(binding);
            }
         }
      }
      return mergedInfo;
   }

   public StringBuffer printStatement(int tab, StringBuffer output)
   {

      printIndent(tab, output).append("for ("); //$NON-NLS-1$
      // inits
      if (this.initializations != null)
      {
         for (int i = 0; i < this.initializations.length; i++)
         {
            // nice only with expressions
            if (i > 0)
               output.append(", "); //$NON-NLS-1$
            this.initializations[i].print(0, output);
         }
      }
      output.append("; "); //$NON-NLS-1$
      // cond
      if (this.condition != null)
         this.condition.printExpression(0, output);
      output.append("; "); //$NON-NLS-1$
      // updates
      if (this.increments != null)
      {
         for (int i = 0; i < this.increments.length; i++)
         {
            if (i > 0)
               output.append(", "); //$NON-NLS-1$
            this.increments[i].print(0, output);
         }
      }
      output.append(") "); //$NON-NLS-1$
      // block
      if (this.action == null)
         output.append(';');
      else
      {
         output.append('\n');
         this.action.printStatement(tab + 1, output);
      }
      return output;
   }

   public void resolve(BlockScope upperScope)
   {

      // use the scope that will hold the init declarations
      this.scope = (this.bits & ASTNode.NeededScope) != 0 ? new BlockScope(upperScope) : upperScope;
      if (this.initializations != null)
         for (int i = 0, length = this.initializations.length; i < length; i++)
            this.initializations[i].resolve(this.scope);
      if (this.condition != null)
      {
         TypeBinding type = this.condition.resolveTypeExpecting(this.scope, TypeBinding.BOOLEAN);
         this.condition.computeConversion(this.scope, type, type);
      }
      if (this.increments != null)
         for (int i = 0, length = this.increments.length; i < length; i++)
            this.increments[i].resolve(this.scope);
      if (this.action != null)
         this.action.resolve(this.scope);
   }

   public void traverse(ASTVisitor visitor, BlockScope blockScope)
   {

      if (visitor.visit(this, blockScope))
      {
         if (this.initializations != null)
         {
            int initializationsLength = this.initializations.length;
            for (int i = 0; i < initializationsLength; i++)
               this.initializations[i].traverse(visitor, this.scope);
         }

         if (this.condition != null)
            this.condition.traverse(visitor, this.scope);

         if (this.increments != null)
         {
            int incrementsLength = this.increments.length;
            for (int i = 0; i < incrementsLength; i++)
               this.increments[i].traverse(visitor, this.scope);
         }

         if (this.action != null)
            this.action.traverse(visitor, this.scope);
      }
      visitor.endVisit(this, blockScope);
   }
}
