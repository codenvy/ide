/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.codenvy.ide.texteditor.codeassistant;

import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.quickassist.QuickAssistInvocationContext;

/**
 * Text quick assist invocation context.
 *
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 */
public class TextInvocationContext implements QuickAssistInvocationContext {

    private TextEditorPartView view;

    private int offset;

    private int length;

    /**
     * @param view
     * @param offset
     * @param length
     */
    public TextInvocationContext(TextEditorPartView view, int offset, int length) {
        super();
        this.view = view;
        this.offset = offset;
        this.length = length;
    }

    /** {@inheritDoc} */
    @Override
    public int getOffset() {
        return offset;
    }

    /** {@inheritDoc} */
    @Override
    public int getLength() {
        return length;
    }

    /** {@inheritDoc} */
    @Override
    public TextEditorPartView getTextEditor() {
        return view;
    }

}