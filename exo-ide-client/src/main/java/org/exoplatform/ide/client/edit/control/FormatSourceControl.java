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
package org.exoplatform.ide.client.edit.control;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.gwtframework.ui.client.command.SimpleControl;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.IDEImageBundle;
import org.exoplatform.ide.client.framework.annotation.RolesAllowed;
import org.exoplatform.ide.client.framework.control.GroupNames;
import org.exoplatform.ide.client.framework.control.IDEControl;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.editor.event.EditorFormatTextEvent;
import org.exoplatform.ide.editor.client.api.EditorCapability;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */
@RolesAllowed({"developer"})
public class FormatSourceControl extends SimpleControl implements IDEControl, EditorActiveFileChangedHandler {

    public static final String ID = "Edit/Format";

    private static final String TITLE = IDE.IDE_LOCALIZATION_CONSTANT.formatControl();

    /**
     *
     */
    public FormatSourceControl() {
        super(ID);
        setTitle(TITLE);
        setPrompt(TITLE);
        setImages(IDEImageBundle.INSTANCE.format(), IDEImageBundle.INSTANCE.formatDisabled());
        setEvent(new EditorFormatTextEvent());
        setHotKey("Ctrl+Shift+F");
        setGroupName(GroupNames.EDIT);
    }

    /** @see org.exoplatform.ide.client.framework.control.IDEControl#initialize() */
    @Override
    public void initialize() {
        IDE.addHandler(EditorActiveFileChangedEvent.TYPE, this);
    }

    /** @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform
     * .ide.client.framework.editor.event.EditorActiveFileChangedEvent) */
    @Override
    public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event) {
        // TODO versions
        if (event.getFile() == null || event.getEditor() == null /* || (event.getFile() instanceof Version) */) {
            setVisible(false);
            setEnabled(false);
            return;
        }

        if (event.getEditor().isCapable(EditorCapability.FORMAT_SOURCE)) {
            String mimeType = event.getFile().getMimeType();
            //TODO add possibility to configure editor capability's
            if (MimeType.TEXT_PLAIN.equals(mimeType) || MimeType.TEXT_X_PYTHON.equals(mimeType)) {
                setVisible(false);
                setEnabled(false);
            } else {
                setVisible(true);
                setEnabled(true);
            }
        } else {
            setVisible(false);
            setEnabled(false);
        }
    }
}