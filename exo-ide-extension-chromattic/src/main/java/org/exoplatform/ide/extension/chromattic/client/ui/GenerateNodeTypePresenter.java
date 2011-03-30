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
package org.exoplatform.ide.extension.chromattic.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;

import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.vfs.File;
import org.exoplatform.ide.extension.chromattic.client.event.GenerateNodeTypeEvent;
import org.exoplatform.ide.extension.chromattic.client.event.GenerateNodeTypeHandler;
import org.exoplatform.ide.extension.chromattic.client.model.EnumNodeTypeFormat;
import org.exoplatform.ide.extension.chromattic.client.model.GenerateNodeTypeResult;
import org.exoplatform.ide.extension.chromattic.client.model.service.ChrommaticService;
import org.exoplatform.ide.extension.chromattic.client.model.service.event.NodeTypeGenerationResultReceivedEvent;

/**
 * Presenter for generating new node type definition view.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Dec 6, 2010 $
 *
 */
public class GenerateNodeTypePresenter implements GenerateNodeTypeHandler, EditorActiveFileChangedHandler
{
   
   interface Display
   {
      /**
       * Close view.
       */
      void closeView();

      /**
       * Get cancel button.
       * 
       * @return {@link HasClickHandlers} cancel button
       */
      HasClickHandlers getCancelButton();

      /**
       * Get generate button.
       * 
       * @return {@link HasClickHandlers} generate button
       */
      HasClickHandlers getGenerateButton();

      /**
       * Get node type definition field.
       * 
       * @return {@link HasValue} node type format field
       */
      HasValue<String> getNodeTypeFormat();

      /**
       * Set values for node type format field.
       * 
       * @param values
       */
      void setNodeTypeFormatValues(String[] values);
   }

   /**
    * Display.
    */
   private Display display;

   /**
    * Handler manager.
    */
   private HandlerManager eventBus;

   /**
    * Active file in editor.
    */
   private File activeFile;

   /**
    * @param eventBus handler manager
    */
   public GenerateNodeTypePresenter(HandlerManager eventBus)
   {
      this.eventBus = eventBus;
      eventBus.addHandler(GenerateNodeTypeEvent.TYPE, this);
      eventBus.addHandler(EditorActiveFileChangedEvent.TYPE, this);
      new GeneratedNodeTypePreviewPresenter(eventBus);
   }

   /**
    * Bind view with presenter.
    * 
    * @param d 
    */
   public void bindDisplay(Display d)
   {
      display = d;

      display.getCancelButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            display.closeView();
         }
      });

      display.getGenerateButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            doGenerateNodeType();
         }
      });

   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.event.GenerateNodeTypeHandler#onGenerateNodeType(org.exoplatform.ide.client.module.chromattic.event.GenerateNodeTypeEvent)
    */
   @Override
   public void onGenerateNodeType(GenerateNodeTypeEvent event)
   {
      if (activeFile == null)
         return;
      bindDisplay(new GenerateNodeTypeForm(eventBus));
      display.setNodeTypeFormatValues(EnumNodeTypeFormat.getValues());
   }

   /**
    * Generate node type definition.
    */
   private void doGenerateNodeType()
   {
      if (activeFile == null)
         return;
      EnumNodeTypeFormat nodeTypeFormat = EnumNodeTypeFormat.valueOf(display.getNodeTypeFormat().getValue());
      ChrommaticService.getInstance().generateNodeType(activeFile.getHref(), nodeTypeFormat,
         new AsyncRequestCallback<GenerateNodeTypeResult>()
         {
            @Override
            protected void onSuccess(GenerateNodeTypeResult result)
            {
               eventBus.fireEvent(new NodeTypeGenerationResultReceivedEvent(result));
               display.closeView();
            }

            @Override
            protected void onFailure(Throwable exception)
            {
               NodeTypeGenerationResultReceivedEvent event =
                  new NodeTypeGenerationResultReceivedEvent(this.getResult());
               event.setException(exception);
               eventBus.fireEvent(event);
               display.closeView();
            }
         });
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent)
    */
   @Override
   public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event)
   {
      activeFile = event.getFile();
   }

}
