/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.tutorial.gin.annotation;

import com.codenvy.ide.api.parts.ConsolePart;
import com.google.inject.Inject;

/**
 * The class that uses {@link MyString} annotation for defining string value.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class MyClassWithAnnotationParam {
    private ConsolePart console;
    private String      text;

    @Inject
    public MyClassWithAnnotationParam(ConsolePart console, @MyString String text) {
        this.console = console;
        this.text = text;
    }

    public void doSomething() {
        console.print(text);
    }
}