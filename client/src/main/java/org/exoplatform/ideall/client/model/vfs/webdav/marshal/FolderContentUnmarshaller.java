/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ideall.client.model.vfs.webdav.marshal;

import java.util.ArrayList;

import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.gwtframework.commons.rest.Unmarshallable;
import org.exoplatform.gwtframework.commons.webdav.PropfindResponse;
import org.exoplatform.gwtframework.commons.webdav.PropfindResponse.Property;
import org.exoplatform.gwtframework.commons.webdav.PropfindResponse.Resource;
import org.exoplatform.gwtframework.commons.xml.QName;
import org.exoplatform.ideall.client.model.property.ItemProperty;
import org.exoplatform.ideall.client.model.util.ImageUtil;
import org.exoplatform.ideall.client.model.util.NodeTypeUtil;
import org.exoplatform.ideall.client.model.vfs.api.File;
import org.exoplatform.ideall.client.model.vfs.api.Folder;
import org.exoplatform.ideall.client.model.vfs.api.Item;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class FolderContentUnmarshaller implements Unmarshallable
{

   private Folder folder;

   public FolderContentUnmarshaller(Folder folder)
   {
      this.folder = folder;
   }

   public void unmarshal(String body) throws UnmarshallerException
   {
      try
      {
         parseFolderContent(body);
      }
      catch (Exception exc)
      {
         exc.printStackTrace();

         String message = "Can't parse folder content at <b>" + folder.getHref() + "</b>!";
         throw new UnmarshallerException(message);
      }

   }

   private void parseFolderContent(String body)
   {
      body = body.replace(" b:dt=\"dateTime.rfc1123\"", ""); // TODO to fix bug with the Internet Explorer XML Parser, when parsing node with property b:dt="dateTime.rfc1123" (http://markmail.org/message/ai2wypfkbhazhrdp)

      PropfindResponse response = PropfindResponse.parse(body);

      Resource resource = response.getResource();
      folder.setChildren(new ArrayList<Item>());

      if (resource == null)
      {
         return;
      }

      for (Resource child : resource.getChildren())
      {
         String href = child.getHref();

         Item item;
         if (child.isCollection())
         {
            item = new Folder(href);
         }
         else
         {
            item = new File(href);
         }

         item.getProperties().clear();
         item.getProperties().addAll(child.getProperties());

         if (item instanceof File)
         {
            String contentType = getProperty(item, ItemProperty.GETCONTENTTYPE).getValue();
            ((File)item).setContentType(contentType);
            String jcrNodeType = NodeTypeUtil.getContentNodeType(contentType);
            ((File)item).setJcrContentNodeType(jcrNodeType);
            String icon = ImageUtil.getIcon(contentType);
            item.setIcon(icon);
         }

         folder.getChildren().add(item);
      }
   }

   private Property getProperty(Item item, QName name)
   {
      for (Property property : item.getProperties())
      {
         if (property.getName().equals(name))
         {
            return property;
         }
      }

      return null;
   }

}
