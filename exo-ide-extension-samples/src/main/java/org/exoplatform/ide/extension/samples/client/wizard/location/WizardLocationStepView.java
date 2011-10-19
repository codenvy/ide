/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.ide.extension.samples.client.wizard.location;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import org.exoplatform.gwtframework.ui.client.api.TreeGridItem;
import org.exoplatform.gwtframework.ui.client.component.ImageButton;
import org.exoplatform.gwtframework.ui.client.component.TextInput;
import org.exoplatform.ide.client.framework.ui.ItemTree;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;
import org.exoplatform.ide.client.framework.ui.impl.ViewType;
import org.exoplatform.ide.extension.samples.client.SamplesExtension;
import org.exoplatform.ide.extension.samples.client.location.SelectLocationView;
import org.exoplatform.ide.vfs.shared.Item;

import java.util.List;

/**
 * TODO: this view duplicates some functionality from
 * {@link SelectLocationView}.
 * In future this must be fix.
 * 
 * View, that displays tree with folders to select location
 * to import sample application.
 * 
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: SelectLocationView.java Aug 31, 2011 12:38:35 PM vereshchaka $
 */
public class WizardLocationStepView extends ViewImpl implements WizardLocationStepPresenter.Display
{
   private static final String ID = "WizardLocationView";
   
   private static final String TITLE = SamplesExtension.LOCALIZATION_CONSTANT.selectLocationTitle();
   
   private static final int HEIGHT = 345;

   private static final int WIDTH = 450;
   
   interface SelectLocationViewUiBinder extends UiBinder<Widget, WizardLocationStepView>
   {
   }
   
   /**
    * UIBinder instance
    */
   private static SelectLocationViewUiBinder uiBinder = GWT.create(SelectLocationViewUiBinder.class);
   
   @UiField
   ItemTree navigationTreeGrid;
   
   @UiField
   TextInput newFolderField;
   
   @UiField
   ImageButton newFolderButton;
   
   @UiField
   ImageButton cancelButton;
   
   @UiField
   ImageButton nextButton;
   
   @UiField
   ImageButton backButton;
   
   public WizardLocationStepView()
   {
      super(ID, ViewType.POPUP, TITLE, null, WIDTH, HEIGHT, false);
      add(uiBinder.createAndBindUi(this));
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getNavigationTree()
    */
   @Override
   public TreeGridItem<Item> getNavigationTree()
   {
      return navigationTreeGrid;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getFolderNameField()
    */
   @Override
   public HasValue<String> getFolderNameField()
   {
      return newFolderField;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getNewFolderButton()
    */
   @Override
   public HasClickHandlers getNewFolderButton()
   {
      return newFolderButton;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getCancelButton()
    */
   @Override
   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getNextButton()
    */
   @Override
   public HasClickHandlers getNextButton()
   {
      return nextButton;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getSelectedItems()
    */
   @Override
   public List<Item> getSelectedItems()
   {
      return navigationTreeGrid.getSelectedItems();
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#selectItem(java.lang.String)
    */
   @Override
   public void selectItem(String itemId)
   {
      navigationTreeGrid.selectItem(itemId);
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#enableNextButton(boolean)
    */
   @Override
   public void enableNextButton(boolean enable)
   {
      nextButton.setEnabled(enable);
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#getBackButton()
    */
   @Override
   public HasClickHandlers getBackButton()
   {
      return backButton;
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#focusInFolderNameField()
    */
   @Override
   public void focusInFolderNameField()
   {
      newFolderField.setFocus(true);
   }

   /**
    * @see org.exoplatform.ide.client.WizardLocationStepPresenter.selectlocation.SelectLocationPresenter.Display#enableNewFolderButton(boolean)
    */
   @Override
   public void enableNewFolderButton(boolean enable)
   {
      newFolderButton.setEnabled(enable);
   }

}
