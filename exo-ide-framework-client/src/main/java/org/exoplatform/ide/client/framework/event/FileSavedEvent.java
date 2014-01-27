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

import org.exoplatform.ide.vfs.client.model.FileModel;

/**
 * Event, generated by IDE, to notify, that file was saved.
 * <p/>
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 */
public class FileSavedEvent extends GwtEvent<FileSavedHandler> {

    public static final GwtEvent.Type<FileSavedHandler> TYPE = new GwtEvent.Type<FileSavedHandler>();

    /** Saved file. */
    private FileModel file;

    /** Source of original file. */
    private String sourceHref;

    /**
     * @param file
     *         - file, that was saved
     * @param sourceHref
     *         - href of original file, that was saved
     */
    public FileSavedEvent(FileModel file, String sourceHref) {
        this.file = file;
        this.sourceHref = sourceHref;
    }

    public FileModel getFile() {
        return file;
    }

    public String getSourceHref() {
        return sourceHref;
    }

    /** {@inheritDoc} */
    @Override
    protected void dispatch(FileSavedHandler handler) {
        handler.onFileSaved(this);
    }

    /** {@inheritDoc} */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<FileSavedHandler> getAssociatedType() {
        return TYPE;
    }

}