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
package org.exoplatform.ideall.client.outline;

import org.exoplatform.ideall.client.editor.MinMaxControlButton;
import org.exoplatform.ideall.client.event.perspective.CodeHelperPanelRestoredEvent;
import org.exoplatform.ideall.client.event.perspective.CodeHelperPanelRestoredHandler;
import org.exoplatform.ideall.client.event.perspective.MaximizeCodeHelperPanelEvent;
import org.exoplatform.ideall.client.event.perspective.RestoreCodeHelperPanelEvent;
import org.exoplatform.ideall.client.form.event.OpenedFormsStateChangedEvent;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.outline.event.ShowOutlineEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id:
 *
 */
public class CodeHelperForm extends Layout implements CodeHelperPresenter.Display, 
CodeHelperPanelRestoredHandler
{
   public static String ID = "CodeHelper";
   
   private HandlerManager eventBus;
   
   private ApplicationContext context;
   
   private CodeHelperPresenter presenter;
   
   private TabSet tabSet;
   
   private OutlineForm outlineTab;
   
   private Layout tabBarColtrols;
   
   protected MinMaxControlButton minMaxControlButton;
   
   public CodeHelperForm(HandlerManager bus, ApplicationContext applicationContext)
   {
      eventBus = bus;
      context = applicationContext;
      
      tabSet = new TabSet();
      createButtons();
      outlineTab = new OutlineForm(eventBus, context);
      tabSet.addTab(outlineTab);
      addMember(tabSet);
      tabSet.addCloseClickHandler(closeClickHandler);

      presenter = new CodeHelperPresenter(eventBus, context);
      presenter.bindDisplay(this);
   }
   
   private void createButtons()
   {
      tabBarColtrols = new Layout();
      tabBarColtrols.setHeight(18);
      tabBarColtrols.setAutoWidth();

      minMaxControlButton =
         new MinMaxControlButton(eventBus, true, new MaximizeCodeHelperPanelEvent(), 
            new RestoreCodeHelperPanelEvent());
      tabBarColtrols.addMember(minMaxControlButton);

      tabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, tabBarColtrols);
   }
   
   public void setTabCanClose(boolean canClose)
   {
      outlineTab.setCanClose(canClose);
   }

   public void onCodeHelperPanelRestored(CodeHelperPanelRestoredEvent event)
   {
      minMaxControlButton.setMaximize(true);
   }
   
   private CloseClickHandler closeClickHandler = new CloseClickHandler()
   {
      public void onCloseClick(TabCloseClickEvent event)
      {
         event.cancel();
         hide();
         eventBus.fireEvent(new ShowOutlineEvent(false));
      }
   };
   
   public void showCodeHelper(boolean isShow)
   {
      if (isShow)
      {
         if (!context.getOpenedForms().contains(ID)){
            context.getOpenedForms().add(ID);
         }
         show();
      }
      else
      {
         context.getOpenedForms().remove(ID);
         hide();
         //         outlineTab.getBrowserTree().setValue(null);
      }

      eventBus.fireEvent(new OpenedFormsStateChangedEvent());
   }
   
}