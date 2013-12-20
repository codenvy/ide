/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.java.jdt.internal.codeassist.complete;

import com.codenvy.ide.ext.java.jdt.internal.compiler.ast.SingleTypeReference;
import com.codenvy.ide.ext.java.jdt.internal.compiler.lookup.Scope;
import com.codenvy.ide.ext.java.jdt.internal.compiler.lookup.TypeBinding;

public class CompletionOnKeyword1 extends SingleTypeReference implements CompletionOnKeyword {
    private char[][] possibleKeywords;

    public boolean canCompleteEmptyToken;

    public CompletionOnKeyword1(char[] token, long pos, char[] possibleKeyword) {
        this(token, pos, new char[][]{possibleKeyword});
    }

    public CompletionOnKeyword1(char[] token, long pos, char[][] possibleKeywords) {
        super(token, pos);
        this.possibleKeywords = possibleKeywords;
    }

    public boolean canCompleteEmptyToken() {
        return this.canCompleteEmptyToken;
    }

    public char[] getToken() {
        return this.token;
    }

    public char[][] getPossibleKeywords() {
        return this.possibleKeywords;
    }

    public void aboutToResolve(Scope scope) {
        getTypeBinding(scope);
    }

    protected TypeBinding getTypeBinding(Scope scope) {
        throw new CompletionNodeFound(this, scope);
    }

    public StringBuffer printExpression(int indent, StringBuffer output) {

        return output.append("<CompleteOnKeyword:").append(this.token).append('>'); //$NON-NLS-1$
    }
}
