/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.eclipse.jdt.client;

import org.eclipse.jdt.client.event.ViewJavadocEvent;
import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.gwtframework.commons.util.BrowserResolver;
import org.exoplatform.gwtframework.ui.client.command.SimpleControl;
import org.exoplatform.ide.client.framework.control.IDEControl;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.module.IDE;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 *
 */
public class ViewJavadocControl extends SimpleControl implements IDEControl, EditorActiveFileChangedHandler
{

   /**
    * 
    */
   public ViewJavadocControl()
   {
      super("View/Quick Documentation");
      setTitle("Quick Documentation");
      setPrompt("Quick Documentation");
      if (BrowserResolver.isMacOs())
      {
         setHotKey("Ctrl+Shift+Q");
      }
      else
      {
         setHotKey("Ctrl+Q");
      }
      setEvent(new ViewJavadocEvent());
      setImages(JdtClientBundle.INSTANCE.javadoc(), JdtClientBundle.INSTANCE.javadoc());
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent)
    */
   @Override
   public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event)
   {
      if (event.getFile() != null)
      {
         if (MimeType.APPLICATION_JAVA.equals(event.getFile().getMimeType()))
         {
            setEnabled(true);
            return;
         }
      }
      setEnabled(false);
   }

   /**
    * @see org.exoplatform.ide.client.framework.control.IDEControl#initialize()
    */
   @Override
   public void initialize()
   {
      setEnabled(false);
      setVisible(false);
      setShowInMenu(false);
      setShowInContextMenu(true);
      IDE.addHandler(EditorActiveFileChangedEvent.TYPE, this);
   }

}
