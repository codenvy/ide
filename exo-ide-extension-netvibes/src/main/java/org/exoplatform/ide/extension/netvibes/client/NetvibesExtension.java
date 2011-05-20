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
package org.exoplatform.ide.extension.netvibes.client;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesEvent;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler;
import org.exoplatform.ide.client.framework.configuration.IDEConfiguration;
import org.exoplatform.ide.client.framework.control.event.RegisterControlEvent.DockTarget;
import org.exoplatform.ide.client.framework.documentation.RegisterDocumentationEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.module.Extension;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.ui.PreviewForm;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.client.framework.vfs.File;
import org.exoplatform.ide.extension.netvibes.client.controls.DeployUwaWidgetControl;
import org.exoplatform.ide.extension.netvibes.client.controls.ShowNetvibesPreviewControl;
import org.exoplatform.ide.extension.netvibes.client.event.PreviewNetvibesEvent;
import org.exoplatform.ide.extension.netvibes.client.event.PreviewNetvibesHandler;
import org.exoplatform.ide.extension.netvibes.client.service.deploy.DeployWidgetServiceImpl;
import org.exoplatform.ide.extension.netvibes.client.ui.DeployUwaWidgetPresenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Image;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $Id: $
 */

public class NetvibesExtension extends Extension implements InitializeServicesHandler, PreviewNetvibesHandler,
   EditorActiveFileChangedHandler, ViewClosedHandler
{
   private HandlerManager eventBus;

   /**
    * IDE application configuration.
    */
   private IDEConfiguration configuration;
   
   /**
    * Current opened file in editor
    */
   private File activeFile;
   
   private boolean previewOpened = false;
   
   private PreviewForm previewForm;

   /**
    * @see org.exoplatform.ide.client.framework.module.Extension#initialize(com.google.gwt.event.shared.HandlerManager)
    */
   @Override
   public void initialize()
   {
      this.eventBus = IDE.EVENT_BUS;

  
      IDE.getInstance().addControl(new DeployUwaWidgetControl(), DockTarget.TOOLBAR, true);
      IDE.getInstance().addControl(new ShowNetvibesPreviewControl(), DockTarget.TOOLBAR, true);

      eventBus.addHandler(InitializeServicesEvent.TYPE, this);
      eventBus.addHandler(PreviewNetvibesEvent.TYPE, this);
      eventBus.addHandler(EditorActiveFileChangedEvent.TYPE, this);
      eventBus.addHandler(ViewClosedEvent.TYPE, this);

      new DeployUwaWidgetPresenter(eventBus);

      NetvibesClientBundle.INSTANCE.css().ensureInjected();

      eventBus.fireEvent(new RegisterDocumentationEvent(MimeType.UWA_WIDGET,
         "http://dev.netvibes.com/doc/uwa/documentation"));
   }

   /**
    * @see org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler#onInitializeServices(org.exoplatform.ide.client.framework.application.event.InitializeServicesEvent)
    */
   @Override
   public void onInitializeServices(InitializeServicesEvent event)
   {
      configuration = event.getApplicationConfiguration();
      new DeployWidgetServiceImpl(eventBus, configuration.getContext(), event.getLoader());
   }

   /**
    * @see org.exoplatform.ide.extension.netvibes.client.event.PreviewNetvibesHandler#onPreviewNetvibes(org.exoplatform.ide.extension.netvibes.client.event.PreviewNetvibesEvent)
    */
   @Override
   public void onPreviewNetvibes(PreviewNetvibesEvent event)
   {
      if(activeFile == null)
         return;
     
      
      String href = activeFile.getHref();
      href = href.replace("jcr", "ide/netvibes");
      
      if (previewForm == null)
      {
         previewForm = new PreviewForm();
         previewForm.setIcon(new Image(NetvibesClientBundle.INSTANCE.preview()));
      }
      previewForm.showPreview(href);
      
      if (previewOpened)
      {
         previewForm.setViewVisible();
      }
      else
      {
         IDE.getInstance().openView(previewForm);
      }
      previewOpened = true;
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent)
    */
   @Override
   public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event)
   {
      activeFile = event.getFile();
      if(previewOpened)
      {
         IDE.getInstance().closeView(PreviewForm.ID);
         previewOpened = false;
      }
   }

   /**
    * @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent)
    */
   @Override
   public void onViewClosed(ViewClosedEvent event)
   {
      if (previewForm == null)
         return;
      if (event.getView().getId().equals(previewForm.getId()))
      {
         previewOpened = false;
         previewForm = null;
      }
   }

}
