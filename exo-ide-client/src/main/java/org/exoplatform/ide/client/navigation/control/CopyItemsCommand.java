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
package org.exoplatform.ide.client.navigation.control;

import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.IDEImageBundle;
import org.exoplatform.ide.client.framework.annotation.RolesAllowed;
import org.exoplatform.ide.client.framework.navigation.event.ItemsSelectedEvent;
import org.exoplatform.ide.client.framework.navigation.event.ItemsSelectedHandler;
import org.exoplatform.ide.client.framework.ui.api.event.ViewActivatedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewActivatedHandler;
import org.exoplatform.ide.client.framework.vfs.Item;
import org.exoplatform.ide.client.navigation.WorkspacePresenter;
import org.exoplatform.ide.client.navigation.event.CopyItemsEvent;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
*/
@RolesAllowed({"administrators", "developers"})
public class CopyItemsCommand extends MultipleSelectionItemsCommand implements ItemsSelectedHandler,
   ViewActivatedHandler
{

   public static final String ID = "Edit/Copy Item(s)";
   
   private static final String TITLE = IDE.IDE_LOCALIZATION_CONSTANT.copyItemsTitleControl();
   
   private static final String PROMPT = IDE.IDE_LOCALIZATION_CONSTANT.copyItemsPromptControl();

   private Item selectedItem;

   private boolean copyReady = false;

   public CopyItemsCommand()
   {
      super(ID);
      setTitle(TITLE);
      setPrompt(PROMPT);
      setImages(IDEImageBundle.INSTANCE.copy(), IDEImageBundle.INSTANCE.copyDisabled());
      setEvent(new CopyItemsEvent());

   }

   /**
    * @see org.exoplatform.ide.client.navigation.control.MultipleSelectionItemsCommand#initialize(com.google.gwt.event.shared.HandlerManager)
    */
   @Override
   public void initialize(HandlerManager eventBus)
   {
      eventBus.addHandler(ItemsSelectedEvent.TYPE, this);
      super.initialize(eventBus);
   }

   public void onItemsSelected(ItemsSelectedEvent event)
   {
      if (event.getSelectedItems().size() != 0)
      {
         selectedItem = event.getSelectedItems().get(0);
         copyReady = isItemsInSameFolder(event.getSelectedItems());
         updateEnabling();
      }
      else
      {
         setEnabled(false);
         return;
      }
   }

   @Override
   protected void updateEnabling()
   {
      if (!browserSelected)
      {
         setEnabled(false);
         return;
      }

      if (selectedItem == null)
      {
         setEnabled(false);
         return;
      }

      if (copyReady)
      {
         setEnabled(true);
      }
      else
      {
         setEnabled(false);
      }
   }

   public void onViewActivated(ViewActivatedEvent event)
   {
      if (event.getView() instanceof WorkspacePresenter.Display)
      {
         setEnabled(true);
      }
      else
      {
         setEnabled(false);
      }
   }

}
