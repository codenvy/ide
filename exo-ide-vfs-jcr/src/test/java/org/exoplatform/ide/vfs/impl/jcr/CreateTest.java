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
package org.exoplatform.ide.vfs.impl.jcr;

import org.everrest.core.impl.ContainerResponse;
import org.everrest.core.tools.ByteArrayContainerResponseWriter;
import org.exoplatform.services.jcr.access.PermissionType;
import org.exoplatform.services.jcr.core.ExtendedNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: CreateTest.java 75317 2011-10-19 15:02:05Z andrew00x $
 */
public class CreateTest extends JcrFileSystemTest
{
   private String createTestNodeID;
   private String createTestNodePath;
   private Node createTestNode;

   /**
    * @see org.exoplatform.ide.vfs.impl.jcr.JcrFileSystemTest#setUp()
    */
   @Override
   public void setUp() throws Exception
   {
      super.setUp();
      String name = getClass().getName();
      createTestNode = testRoot.addNode(name, "nt:unstructured");
      session.save();
      createTestNodeID = ((ExtendedNode)createTestNode).getIdentifier();
      createTestNodePath = createTestNode.getPath();
   }
   
   public void testCreateFile() throws Exception
   {
      String name = "testCreateFile";
      String content = "test create file";
      String path = SERVICE_URI + "file/" + createTestNodeID + '?' + "name=" + name;
      Map <String, List <String>> headers = new HashMap <String, List <String>> ();
      List <String> contentType = new ArrayList<String>();
      contentType.add("text/plain;charset=utf8");
      headers.put("Content-Type", contentType);
      
      ContainerResponse response = launcher.service("POST", path, BASE_URI, headers, content.getBytes(), null);
      assertEquals(200, response.getStatus());
      String expectedPath = createTestNodePath + '/' + name;
      assertTrue("File was not created in expected location. ", session.itemExists(expectedPath));
      Node file = (Node)session.getItem(expectedPath);
      assertEquals("text/plain", file.getNode("jcr:content").getProperty("jcr:mimeType").getString());
      assertEquals("utf8", file.getNode("jcr:content").getProperty("jcr:encoding").getString());
      assertEquals(content, file.getNode("jcr:content").getProperty("jcr:data").getString());
   }

   public void testCreateFileInRoot() throws Exception
   {
      String name = "testCreateFileInRoot";
      String content = "test create file";
      String path = SERVICE_URI + "file/" + ((ExtendedNode)session.getRootNode()).getIdentifier() + '?' + "name=" + name;
      Map <String, List <String>> headers = new HashMap <String, List <String>> ();
      List <String> contentType = new ArrayList<String>();
      contentType.add("text/plain;charset=utf8");
      headers.put("Content-Type", contentType);
      
      ContainerResponse response = launcher.service("POST", path, BASE_URI, headers, content.getBytes(), null);
      assertEquals(200, response.getStatus());
      String expectedPath = '/' + name;
      assertTrue("File was not created in expected location. ", session.itemExists(expectedPath));
      Node file = (Node)session.getItem(expectedPath);
      assertEquals("text/plain", file.getNode("jcr:content").getProperty("jcr:mimeType").getString());
      assertEquals("utf8", file.getNode("jcr:content").getProperty("jcr:encoding").getString());
      assertEquals(content, file.getNode("jcr:content").getProperty("jcr:data").getString());
   }

   public void testCreateFileNoContent() throws Exception
   {
      String name = "testCreateFileNoContent";
      String path = SERVICE_URI + "file/" + createTestNodeID + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);

      assertEquals(200, response.getStatus());
      String expectedPath = createTestNodePath + '/' + name;
      assertTrue("File was not created in expected location. ", session.itemExists(expectedPath));
      Node file = (Node)session.getItem(expectedPath);
      assertEquals(MediaType.APPLICATION_OCTET_STREAM, file.getNode("jcr:content").getProperty("jcr:mimeType")
         .getString());
      assertFalse(file.getNode("jcr:content").hasProperty("jcr:encoding"));
      assertEquals("", file.getNode("jcr:content").getProperty("jcr:data").getString());
   }

   public void testCreateFileNoMediaType() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String name = "testCreateFileNoMediaType";
      String content = "test create file without media type";
      String path = SERVICE_URI + "file/" + createTestNodeID + '?' + "name=" + name;
      
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, content.getBytes(), writer, null);
      assertEquals(200, response.getStatus());
      String expectedPath = createTestNodePath + '/' + name;
      assertTrue("File was not created in expected location. ", session.itemExists(expectedPath));
      Node file = (Node)session.getItem(expectedPath);
      assertEquals(MediaType.APPLICATION_OCTET_STREAM, file.getNode("jcr:content").getProperty("jcr:mimeType")
         .getString());
      assertFalse(file.getNode("jcr:content").hasProperty("jcr:encoding"));
      assertEquals(content, file.getNode("jcr:content").getProperty("jcr:data").getString());
   }

   public void testCreateFileNoName() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = SERVICE_URI + "file/" + createTestNodeID;
      ContainerResponse response =
         launcher.service("POST", path, BASE_URI, null, DEFAULT_CONTENT.getBytes(), writer, null);
      assertEquals(400, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFileNoPermissions() throws Exception
   {
      Node parent = createTestNode.addNode("testCreateFileNoPermissions_PARENT", "nt:folder");
      parent.addMixin("exo:privilegeable");
      Map<String, String[]> permissions = new HashMap<String, String[]>(1);
      permissions.put("root", PermissionType.ALL);
      ((ExtendedNode)parent).setPermissions(permissions);
      session.save();
      String parentID = ((ExtendedNode)parent).getIdentifier();

      String name = "testCreateFileNoPermissions";
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = SERVICE_URI + "file/" + parentID + '?' + "name=" + name;
      ContainerResponse response =
         launcher.service("POST", path, BASE_URI, null, DEFAULT_CONTENT.getBytes(), writer, null);
      assertEquals(403, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFileWrongParent() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String name = "testCreateFileWrongParent";
      String path = SERVICE_URI + "file/" + createTestNodeID + "_WRONG_ID" + '?' + "name=" + name;
      ContainerResponse response =
         launcher.service("POST", path, BASE_URI, null, DEFAULT_CONTENT.getBytes(), writer, null);
      assertEquals(404, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFolder() throws Exception
   {
      String name = "testCreateFolder";
      String path = SERVICE_URI + "folder/" + createTestNodeID + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
      assertEquals(200, response.getStatus());
      String expectedPath = createTestNodePath + '/' + name;
      assertTrue("Folder was not created in expected location. ", session.itemExists(expectedPath));
      Node folder = (Node)session.getItem(expectedPath);
      assertTrue("nt:folder node type expected", folder.getPrimaryNodeType().isNodeType("nt:folder"));
   }

   public void testCreateFolderInRoot() throws Exception
   {
      String name = "testCreateFolderInRoot";
      String path = SERVICE_URI + "folder/" + ((ExtendedNode)session.getRootNode()).getIdentifier() + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
      assertEquals(200, response.getStatus());
      String expectedPath = '/' + name;
      assertTrue("Folder was not created in expected location. ", session.itemExists(expectedPath));
      Node folder = (Node)session.getItem(expectedPath);
      assertTrue("nt:folder node type expected", folder.getPrimaryNodeType().isNodeType("nt:folder"));
   }

   public void testCreateFolderNoName() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = SERVICE_URI + "folder/" + createTestNodeID;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
      assertEquals(400, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFolderNoPermissions() throws Exception
   {
      Node parent = createTestNode.addNode("testCreateFolderNoPermissions_PARENT", "nt:folder");
      parent.addMixin("exo:privilegeable");
      Map<String, String[]> permissions = new HashMap<String, String[]>(1);
      permissions.put("root", PermissionType.ALL);
      ((ExtendedNode)parent).setPermissions(permissions);
      session.save();
      String parentID = ((ExtendedNode)parent).getIdentifier();

      String name = "testCreateFolderNoPermissions";
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = SERVICE_URI + "folder/" + parentID + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
      assertEquals(403, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFolderWrongParent() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String name = "testCreateFolderWrongParent";
      String path = SERVICE_URI + "folder/" + createTestNodeID + "_WRONG_ID" + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
      assertEquals(404, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testCreateFolderHierarchy() throws Exception
   {
      String name = "testCreateFolderHierarchy/1/2/3/4/5";
      String path = SERVICE_URI + "folder/" + createTestNodeID + '?' + "name=" + name;
      ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null, null);
      assertEquals(200, response.getStatus());
      String expectedPath = createTestNodePath + '/' + name;
      assertTrue("Folder was not created in expected location. ", session.itemExists(expectedPath));
      Node folder = (Node)session.getItem(expectedPath);
      assertTrue("nt:folder node type expected", folder.getPrimaryNodeType().isNodeType("nt:folder"));
   }
}
