/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package org.exoplatform.ide.client.framework.event;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.ide.vfs.shared.Folder;
import org.exoplatform.ide.vfs.shared.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
 */
public class RefreshBrowserEvent extends GwtEvent<RefreshBrowserHandler> {

    public static final GwtEvent.Type<RefreshBrowserHandler> TYPE = new Type<RefreshBrowserHandler>();

    private List<Folder>                                     folders;

    private Item                                             itemToSelect;

    public RefreshBrowserEvent() {
    }

    public RefreshBrowserEvent(Folder folder) {
        folders = new ArrayList<Folder>();
        folders.add(folder);
    }

    public RefreshBrowserEvent(Folder folder, Item itemToSelect) {
        folders = new ArrayList<Folder>();
        folders.add(folder);
        this.itemToSelect = itemToSelect;
    }

    public RefreshBrowserEvent(List<Folder> folders, Item itemToSelect) {
        this.folders = folders;
        this.itemToSelect = itemToSelect;
    }

    @Override
    protected void dispatch(RefreshBrowserHandler handler) {
        handler.onRefreshBrowser(this);
    }

    public List<Folder> getFolders() {
        ArrayList<Folder> folderList = new ArrayList<Folder>();
        if (folders != null) {
            folderList.addAll(folders);
        }

        return folderList;
    }

    public Item getItemToSelect() {
        return itemToSelect;
    }

    @Override
    public GwtEvent.Type<RefreshBrowserHandler> getAssociatedType() {
        return TYPE;
    }

}
