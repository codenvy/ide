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
package org.exoplatform.ideall.client.model.template.event;

import org.exoplatform.gwt.commons.exceptions.ServerExceptionEvent;
import org.exoplatform.ideall.client.model.template.TemplateList;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class TemplateListReceivedEvent extends ServerExceptionEvent<TemplateListReceivedHandler>
{

   public static final GwtEvent.Type<TemplateListReceivedHandler> TYPE =
      new GwtEvent.Type<TemplateListReceivedHandler>();

   private TemplateList templateList;

   private Throwable exception;

   public TemplateListReceivedEvent(TemplateList templateList)
   {
      this.templateList = templateList;
   }

   @Override
   protected void dispatch(TemplateListReceivedHandler handler)
   {
      handler.onTemplateListReceived(this);
   }

   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<TemplateListReceivedHandler> getAssociatedType()
   {
      return TYPE;
   }

   /**
    * @return the templateList
    */
   public TemplateList getTemplateList()
   {
      return templateList;
   }

   @Override
   public void setException(Throwable exception)
   {
      this.exception = exception;
   }

   public Throwable getException()
   {
      return exception;
   }

}
