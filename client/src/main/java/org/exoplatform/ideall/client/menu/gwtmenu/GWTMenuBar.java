/**
 * Copyright (C) 2009 eXo Platform SAS.
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
 *
 */
package org.exoplatform.ideall.client.menu.gwtmenu;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.exoplatform.ideall.client.menu.gwtmenu.bean.MenuBarItem;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class GWTMenuBar extends Composite implements GWTMenuBarPresenter.Display
{

   public static interface Style
   {

      public final static String MENUBAR = "exo-menuBar";

      public final static String MENUBAR_TABLE = "exo-menuBarTable";

      public final static String ITEM = "exo-menuBarItem";

      public final static String ITEM_OVER = "exo-menuBarItemOver";

      public final static String ITEM_SELECTED = "exo-menuBarItemSelected";

      public final static String ITEM_DISABLED = "exo-menuBarItemDisabled";

   }

   public final static String TITLE_PROPERTY = "menuBarTitle";

   public final static String ENABLED_PROPERTY = "menuBarEnabled";

   public final static int MENU_HEIGHT = 20;

   private HandlerManager eventBus;

   private AbsolutePanel absolutePanel;

   private AbsolutePanel lockLayer;

   private GWTMenuBarPresenter presenter;

   public GWTMenuBar(HandlerManager eventBus)
   {
      this.eventBus = eventBus;

      absolutePanel = new AbsolutePanel();
      initWidget(absolutePanel);
      absolutePanel.setStyleName(Style.MENUBAR);

      presenter = new GWTMenuBarPresenter(eventBus);
      presenter.bindDisplay(this);
   }

   protected Element selectedElement;

   protected GWTPopupMenu visiblePopup;

   protected LinkedHashMap<String, MenuBarItem> menuBarItems;

   protected void menuItemSelected(Element element)
   {
      if (selectedElement != null)
      {
         selectedElement.setClassName(Style.ITEM);
      }
      selectedElement = element;

      if (visiblePopup != null)
      {
         visiblePopup.removeFromParent();
      }

      showPopupMenu();
   }

   public void buildMenu(LinkedHashMap<String, MenuBarItem> menuBarItems)
   {
      this.menuBarItems = menuBarItems;

      MenuBarTable table = new MenuBarTable();
      table.setStyleName(Style.MENUBAR_TABLE);
      table.setCellPadding(0);
      table.setCellSpacing(0);
      DOM.setElementAttribute(table.getElement(), "border", "0");
      absolutePanel.add(table);

      Iterator<String> iterator = menuBarItems.keySet().iterator();
      int i = 0;
      while (iterator.hasNext())
      {
         String title = iterator.next();
         MenuBarItem item = menuBarItems.get(title);

         table.setText(0, i, title);
         item.initElement(table.getCellFormatter().getElement(0, i));
         i++;
      }

   }

   private class LockLayer extends AbsolutePanel
   {

      public LockLayer()
      {
         sinkEvents(Event.ONMOUSEDOWN);
      }

      @Override
      public void onBrowserEvent(Event event)
      {
         switch (DOM.eventGetType(event))
         {
            case Event.ONMOUSEDOWN :
               unlockScreen();
               break;

         }

      }

   }

   private void lockScreen()
   {
      if (lockLayer != null)
      {
         return;
      }

      lockLayer = new AbsolutePanel();
      RootPanel.get().add(lockLayer, 0, MENU_HEIGHT);
      lockLayer.setWidth("" + Window.getClientWidth() + "px");
      lockLayer.setHeight("" + (Window.getClientHeight() - MENU_HEIGHT) + "px");
      DOM.setElementAttribute(lockLayer.getElement(), "id", "menu-lock-layer-id");
      DOM.setStyleAttribute(lockLayer.getElement(), "zIndex", "" + (Integer.MAX_VALUE));

      AbsolutePanel blockMouseEventsPanel = new LockLayer();
      blockMouseEventsPanel.setStyleName("exo-lockLayer");
      blockMouseEventsPanel.setWidth("" + Window.getClientWidth() + "px");
      blockMouseEventsPanel.setHeight("" + (Window.getClientHeight() - MENU_HEIGHT) + "px");
      lockLayer.add(blockMouseEventsPanel, 0, 0);
   }

   private void unlockScreen()
   {
      if (lockLayer == null)
      {
         return;
      }

      lockLayer.removeFromParent();
      lockLayer = null;

      if (selectedElement != null)
      {
         selectedElement.setClassName(Style.ITEM);
         selectedElement = null;
      }
   }

   private void showPopupMenu()
   {
      lockScreen();

      int x = selectedElement.getAbsoluteLeft();
      int y = selectedElement.getAbsoluteTop();

      String menuBarTitle = DOM.getElementAttribute(selectedElement, TITLE_PROPERTY);

      MenuBarItem menuBarItem = menuBarItems.get(menuBarTitle);
      visiblePopup = new GWTPopupMenu(eventBus, menuBarItem.getPopupItems(), lockLayer, null);
      lockLayer.add(visiblePopup, x, y);
   }

   public void closeMenu()
   {
      unlockScreen();
   }

   private class MenuBarTable extends FlexTable
   {

      public MenuBarTable()
      {
         sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEDOWN);
      }

      @Override
      public void onBrowserEvent(Event event)
      {
         Element td = getEventTargetCell(event);
         if (td == null)
            return;
         if (td == selectedElement)
         {
            return;
         }

         boolean enabled = Boolean.parseBoolean(DOM.getElementAttribute(td, ENABLED_PROPERTY));
         if (!enabled)
         {
            return;
         }

         switch (DOM.eventGetType(event))
         {
            case Event.ONMOUSEOVER : {
               td.setClassName(Style.ITEM_OVER);
               break;
            }

            case Event.ONMOUSEOUT : {
               td.setClassName(Style.ITEM);
               break;
            }

            case Event.ONMOUSEDOWN : {
               td.setClassName(Style.ITEM_SELECTED);
               menuItemSelected(td);
               break;
            }

         }

      }
   }

}
