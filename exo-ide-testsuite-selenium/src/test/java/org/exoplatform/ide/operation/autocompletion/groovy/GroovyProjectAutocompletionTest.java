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
package org.exoplatform.ide.operation.autocompletion.groovy;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.project.classpath.UseOfClasspathEntriesTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: GroovyClassNameProjectTest Jan 20, 2011 2:13:30 PM evgen $
 *
 */
public class GroovyProjectAutocompletionTest extends BaseTest
{

   private static final String FOLDER_NAME = UseOfClasspathEntriesTest.class.getSimpleName() + "-test";

   private static final String PROJECT_NAME = UseOfClasspathEntriesTest.class.getSimpleName() + "-project";

   private static final String EMPLOYEE_FILE_NAME = "Employee.groovy";

   private static final String POJO_FILE_NAME = "Pojo.groovy";

   private static final String REST_SERVICE_FILE_NAME = "Sample.grs";

   private static final String CLASSPATH_FILE_CONTENT = "{\"entries\":[{\"kind\":\"dir\", \"path\":\""
      + BaseTest.WS_NAME + "#/" + FOLDER_NAME + "/\"}]}";;

   private static final String CLASSPATH_FILE_NAME = ".groovyclasspath";

   @BeforeClass
   public static void setUp()
   {
      final String filePath = "src/test/resources/org/exoplatform/ide/project/classpath/";
      try
      {
         VirtualFileSystemUtils.mkcol(WORKSPACE_URL + FOLDER_NAME);
         //create structure of folder for package org/exoplatform/sample, 
         //where will be placed Employee.groovy file
         VirtualFileSystemUtils.mkcol(WORKSPACE_URL + FOLDER_NAME + "/org");
         VirtualFileSystemUtils.mkcol(WORKSPACE_URL + FOLDER_NAME + "/org/exoplatform");
         VirtualFileSystemUtils.mkcol(WORKSPACE_URL + FOLDER_NAME + "/org/exoplatform/sample");
         //put Employee.groovy file
         VirtualFileSystemUtils.put(filePath + "employee.groovy", MimeType.APPLICATION_GROOVY, WORKSPACE_URL
            + FOLDER_NAME + "/org/exoplatform/sample/" + EMPLOYEE_FILE_NAME);
         //put Pojo.groovy
         VirtualFileSystemUtils.put(filePath + POJO_FILE_NAME, MimeType.APPLICATION_GROOVY, WORKSPACE_URL + FOLDER_NAME
            + "/org/exoplatform/sample/" + POJO_FILE_NAME);

         VirtualFileSystemUtils.mkcol(WORKSPACE_URL + PROJECT_NAME);
         //put rest service file
         VirtualFileSystemUtils.put(filePath + "rest-service.grs", MimeType.GROOVY_SERVICE, WORKSPACE_URL
            + PROJECT_NAME + "/" + REST_SERVICE_FILE_NAME);
         //put classpath file
         VirtualFileSystemUtils.put(CLASSPATH_FILE_CONTENT.getBytes(), MimeType.APPLICATION_JSON, WORKSPACE_URL
            + PROJECT_NAME + "/" + CLASSPATH_FILE_NAME);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail("Can't create project structure");
      }
   }

   @Test
   public void testGroovyClassNameProject() throws Exception
   {
      IDE.WORKSPACE.waitForRootItem();

      /*
       * 1. Check, that project folder and folder with resources are present.
       * Open REST Service. 
       */
      IDE.NAVIGATION.assertItemVisible(WS_URL + PROJECT_NAME + "/");
      IDE.NAVIGATION.assertItemVisible(WS_URL + FOLDER_NAME + "/");

      IDE.WORKSPACE.doubleClickOnFolder(WS_URL + PROJECT_NAME + "/");

      IDE.NAVIGATION.openFileFromNavigationTreeWithCodeEditor(WORKSPACE_URL + PROJECT_NAME + "/"
         + REST_SERVICE_FILE_NAME, false);

      IDE.CODEASSISTANT.moveCursorDown(12);

      IDE.EDITOR.typeTextIntoEditor(0, "Po");

      IDE.CODEASSISTANT.openForm();

      IDE.CODEASSISTANT.checkElementPresent("Pojo");

      selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_ENTER);
      Thread.sleep(TestConstants.SLEEP_SHORT);

      assertTrue(IDE.EDITOR.getTextFromCodeEditor(0).contains("import org.exoplatform.sample.Pojo"));

      IDE.EDITOR.typeTextIntoEditor(0, " p");

      selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_ENTER);
      Thread.sleep(TestConstants.SLEEP_SHORT);

      IDE.EDITOR.typeTextIntoEditor(0, "p.");

      IDE.CODEASSISTANT.openForm();

      IDE.CODEASSISTANT.checkElementPresent("getName():String");
      IDE.CODEASSISTANT.checkElementPresent("printText(String):void");
      IDE.CODEASSISTANT.checkElementPresent("setName(String):void");

      IDE.CODEASSISTANT.typeToInput("pr");

      selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_ENTER);
      Thread.sleep(TestConstants.SLEEP_SHORT);

      assertTrue(IDE.EDITOR.getTextFromCodeEditor(0).contains("p.printText(String)"));

   }

   @AfterClass
   public static void tearDown()
   {
      try
      {
         VirtualFileSystemUtils.delete(WORKSPACE_URL + FOLDER_NAME);
         VirtualFileSystemUtils.delete(WORKSPACE_URL + PROJECT_NAME);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
