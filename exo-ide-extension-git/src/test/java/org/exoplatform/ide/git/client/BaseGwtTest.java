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
package org.exoplatform.ide.git.client;

import com.google.gwt.core.client.JavaScriptObject;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:  Apr 27, 2011 11:42:28 AM anya $
 *
 */
public class BaseGwtTest extends GWTTestCase
{

   /**
    * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
    */
   @Override
   public String getModuleName()
   {
      return "org.exoplatform.ide.git.GitTest";
   }

   /**
   * Build {@link JavaScriptObject} from string.
   * 
   * @param json string that contains object
   * @return {@link JavaScriptObject}
   */
   protected static native JavaScriptObject build(String json) /*-{
                                                               try {
                                                               var object = eval('(' + json + ')');
                                                               return object;
                                                               } catch (e) {
                                                               return null;
                                                               }
                                                               }-*/;
}
