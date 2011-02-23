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
package org.exoplatform.ide.extension.groovy.client.codeassistant;

import com.google.gwt.event.shared.HandlerManager;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.exception.ServerException;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.ide.client.framework.codeassistant.TokenExt;
import org.exoplatform.ide.client.framework.codeassistant.api.ImportDeclarationTokenCollector;
import org.exoplatform.ide.client.framework.codeassistant.api.ImportDeclarationTokenCollectorCallback;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage;
import org.exoplatform.ide.client.framework.vfs.File;
import org.exoplatform.ide.extension.groovy.client.service.codeassistant.CodeAssistantService;

import java.util.List;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 22, 2010 2:47:20 PM evgen $
 *
 */
public class GroovyImportDeclarationTokenCollector implements ImportDeclarationTokenCollector, EditorActiveFileChangedHandler
{

   private ImportDeclarationTokenCollectorCallback callback;

   private HandlerManager eventBus;

   private File activeFile;

   /**
    * @param eventBus
    */
   public GroovyImportDeclarationTokenCollector(HandlerManager eventBus)
   {
      eventBus.addHandler(EditorActiveFileChangedEvent.TYPE, this);
   }

   /**
    * @see org.exoplatform.ide.client.framework.codeassistant.api.ImportDeclarationTokenCollector#getImportDeclarationTokens(java.lang.String)
    */
   public void collectImportDeclarationTokens(String className, final ImportDeclarationTokenCollectorCallback tokenCallback)
   {
      this.callback = tokenCallback;
      CodeAssistantService.getInstance().findClass(className, activeFile.getHref(), new AsyncRequestCallback<List<TokenExt>>()
      {
         
         @Override
         protected void onSuccess(List<TokenExt> result)
         {
            callback.tokensCollected(result);
         }
         
         @Override
         protected void onFailure(Throwable exc)
         {
            if (exc instanceof ServerException)
            {
               ServerException exception = (ServerException)exc;
               String outputContent =
                  "Error (<i>" + exception.getHTTPStatus() + "</i>: <i>" + exception.getStatusText() + "</i>)";
               if (!exception.getMessage().equals(""))
               {
                  outputContent += "<br />" + exception.getMessage().replace("\n", "<br />"); // replace "end of line" symbols on "<br />"
               }

               //         findLineNumberAndColNumberOfError(exception.getMessage());

               //         outputContent =
               //            "<span title=\"Go to error\" onClick=\"window.groovyGoToErrorFunction(" + String.valueOf(errLineNumber)
               //               + "," + String.valueOf(errColumnNumber) + ", '" + event.getFileHref() + "', '"
               //               + "');\" style=\"cursor:pointer;\">" + outputContent + "</span>";

               eventBus.fireEvent(new OutputEvent(outputContent, OutputMessage.Type.ERROR));
            }
            else
            {
               eventBus.fireEvent(new ExceptionThrownEvent(exc.getMessage()));
            }
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
