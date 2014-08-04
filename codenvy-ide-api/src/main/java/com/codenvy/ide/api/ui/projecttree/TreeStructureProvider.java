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
package com.codenvy.ide.api.ui.projecttree;

/**
 * Tree structure provider responsible for creating {@link AbstractTreeStructure} instances of desired type.
 *
 * @author Artem Zatsarynnyy
 */
public interface TreeStructureProvider {
    /**
     * Creates an empty {@link AbstractTreeStructure} instance of the corresponding class.
     *
     * @return {@link AbstractTreeStructure} instance
     */
    AbstractTreeStructure getTreeStructure();
}
