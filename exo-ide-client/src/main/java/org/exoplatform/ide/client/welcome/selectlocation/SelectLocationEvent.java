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
package org.exoplatform.ide.client.welcome.selectlocation;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event, that calls <code>Select location</code> dialog window with navigation tree.
 * 
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: SelectLocationEvent.java Aug 31, 2011 12:03:17 PM vereshchaka $
 *
 */
public class SelectLocationEvent extends GwtEvent<SelectLocationHandler>
{
   public static final GwtEvent.Type<SelectLocationHandler> TYPE = new GwtEvent.Type<SelectLocationHandler>();
   
   public SelectLocationEvent()
   {
   }

   @Override
   protected void dispatch(SelectLocationHandler handler)
   {
      handler.onSelectLocation(this);
   }

   /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    */
   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<SelectLocationHandler> getAssociatedType()
   {
      return TYPE;
   }

}
