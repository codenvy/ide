/*
 * Copyright (C) 2013 eXo Platform SAS.
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
package com.codenvy.ide.api.ui.action;

/**
 * @author <a href="mailto:evidolob@codenvy.com">Evgen Vidolob</a>
 * @version $Id:
 */
public interface IdeActions {
    String GROUP_MAIN_MENU    = "MainMenu";
    String GROUP_MAIN_TOOLBAR = "MainToolBar";
    String GROUP_EDITOR_POPUP = "EditorPopupMenu";
    String GROUP_OTHER_MENU   = "OtherMenu";
    String GROUP_EDITOR       = "EditorActions";

    String GROUP_FILE = "FileGroup";

    String GROUP_WINDOW = "WindowGroup";

    String GROUP_PROJECT = "ProjectGroup";

    String GROUP_PROJECT_PAAS = "ProjectPaaSGroup";

    String GROUP_PAAS = "PaaSGroup";

    String GROUP_RUN_MAIN_MENU = "RunGroupMainMenu";

    String GROUP_RUN_TOOLBAR = "RunGroupToolbar";
}