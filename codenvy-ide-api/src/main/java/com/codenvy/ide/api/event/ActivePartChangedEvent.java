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
package com.codenvy.ide.api.event;

import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.google.gwt.event.shared.GwtEvent;


/**
 * Event that notifies of changed Core Expressions
 *
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public class ActivePartChangedEvent extends GwtEvent<ActivePartChangedHandler> {
    public static Type<ActivePartChangedHandler> TYPE = new Type<ActivePartChangedHandler>();

    private final PartPresenter activePart;

    /**
     * @param expressions
     *         the map of ID's and current values
     */
    public ActivePartChangedEvent(PartPresenter activePart) {
        this.activePart = activePart;
    }

    @Override
    public Type<ActivePartChangedHandler> getAssociatedType() {
        return TYPE;
    }

    /** @return instance of Active Part */
    public PartPresenter getActivePart() {
        return activePart;
    }

    @Override
    protected void dispatch(ActivePartChangedHandler handler) {
        handler.onActivePartChanged(this);
    }
}
