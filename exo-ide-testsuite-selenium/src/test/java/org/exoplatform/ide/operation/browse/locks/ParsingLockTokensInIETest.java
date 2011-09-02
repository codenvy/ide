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
package org.exoplatform.ide.operation.browse.locks;

import static org.junit.Assert.assertFalse;

import org.everrest.http.client.ModuleException;
import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.MenuCommands;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.ToolbarCommands;
import org.exoplatform.ide.VirtualFileSystemUtils;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Run this test in IE to test bug http://jira.exoplatform.org/browse/IDE-425.
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 11, 2010 $
 *
 */
public abstract class ParsingLockTokensInIETest extends BaseTest
{
 
 private final static String URL = BASE_URL + REST_CONTEXT + "/" + WEBDAV_CONTEXT + "/" + REPO_NAME + "/" + WS_NAME + "/";
   
   private static String FOLDER_NAME = ParsingLockTokensInIETest.class.getSimpleName();

   private static String FILE_NAME = "lhntklshbadsygfbolthg";
   
   @Ignore
   @Test
   public void testParsingLockTokensInIE() throws Exception
   {
      Thread.sleep(TestConstants.SLEEP);
      
      IDE.NAVIGATION.createFolder(FOLDER_NAME);
      IDE.WORKSPACE.selectItem(URL + FOLDER_NAME + "/");
      
      createSaveAndCloseFile(MenuCommands.New.REST_SERVICE_FILE, FILE_NAME, 0);
      
      IDE.WORKSPACE.selectItem(URL + FOLDER_NAME + "/" + FILE_NAME);
      
      IDE.NAVIGATION.openFileFromNavigationTreeWithCodeEditor(FILE_NAME, false);
      
      IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.LOCK_FILE);
      
     IDE.EDITOR.closeFile(0);
      
      assertFalse(selenium().isElementPresent("scLocator=//Dialog[ID=\"isc_globalWarn\"]"));
   }
   
   @AfterClass
   public void tearDown()
   {
      deleteCookies();
      try
      {
         VirtualFileSystemUtils.delete(URL + FOLDER_NAME);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (ModuleException e)
      {
         e.printStackTrace();
      }
   }
   
}
