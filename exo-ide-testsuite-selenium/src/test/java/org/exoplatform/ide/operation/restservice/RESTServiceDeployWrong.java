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
package org.exoplatform.ide.operation.restservice;

import static org.junit.Assert.assertTrue;

import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.MenuCommands;
import org.junit.Test;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
public class RESTServiceDeployWrong extends BaseTest
{

   private static String FILE_NAME = "Example.groovy";

   @Test
   public void testDeployUndeploy() throws Exception
   {
      Thread.sleep(1000);
      openNewFileFromToolbar("REST Service");
      Thread.sleep(1000);

      selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_DOWN);
      selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_END);

      typeText("1");
      Thread.sleep(500);

      saveAsUsingToolbarButton(FILE_NAME);
      Thread.sleep(1000);

      runTopMenuCommand("Run", MenuCommands.Run.DEPLOY_REST_SERVICE);
      Thread.sleep(1500);

      assertTrue(selenium.isElementPresent("scLocator=//VLayout[ID=\"ideOutputForm\"]/"));

      String mess = selenium.getText("//div[contains(@eventproxy,'Record_0')]");
      assertTrue(mess.startsWith("[ERROR] http://127.0.0.1:8888/rest/private/jcr/repository/dev-monit/" + FILE_NAME
         + " deploy failed. Error (500: Internal Server Error)"));
      assertTrue(mess.contains("Unexpected error. Error occurs when parse stream, compiler error:"));
      assertTrue(mess.contains("startup failed, Example.groovy: 2: unable to resolve class javax.ws.rs.Path1"));
      assertTrue(mess
         .contains("@ line 2, column 1.Example.groovy: 6: unable to resolve class Path , unable to find class for annotation"));
      assertTrue(mess
         .contains("@ line 6, column 1.Example.groovy: 9: unable to resolve class Path , unable to find class for annotation"));
      assertTrue(mess.contains("@ line 9, column 3."));
      assertTrue(mess.contains("3 errors"));

      closeTab("0");

      selectItemInWorkspaceTree(FILE_NAME);

      deleteSelectedItem();
   }

}
