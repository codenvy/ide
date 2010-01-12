/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ideall.client.model.configuration;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
*/
public class InvalidConfigurationRecievedEvent extends GwtEvent<InvalidConfigurationRecievedHandler>
{
   
   private String message;
   
   public static GwtEvent.Type<InvalidConfigurationRecievedHandler> TYPE = new Type<InvalidConfigurationRecievedHandler>();
   
   public InvalidConfigurationRecievedEvent(String message)
   {
      this.message = message;
   }
   
   @Override
   protected void dispatch(InvalidConfigurationRecievedHandler handler)
   {
      handler.onInvalidConfigurationReceived(this);
   }

   @Override
   public Type<InvalidConfigurationRecievedHandler> getAssociatedType()
   {
      return TYPE;
   }
   
   public String getMessage()
   {
      return message;
   }
}
