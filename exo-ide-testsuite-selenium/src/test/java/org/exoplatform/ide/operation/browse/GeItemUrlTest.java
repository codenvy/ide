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
package org.exoplatform.ide.operation.browse;

import static org.junit.Assert.*;

import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.MenuCommands;
import org.junit.Test;

/**
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class GeItemUrlTest extends BaseTest
{
   private final String content = "Hello!!!";

   private final String file1Name = "gadget.xml";

   private final String file2Name = "Example.groovy";

   private final String folderName = "TestFolder";

   private final String entrypoint = "jcr/repository/";

   @Test
   public void testGetFileUrl() throws Exception
   {
      Thread.sleep(1000);
      //Create first file
      openNewFileFromToolbar(MenuCommands.New.GOOGLE_GADGET_FILE);
      Thread.sleep(2000);
      deleteLinesInEditor(7);
      assertEquals("", getTextFromCodeEditor(0));
      typeText(content);
      saveAsUsingToolbarButton(file1Name);
      Thread.sleep(1000);
      closeTab("0");
      assertElementPresentInWorkspaceTree(file1Name);

      createFolder(folderName);
      openNewFileFromToolbar(MenuCommands.New.GROOVY_SCRIPT_FILE);
      Thread.sleep(2000);
      assertEquals("", getTextFromCodeEditor(0));
      typeText(content);
      saveAsUsingToolbarButton(file2Name);
      Thread.sleep(1000);
      closeTab("0");
      assertElementPresentInWorkspaceTree(file2Name);

      //Refresh root item
      selectRootOfWorkspaceTree();
      clickOnToolbarButton("Refresh Selected Folder");

      String workspaceName = getItemNameFromWorkspaceTree(0);

      String url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/"));

      //Open url and check file and folder are present
      openLink(url);
      assertTrue(selenium.isElementPresent("link=" + file1Name));
      assertTrue(selenium.isElementPresent("link=" + folderName));
      selenium.goBack();
      selenium.waitForPageToLoad("12000");
      Thread.sleep(1000);

      //Check get URL for file in root of the tree
      selectItemInWorkspaceTree(file1Name);
      url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/" + file1Name));

      //Check get URL for folder in root of the tree
      selectItemInWorkspaceTree(folderName);
      url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/" + folderName + "/"));

      //Open url and check file and folder are present
      openLink(url);
      assertTrue(selenium.isElementPresent("link=" + file2Name));
      selenium.goBack();
      selenium.waitForPageToLoad("12000");
      Thread.sleep(1000);

      //Check get URL for the file in the folder
      openOrCloseFolder(folderName);
      Thread.sleep(1000);
      selectItemInWorkspaceTree(file2Name);
      url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/" + folderName + "/" + file2Name));

   }

   /**
    * @throws Exception
    */
   @Test
   public void testGetFileUrlWithSearch() throws Exception
   {
      selectRootOfWorkspaceTree();
      clickOnToolbarButton("Search...");
      Thread.sleep(1000);
      
      assertTrue(selenium.isElementPresent("scLocator=//Window[ID=\"ideSearchForm\"]"));
      assertTrue(selenium.isElementPresent("scLocator=//IButton[ID=\"ideSearchFormSearchButton\"]"));
      assertTrue(selenium.isElementPresent("scLocator=//IButton[ID=\"ideSearchFormCancelButton\"]"));
      //Check form inputs
      assertEquals("/", selenium.getValue("scLocator=//DynamicForm[ID=\"ideSearchFormDynamicForm\"]/item[name=ideSearchFormPathField]/element"));
      assertEquals("", selenium.getValue("scLocator=//DynamicForm[ID=\"ideSearchFormDynamicForm\"]/item[name=ideSearchFormContentField]/element"));
      assertEquals("", selenium.getValue("scLocator=//DynamicForm[ID=\"ideSearchFormDynamicForm\"]/item[name=ideSearchFormMimeTypeField]/element"));
      //Type content to input
      selenium.click("scLocator=//DynamicForm[ID=\"ideSearchFormDynamicForm\"]/item[name=ideSearchFormContentField]/element");
      selenium.type("scLocator=//DynamicForm[ID=\"ideSearchFormDynamicForm\"]/item[name=ideSearchFormContentField]/element", content);
      //Click "Search" button
      selenium.click("scLocator=//IButton[ID=\"ideSearchFormSearchButton\"]");
      Thread.sleep(1000);
      
      //Check files are found
      assertElementPresentSearchResultsTree(file1Name);
      assertElementPresentSearchResultsTree(file2Name);
      
      String workspaceName = getItemNameFromWorkspaceTree(0);
      
      //Check get URL for first file
      selectItemInSearchResultsTree(file1Name);
      String url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/" + file1Name));
      
      //Check get URL for second file
      selectItemInSearchResultsTree(file2Name);
      url = getItemUrl();
      assertTrue(url.startsWith(BASE_URL));
      assertTrue(url.endsWith(entrypoint + workspaceName + "/" + folderName + "/" + file2Name));
      
      selectWorkspaceTab();
      selectItemInWorkspaceTree(folderName);
      selenium.controlKeyDown();
      selectItemInWorkspaceTree(file1Name);
      selenium.controlKeyUp();
      deleteSelectedItem();
      Thread.sleep(5000);
   }

   /**
    * Open link.
    * 
    * @param link link to open
    * @throws Exception 
    */
   private void openLink(String link) throws Exception
   {
      selenium.open(link);
      selenium.waitForPageToLoad("6000");
      Thread.sleep(1000);
   }
}
