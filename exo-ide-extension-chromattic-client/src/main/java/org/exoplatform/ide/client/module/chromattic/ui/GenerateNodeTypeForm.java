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
package org.exoplatform.ide.client.module.chromattic.ui;

import com.google.gwt.user.client.ui.HasValue;

import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

import com.google.gwt.event.dom.client.HasClickHandlers;

import com.smartgwt.client.types.TitleOrientation;

import com.smartgwt.client.types.Alignment;

import com.smartgwt.client.widgets.layout.HLayout;

import com.smartgwt.client.widgets.layout.VLayout;

import com.smartgwt.client.widgets.form.DynamicForm;

import com.google.gwt.event.shared.HandlerManager;

import org.exoplatform.gwtframework.ui.client.api.TextFieldItem;
import org.exoplatform.gwtframework.ui.client.smartgwt.component.IButton;
import org.exoplatform.gwtframework.ui.client.smartgwt.component.SelectItem;
import org.exoplatform.gwtframework.ui.client.smartgwt.component.TextField;
import org.exoplatform.ide.client.framework.ui.DialogWindow;
import org.exoplatform.ide.client.framework.ui.event.ViewClosedEvent;
import org.exoplatform.ide.client.module.chromattic.Images;

/**
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Dec 6, 2010 $
 *
 */
public class GenerateNodeTypeForm extends DialogWindow implements GenerateNodeTypePresenter.Display
{
   public static final int WIDTH = 505;

   public static final int HEIGHT = 215;

   private final int BUTTON_WIDTH = 90;

   private final int BUTTON_HEIGHT = 22;

   public static final String ID = "ideGenerateNodeTypeForm";

   private final String TITLE = "Generate Node Type";

   //IDs for Selenium tests:

   private final String ID_CANCEL_BUTTON = "ideGenerateNodeTypeFormCancelButton";

   private final String ID_GENERATE_BUTTON = "ideGenerateNodeTypeFormGenerateButton";

   private final String ID_DYNAMIC_FORM = "ideGenerateNodeTypeFormDynamicForm";

   private final String ID_LOCATION_FIELD = "ideGenerateNodeTypeFormLocationField";

   private final String ID_DEPENDENCY_FIELD = "ideGenerateNodeTypeFormDependencyField";

   private final String ID_FORMAT_FIELD = "ideGenerateNodeTypeFormFormatField";

   private TextField locationField;

   private TextField dependencyField;

   private SelectItem formatField;

   private IButton generateButton;

   private IButton cancelButton;

   public GenerateNodeTypeForm(HandlerManager eventBus)
   {
      super(eventBus, WIDTH, HEIGHT, ID);
      setTitle(TITLE);

      VLayout mainLayout = new VLayout();
      mainLayout.setWidth100();
      mainLayout.setHeight100();
      mainLayout.setPadding(20);
      mainLayout.setMembersMargin(20);

      mainLayout.addMember(createMainForm());
      mainLayout.addMember(createButtonLayout());

      addCloseClickHandler(new CloseClickHandler()
      {

         @Override
         public void onCloseClick(CloseClientEvent event)
         {
            destroy();
         }
      });

      addItem(mainLayout);
      show();
   }
   
   /**
    * @see com.smartgwt.client.widgets.BaseWidget#onDestroy()
    */
   @Override
   protected void onDestroy()
   {
      eventBus.fireEvent(new ViewClosedEvent(ID));
      super.onDestroy();
   }

   private DynamicForm createMainForm()
   {
      DynamicForm form = new DynamicForm();
      form.setWrapItemTitles(true);
      form.setID(ID_DYNAMIC_FORM);
      form.setNumCols(2);
      form.setAutoWidth();
      form.setLayoutAlign(Alignment.CENTER);

      locationField = new TextField(ID_LOCATION_FIELD, "Location");
      locationField.setWidth(450);
      locationField.setColSpan(2);
      locationField.setTitleOrientation(TitleOrientation.TOP);
      locationField.setShowDisabled(false);
      locationField.setDisabled(true);

      dependencyField = new TextField(ID_DEPENDENCY_FIELD, "<nobr>"+"Dependency URL"+"</nobr>");
      dependencyField.setTitleOrientation(TitleOrientation.TOP);
      dependencyField.setColSpan(2);
      dependencyField.setWidth(450);

      formatField = new SelectItem();
      formatField.setName(ID_FORMAT_FIELD);
      formatField.setTitle("Node type format");
      formatField.setTitleAlign(Alignment.LEFT);
      formatField.setWidth(350);

      form.setItems(locationField, dependencyField, formatField);
      return form;
   }

   private HLayout createButtonLayout()
   {
      HLayout hLayout = new HLayout();
      hLayout.setMembersMargin(10);
      hLayout.setAutoWidth();
      hLayout.setHeight(BUTTON_HEIGHT);
      hLayout.setLayoutAlign(Alignment.CENTER);

      generateButton = createButton(ID_GENERATE_BUTTON, "Generate", Images.Buttons.OK);
      cancelButton = createButton(ID_CANCEL_BUTTON, "Cancel", Images.Buttons.CANCEL);

      hLayout.addMember(generateButton);
      hLayout.addMember(cancelButton);
      return hLayout;
   }

   /**
    * Created button.
    * 
    * @param id button's id
    * @param title button's display title
    * @param icon button's icon
    * @return {@link IButton} created button
    */
   private IButton createButton(String id, String title, String icon)
   {
      IButton button = new IButton(title);
      button.setID(id);
      button.setWidth(BUTTON_WIDTH);
      button.setHeight(BUTTON_HEIGHT);
      button.setIcon(icon);
      return button;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#closeView()
    */
   @Override
   public void closeView()
   {
      destroy();
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#getCancelButton()
    */
   @Override
   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#getGenerateButton()
    */
   @Override
   public HasClickHandlers getGenerateButton()
   {
      return generateButton;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#getLocation()
    */
   @Override
   public TextFieldItem getLocation()
   {
      return locationField;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#getDependencyLocation()
    */
   @Override
   public TextFieldItem getDependencyLocation()
   {
      return dependencyField;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#getNodeTypeFormat()
    */
   @Override
   public HasValue<String> getNodeTypeFormat()
   {
      return formatField;
   }

   /**
    * @see org.exoplatform.ide.client.module.chromattic.ui.GenerateNodeTypePresenter.Display#setNodeTypeFormatValues()
    */
   @Override
   public void setNodeTypeFormatValues(String[] values)
   {
      formatField.setValueMap(values);
      if (values.length > 0)
      {
         formatField.setValue(values[0]);
      }
   }
}
