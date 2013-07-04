/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.ide.client.framework.ui.impl;

import com.google.gwt.event.shared.HandlerManager;

import org.exoplatform.ide.client.framework.ui.api.View;
import org.exoplatform.ide.client.framework.ui.api.event.*;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 3, 2010 $
 */
public class ViewHighlightManager implements ViewClosedHandler {

    /** Instance of this Highlighting Manager. */
    private static ViewHighlightManager instance;

    /** Currently active view. */
    private View currentActiveView;

    /** Previous active view. */
    private View previousActiveView;

    /** Event Bus. */
    private HandlerManager eventBus;

    /**
     * Creates new instance of this Highlighting Manager.
     *
     * @param eventBus
     *         event bus
     */
    public ViewHighlightManager(HandlerManager eventBus) {
        this.eventBus = eventBus;
        instance = this;

        eventBus.addHandler(ViewClosedEvent.TYPE, this);
    }

    /**
     * Sets view activated.
     *
     * @param view
     *         view to be activated
     */
    public void activateView(View view) {
        if (currentActiveView == view) {
            return;
        }

        previousActiveView = currentActiveView;
        if (currentActiveView != null) {
            currentActiveView.fireEvent(new BeforeViewLoseActivityEvent(currentActiveView));
            ((ViewImpl)currentActiveView).setActivated(false);
            currentActiveView.fireEvent(new ViewLostActivityEvent(currentActiveView));
        }

        currentActiveView = view;
        ((ViewImpl)currentActiveView).setActivated(true);

        eventBus.fireEvent(new ViewActivatedEvent(view));
    }

    /**
     * Get instance of this Highlighting manager.
     *
     * @return the instance
     */
    public static ViewHighlightManager getInstance() {
        if (instance == null) {
            new ViewHighlightManager(null);
        }

        return instance;
    }

    /**
     * View Closed Handler
     *
     * @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api
     * .event.ViewClosedEvent)
     */
    @Override
    public void onViewClosed(ViewClosedEvent event) {
        if (event.getView() != currentActiveView) {
            return;
        }

        if (previousActiveView != null) {
            ((ViewImpl)previousActiveView).setActivated(false);
            currentActiveView = previousActiveView;
            ((ViewImpl)currentActiveView).setActivated(true);
            eventBus.fireEvent(new ViewActivatedEvent(currentActiveView));
        } else {
            currentActiveView = null;
        }
    }

}