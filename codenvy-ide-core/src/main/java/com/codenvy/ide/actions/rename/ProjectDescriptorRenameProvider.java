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
package com.codenvy.ide.actions.rename;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.event.RefreshProjectTreeEvent;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.ui.dialogs.askValue.AskValueCallback;
import com.codenvy.ide.ui.dialogs.askValue.AskValueDialog;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Rename provider for renaming {@link ProjectDescriptor} objects.
 *
 * @author Artem Zatsarynnyy
 */
public class ProjectDescriptorRenameProvider implements RenameProvider<ProjectDescriptor> {
    private ProjectServiceClient projectServiceClient;
    private NotificationManager  notificationManager;
    private EventBus             eventBus;

    @Inject
    public ProjectDescriptorRenameProvider(ProjectServiceClient projectServiceClient, NotificationManager notificationManager,
                                           EventBus eventBus) {
        this.projectServiceClient = projectServiceClient;
        this.notificationManager = notificationManager;
        this.eventBus = eventBus;
    }

    /** {@inheritDoc} */
    @Override
    public void renameItem(final ProjectDescriptor item) {
        new AskValueDialog("Rename project", "New name:", new AskValueCallback() {
            @Override
            public void onOk(String value) {
                projectServiceClient.rename(item.getPath(), value, null, new AsyncRequestCallback<Void>() {
                    @Override
                    protected void onSuccess(Void result) {
                        eventBus.fireEvent(new RefreshProjectTreeEvent());
                        // TODO: check opened files
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        notificationManager.showNotification(new Notification(exception.getMessage(), Notification.Type.ERROR));
                        Log.error(ProjectDescriptorRenameProvider.class, exception);
                    }
                });
            }
        }).show();
    }

    /** {@inheritDoc} */
    @Override
    public boolean canRename(Object item) {
        return item instanceof ProjectDescriptor;
        // TODO: check that item isn't a module
    }
}
