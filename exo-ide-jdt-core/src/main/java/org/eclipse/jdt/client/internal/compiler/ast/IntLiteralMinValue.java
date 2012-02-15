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
package org.eclipse.jdt.client.internal.compiler.ast;

import org.eclipse.jdt.client.internal.compiler.impl.IntConstant;

public class IntLiteralMinValue extends IntLiteral
{

   public IntLiteralMinValue(char[] token, char[] reducedToken, int start, int end)
   {
      super(token, reducedToken, start, end, Integer.MIN_VALUE, IntConstant.fromValue(Integer.MIN_VALUE));
   }

   public void computeConstant()
   {
      /* precomputed at creation time */}
}
