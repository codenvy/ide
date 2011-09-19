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
package org.exoplatform.ide.extension.samples.client.wizard.deployment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import org.exoplatform.gwtframework.ui.client.component.ImageButton;
import org.exoplatform.gwtframework.ui.client.component.SelectItem;
import org.exoplatform.gwtframework.ui.client.component.TextField;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;
import org.exoplatform.ide.client.framework.ui.impl.ViewType;
import org.exoplatform.ide.extension.samples.client.SamplesClientBundle;
import org.exoplatform.ide.extension.samples.client.SamplesExtension;

/**
 * View to select PaaS in Wizard for Java project creation.
 * 
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: SourceWizardView.java Sep 7, 2011 2:59:09 PM vereshchaka $
 */
public class WizardDeploymentStepView extends ViewImpl implements WizardDeploymentStepPresenter.Display 
{
   private static final String ID = "WizardProjectDeploymentView";
   
   private static final String TITLE = SamplesExtension.LOCALIZATION_CONSTANT.wizardDeploymentDialogTitle();
   
   private static final int HEIGHT = 345;

   private static final int WIDTH = 450;
   
   interface SourceWizardViewUiBinder extends UiBinder<Widget, WizardDeploymentStepView>
   {
   }
   
   /**
    * UIBinder instance
    */
   private static SourceWizardViewUiBinder uiBinder = GWT.create(SourceWizardViewUiBinder.class);
   
   @UiField
   SelectItem selectPaasField;
   
   @UiField
   ImageButton cancelButton;
   
   @UiField
   ImageButton nextButton;
   
   @UiField
   ImageButton backButton;
   
   @UiField
   HTMLPanel paasPanel;
   
   @UiField
   TextField cloudFoundryNameField;
   
   @UiField
   TextField cloudFoundryUrlField;
   
   @UiField
   SelectItem selectDomainField;
   
   @UiField
   TextField cloudBeesNameField;
   
   @UiField
   TextField cloudBeesIdField;
   
   public WizardDeploymentStepView()
   {
      super(ID, ViewType.POPUP, TITLE, null, WIDTH, HEIGHT);
      add(uiBinder.createAndBindUi(this));
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.source.WizardSourceStepPresenter.Display#getNextButton()
    */
   @Override
   public HasClickHandlers getNextButton()
   {
      return nextButton;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.source.WizardSourceStepPresenter.Display#getCancelButton()
    */
   @Override
   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.source.WizardSourceStepPresenter.Display#getSelectSourceField()
    */
   @Override
   public HasValue<String> getSelectPaasField()
   {
      return selectPaasField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getBackButton()
    */
   @Override
   public HasClickHandlers getBackButton()
   {
      return backButton;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#setPaasValueMap(java.lang.String[])
    */
   @Override
   public void setPaasValueMap(String[] values, String selected)
   {
      selectPaasField.setValueMap(values, selected);
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#setVisibleCloudBeesPanel(boolean)
    */
   @Override
   public void setVisibleCloudBeesPanel(boolean visible)
   {
      if (visible)
      {
         paasPanel.getElementById("cloudBeesTable").removeClassName(SamplesClientBundle.INSTANCE.css().hiddenTable());
         paasPanel.getElementById("cloudBeesTable").addClassName(SamplesClientBundle.INSTANCE.css().visibleTable());
      }
      else
      {
         paasPanel.getElementById("cloudBeesTable").removeClassName(SamplesClientBundle.INSTANCE.css().visibleTable());
         paasPanel.getElementById("cloudBeesTable").addClassName(SamplesClientBundle.INSTANCE.css().hiddenTable());
      }
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#setVisibleCloudFoundryPanel(boolean)
    */
   @Override
   public void setVisibleCloudFoundryPanel(boolean visible)
   {
      if (visible)
      {
         paasPanel.getElementById("cloudFoundryTable").removeClassName(SamplesClientBundle.INSTANCE.css().hiddenTable());
         paasPanel.getElementById("cloudFoundryTable").addClassName(SamplesClientBundle.INSTANCE.css().visibleTable());
      }
      else
      {
         paasPanel.getElementById("cloudFoundryTable").removeClassName(SamplesClientBundle.INSTANCE.css().visibleTable());
         paasPanel.getElementById("cloudFoundryTable").addClassName(SamplesClientBundle.INSTANCE.css().hiddenTable());
      }
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getSelectCloudBeesDomainField()
    */
   @Override
   public HasValue<String> getSelectCloudBeesDomainField()
   {
      return selectDomainField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getCloudFoundryNameField()
    */
   @Override
   public HasValue<String> getCloudFoundryNameField()
   {
      return cloudFoundryNameField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getCloudFoundryUrlField()
    */
   @Override
   public HasValue<String> getCloudFoundryUrlField()
   {
      return cloudFoundryUrlField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getCloudBeesNameField()
    */
   @Override
   public HasValue<String> getCloudBeesNameField()
   {
      return cloudBeesNameField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#setCloudBeesDomainsValueMap(java.lang.String[])
    */
   @Override
   public void setCloudBeesDomainsValueMap(String[] values)
   {
      selectDomainField.setValueMap(values);
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#getCloudBeesIdField()
    */
   @Override
   public HasValue<String> getCloudBeesIdField()
   {
      return cloudBeesIdField;
   }

   /**
    * @see org.exoplatform.ide.extension.samples.client.wizard.deployment.WizardDeploymentStepPresenter.Display#enableNextButton(boolean)
    */
   @Override
   public void enableNextButton(boolean enable)
   {
      nextButton.setEnabled(enable);
   }

}
