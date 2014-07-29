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
import com.codenvy.api.project.shared.dto.ItemReference;
import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.editor.EditorPartPresenter;
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
 * Rename provider for renaming {@link ItemReference} objects.
 *
 * @author Artem Zatsarynnyy
 */
public class ItemReferenceRenameProvider implements RenameProvider<ItemReference> {
    private ProjectServiceClient projectServiceClient;
    private NotificationManager  notificationManager;
    private EventBus             eventBus;
    private EditorAgent          editorAgent;

    @Inject
    public ItemReferenceRenameProvider(ProjectServiceClient projectServiceClient, NotificationManager notificationManager,
                                       EventBus eventBus, EditorAgent editorAgent) {
        this.projectServiceClient = projectServiceClient;
        this.notificationManager = notificationManager;
        this.eventBus = eventBus;
        this.editorAgent = editorAgent;
    }

    /** {@inheritDoc} */
    @Override
    public void renameItem(final ItemReference item) {
        new AskValueDialog("Rename file/folder", "New name:", new AskValueCallback() {
            @Override
            public void onOk(String value) {
                final String prevItemPath = item.getPath();
                projectServiceClient.rename(item.getPath(), value, null, new AsyncRequestCallback<Void>() {
                    @Override
                    protected void onSuccess(Void result) {
                        eventBus.fireEvent(new RefreshProjectTreeEvent());

                        if ("file".equals(item.getType())) {
                            for (EditorPartPresenter editor : editorAgent.getOpenedEditors().getValues().asIterable()) {
                                // TODO: replace key (path) in editorAgent.getOpenedEditors()
                                if (prevItemPath.equals(editor.getEditorInput().getFile().getPath())) {
                                    // TODO: editor.getEditorInput().setFile(renamedItem);
                                    editor.onFileChanged();
                                    break;
                                }
                            }
                        } else if ("folder".equals(item.getType())) {
                            // TODO
                        }
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        notificationManager.showNotification(new Notification(exception.getMessage(), Notification.Type.ERROR));
                        Log.error(ItemReferenceRenameProvider.class, exception);
                    }
                });
            }
        }).show();
    }

    /** {@inheritDoc} */
    @Override
    public boolean canRename(Object item) {
        return item instanceof ItemReference;
    }
}
