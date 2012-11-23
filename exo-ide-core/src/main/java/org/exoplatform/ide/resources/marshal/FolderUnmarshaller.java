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
package org.exoplatform.ide.resources.marshal;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;

import org.exoplatform.ide.commons.exception.UnmarshallerException;
import org.exoplatform.ide.resources.model.Folder;
import org.exoplatform.ide.rest.Unmarshallable;

/**
 * Unmarshaller for {@link Folder}
 * 
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public class FolderUnmarshaller implements Unmarshallable<Folder>
{

   private final Folder item;

   public FolderUnmarshaller(Folder item)
   {

      this.item = item;

   }

   /**
    * @see org.exoplatform.gwtframework.commons.rest.Unmarshallable#unmarshal(com.google.gwt.http.client.Response)
    */
   @Override
   public void unmarshal(Response response) throws UnmarshallerException
   {
      try
      {
         item.init(JSONParser.parseLenient(response.getText()).isObject());
      }
      catch (Exception exc)
      {
         String message = "Can't parse item " + response.getText();
         throw new UnmarshallerException(message, exc);
      }

   }

   @Override
   public Folder getPayload()
   {
      return this.item;
   }

}
