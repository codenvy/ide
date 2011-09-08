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
package org.exoplatform.ide.client.model.discovery.marshal;

import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.gwtframework.commons.rest.Unmarshallable;
import org.exoplatform.ide.client.framework.discovery.EntryPoint;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class EntryPointListUnmarshaller implements Unmarshallable
{
   
   private List<EntryPoint> entryPointList;
   
   public EntryPointListUnmarshaller(List<EntryPoint> entryPointList)
   {
      this.entryPointList = entryPointList;
   }

   public void unmarshal(Response response) throws UnmarshallerException
   {
      JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
      for (int i = 0; i < jsonArray.size(); i++)
      {
         JSONObject obj = jsonArray.get(i).isObject();
         EntryPoint entryPoint = new EntryPoint();
         if (obj.containsKey("href"))
           entryPoint.setHref(obj.get("href").isString().stringValue());    
         if (obj.containsKey("workspace"))
           entryPoint.setWorkspace(obj.get("workspace").isString().stringValue()); 
         
         entryPointList.add(entryPoint);
      }
   }
   
   public static native JavaScriptObject build(String json) /*-{
         return eval('(' + json + ')');      
      }-*/;

}
