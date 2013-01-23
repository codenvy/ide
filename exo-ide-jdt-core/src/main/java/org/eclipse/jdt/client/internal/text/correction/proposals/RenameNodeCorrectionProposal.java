/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.client.internal.text.correction.proposals;

import com.google.gwt.user.client.ui.Image;

import org.eclipse.jdt.client.JdtClientBundle;
import org.eclipse.jdt.client.core.dom.ASTNode;
import org.eclipse.jdt.client.core.dom.CompilationUnit;
import org.eclipse.jdt.client.core.dom.NodeFinder;
import org.eclipse.jdt.client.core.dom.SimpleName;
import org.eclipse.jdt.client.internal.corext.dom.LinkedNodeFinder;
import org.eclipse.jdt.client.runtime.CoreException;
import org.exoplatform.ide.editor.shared.text.IDocument;
import org.exoplatform.ide.editor.shared.text.edits.ReplaceEdit;
import org.exoplatform.ide.editor.shared.text.edits.TextEdit;

public class RenameNodeCorrectionProposal extends CUCorrectionProposal
{

   private String fNewName;

   private int fOffset;

   private int fLength;

   private final CompilationUnit unit;

   public RenameNodeCorrectionProposal(String name, CompilationUnit cu, int offset, int length, String newName,
      int relevance, IDocument document)
   {
      super(name, relevance, document, new Image(JdtClientBundle.INSTANCE.correction_change()));
      this.unit = cu;
      fOffset = offset;
      fLength = length;
      fNewName = newName;
   }

   /*(non-Javadoc)
    * @see org.eclipse.jdt.internal.ui.text.correction.CUCorrectionProposal#addEdits(org.eclipse.jface.text.IDocument)
    */
   @Override
   protected void addEdits(IDocument doc, TextEdit root) throws CoreException
   {
      super.addEdits(doc, root);

      ASTNode name = NodeFinder.perform(unit, fOffset, fLength);
      if (name instanceof SimpleName)
      {

         SimpleName[] names = LinkedNodeFinder.findByProblems(unit, (SimpleName)name);
         if (names != null)
         {
            for (int i = 0; i < names.length; i++)
            {
               SimpleName curr = names[i];
               root.addChild(new ReplaceEdit(curr.getStartPosition(), curr.getLength(), fNewName));
            }
            return;
         }
      }
      root.addChild(new ReplaceEdit(fOffset, fLength, fNewName));
   }
}
