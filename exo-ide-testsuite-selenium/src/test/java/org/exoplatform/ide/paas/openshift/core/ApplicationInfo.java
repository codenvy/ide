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
package org.exoplatform.ide.paas.openshift.core;

import org.exoplatform.ide.MenuCommands;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.core.AbstractTestModule;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class ApplicationInfo extends AbstractTestModule
{

   private static interface Locators
   {

      String APPLICATION_INFO_WINDOW = "ide.OpenShift.ApplicationInfo.View-window";
      
      String APPLICATION_INFO_VIEW = "ide.OpenShift.ApplicationInfo.View";

      String APPLICATION_INFO_LISTGRID = "ide.OpenShift.ApplicationInfo.ListGrid";

      String APPLICATION_INFO_OK_BUTTON = "ide.OpenShift.ApplicationInfo.OkButton";

   }

   public void openApplicationInfoWindow() throws Exception
   {
      IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT,
         MenuCommands.PaaS.OpenShift.APPLICATION_INFO);
      waitForElementPresent(Locators.APPLICATION_INFO_WINDOW);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }

   public boolean isApplicationInfoWindowOpened()
   {
      return selenium().isElementPresent(Locators.APPLICATION_INFO_WINDOW);
   }

   public void waitForApplicationInfoWindowPresent() throws Exception
   {
      waitForElementPresent(Locators.APPLICATION_INFO_WINDOW);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }

   public void waitForApplicationInfoWindowNotPresent() throws Exception
   {
      waitForElementNotPresent(Locators.APPLICATION_INFO_WINDOW);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }

   public void clickOkButton() throws InterruptedException
   {
      selenium().click(Locators.APPLICATION_INFO_OK_BUTTON);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }
   
   /**
    * @param row  row, starts at 1
    * @param column column starts at 1
    * @return text in the cell [row : column]
    */
   public String getTextFromAppInfoTable(int row, int column) {
      String locator = "//table[@id='ide.OpenShift.ApplicationInfo.ListGrid']/tbody/tr[" + row + "]/td[" + column + "]/div";
      String text = selenium().getText(locator);
      return text;
   }

}
