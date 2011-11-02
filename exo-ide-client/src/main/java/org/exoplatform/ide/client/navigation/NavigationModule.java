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
package org.exoplatform.ide.client.navigation;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.Images;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesEvent;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler;
import org.exoplatform.ide.client.framework.control.Docking;
import org.exoplatform.ide.client.framework.control.NewItemControl;
import org.exoplatform.ide.client.framework.editor.event.EditorFileClosedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorFileClosedHandler;
import org.exoplatform.ide.client.framework.editor.event.EditorFileOpenedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorFileOpenedHandler;
import org.exoplatform.ide.client.navigation.control.DownloadFileCommand;
import org.exoplatform.ide.client.navigation.control.DownloadZippedFolderCommand;
import org.exoplatform.ide.client.navigation.control.RefreshBrowserControl;
import org.exoplatform.ide.client.navigation.control.SaveAllFilesCommand;
import org.exoplatform.ide.client.navigation.control.SaveFileAsCommand;
import org.exoplatform.ide.client.navigation.control.SaveFileAsTemplateCommand;
import org.exoplatform.ide.client.navigation.control.SaveFileCommand;
import org.exoplatform.ide.client.navigation.control.newitem.CreateFileFromTemplateControl;
import org.exoplatform.ide.client.navigation.control.newitem.NewFileCommandMenuGroup;
import org.exoplatform.ide.client.navigation.control.newitem.NewFilePopupMenuControl;
import org.exoplatform.ide.client.navigation.handler.CreateFileCommandHandler;
import org.exoplatform.ide.client.navigation.handler.CutCopyPasteItemsCommandHandler;
import org.exoplatform.ide.client.navigation.handler.FileClosedHandler;
import org.exoplatform.ide.client.navigation.handler.OpenFileCommandHandler;
import org.exoplatform.ide.client.navigation.handler.SaveAllFilesCommandHandler;
import org.exoplatform.ide.client.navigation.handler.SaveFileAsCommandHandler;
import org.exoplatform.ide.client.navigation.handler.SaveFileCommandHandler;
import org.exoplatform.ide.client.navigation.template.CreateFileFromTemplatePresenter;
import org.exoplatform.ide.client.navigator.NavigatorPresenter;
import org.exoplatform.ide.client.operation.createfolder.CreateFolderPresenter;
import org.exoplatform.ide.client.operation.deleteitem.DeleteItemsPresenter;
import org.exoplatform.ide.client.operation.geturl.GetItemURLPresenter;
import org.exoplatform.ide.client.operation.gotofolder.GoToFolderCommandHandler;
import org.exoplatform.ide.client.operation.openbypath.OpenFileByPathPresenter;
import org.exoplatform.ide.client.operation.openlocalfile.OpenLocalFilePresenter;
import org.exoplatform.ide.client.operation.rename.RenameFilePresenter;
import org.exoplatform.ide.client.operation.rename.RenameFolderPresenter;
import org.exoplatform.ide.client.operation.rename.RenameItemCommand;
import org.exoplatform.ide.client.operation.search.SearchFilesPresenter;
import org.exoplatform.ide.client.operation.search.SearchResultsPresenter;
import org.exoplatform.ide.client.operation.uploadfile.UploadFilePresenter;
import org.exoplatform.ide.client.operation.uploadzip.UploadZipPresenter;
import org.exoplatform.ide.client.progress.ProgressPresenter;
import org.exoplatform.ide.client.remote.OpenFileByURLPresenter;
import org.exoplatform.ide.client.statusbar.NavigatorStatusControl;
import org.exoplatform.ide.client.template.SaveAsTemplatePresenter;
import org.exoplatform.ide.client.versioning.VersionsListPresenter;
import org.exoplatform.ide.client.versioning.control.RestoreToVersionControl;
import org.exoplatform.ide.client.versioning.control.ViewNextVersionControl;
import org.exoplatform.ide.client.versioning.control.ViewPreviousVersionControl;
import org.exoplatform.ide.client.versioning.control.ViewVersionHistoryControl;
import org.exoplatform.ide.client.versioning.control.ViewVersionListControl;
import org.exoplatform.ide.client.versioning.handler.RestoreToVersionCommandHandler;
import org.exoplatform.ide.client.versioning.handler.VersionHistoryCommandHandler;
import org.exoplatform.ide.vfs.client.VirtualFileSystem;
import org.exoplatform.ide.vfs.client.model.FileModel;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
public class NavigationModule implements
   InitializeServicesHandler, EditorFileClosedHandler, EditorFileOpenedHandler
{

   private Map<String, FileModel> openedFiles = new HashMap<String, FileModel>();

   public NavigationModule()
   {
      NewFilePopupMenuControl newFilePopupMenuControl = new NewFilePopupMenuControl();

      IDE.getInstance().addControl(newFilePopupMenuControl, Docking.TOOLBAR, false);
      IDE.getInstance().addControl(new NewFileCommandMenuGroup());
      //eventBus.fireEvent(new RegisterControlEvent(new CreateProjectFromTemplateControl()));
      IDE.getInstance().addControl(new CreateFileFromTemplateControl());

      new CreateFolderPresenter();

      IDE.getInstance().addControl(
         new NewItemControl("File/New/New TEXT", IDE.IDE_LOCALIZATION_CONSTANT.controlNewTextTitle(),
            IDE.IDE_LOCALIZATION_CONSTANT.controlNewTextPrompt(), Images.FileTypes.TXT, MimeType.TEXT_PLAIN)
            .setGroup(1));

      /*      eventBus.fireEvent(new RegisterControlEvent(new NewItemControl("File/New/New JSON File", "JSON File",
               "Create New JSON File", Images.FileTypes.JSON, MimeType.APPLICATION_JSON))); */

      IDE.getInstance().addControl(new ViewVersionHistoryControl(), Docking.TOOLBAR, true);
      IDE.getInstance().addControl(new ViewVersionListControl(), Docking.TOOLBAR, true);
      IDE.getInstance().addControl(new ViewPreviousVersionControl(), Docking.TOOLBAR, true);
      IDE.getInstance().addControl(new ViewNextVersionControl(), Docking.TOOLBAR, true);
      IDE.getInstance().addControl(new RestoreToVersionControl(), Docking.TOOLBAR, true);

      new UploadFilePresenter();
      new UploadZipPresenter();
      new OpenLocalFilePresenter();
      new OpenFileByPathPresenter();
      new OpenFileByURLPresenter();

      IDE.getInstance().addControl(new DownloadFileCommand());
      IDE.getInstance().addControl(new DownloadZippedFolderCommand());
      IDE.getInstance().addControl(new SaveFileCommand(), Docking.TOOLBAR, false);
      IDE.getInstance().addControl(new SaveFileAsCommand(), Docking.TOOLBAR, false);
      IDE.getInstance().addControl(new SaveAllFilesCommand());
      IDE.getInstance().addControl(new SaveFileAsTemplateCommand());
      
      new CutCopyPasteItemsCommandHandler();
      
      IDE.getInstance().addControl(new RenameItemCommand());
      new RenameFilePresenter();
      new RenameFolderPresenter();

      //eventBus.fireEvent(new RegisterControlEvent(new DeleteItemCommand(), DockTarget.TOOLBAR));
      new DeleteItemsPresenter();

      new SearchFilesPresenter();
      new SearchResultsPresenter();
      
      
      IDE.getInstance().addControl(new RefreshBrowserControl(), Docking.TOOLBAR, false);

      new GoToFolderCommandHandler();

      new GetItemURLPresenter();

      IDE.getInstance().addControl(new NavigatorStatusControl(), Docking.STATUSBAR, false);
      //eventBus.fireEvent(new RegisterControlEvent(new CreateProjectTemplateControl()));

      IDE.addHandler(InitializeServicesEvent.TYPE, this);

      IDE.addHandler(EditorFileClosedEvent.TYPE, this);
      IDE.addHandler(EditorFileOpenedEvent.TYPE, this);

      new CreateFileCommandHandler();
      new CreateFileFromTemplatePresenter();
      new OpenFileCommandHandler();
      new SaveFileCommandHandler();
      new SaveFileAsCommandHandler();
      new SaveAllFilesCommandHandler();
      new FileClosedHandler();

      new VersionHistoryCommandHandler();
      new RestoreToVersionCommandHandler();
      new VersionsListPresenter();

      new NavigatorPresenter();
      new SaveAsTemplatePresenter();
      new ProgressPresenter();
      new ShellLinkUpdater();
   }

   public void onInitializeServices(InitializeServicesEvent event)
   {
      String workspace =
         (event.getApplicationConfiguration().getVfsBaseUrl().endsWith("/")) ? event.getApplicationConfiguration()
            .getVfsBaseUrl() + event.getApplicationConfiguration().getVfsId() : event.getApplicationConfiguration()
            .getVfsBaseUrl() + "/" + event.getApplicationConfiguration().getVfsId();
            new VirtualFileSystem(workspace);
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorFileOpenedHandler#onEditorFileOpened(org.exoplatform.ide.client.framework.editor.event.EditorFileOpenedEvent)
    */
   @Override
   public void onEditorFileOpened(EditorFileOpenedEvent event)
   {
      openedFiles = event.getOpenedFiles();
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorFileClosedHandler#onEditorFileClosed(org.exoplatform.ide.client.framework.editor.event.EditorFileClosedEvent)
    */
   @Override
   public void onEditorFileClosed(EditorFileClosedEvent event)
   {
      openedFiles = event.getOpenedFiles();
   }

}
