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

import org.exoplatform.ideall.client.model.template.Template;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class TemplateCreatedEvent extends GwtEvent<TemplateCreatedHandler>
{

   public static final GwtEvent.Type<TemplateCreatedHandler> TYPE = new GwtEvent.Type<TemplateCreatedHandler>();

   private Template template;

   public TemplateCreatedEvent(Template template)
   {
      this.template = template;
   }

   @Override
   protected void dispatch(TemplateCreatedHandler handler)
   {
      handler.onTemplateCreated(this);
   }

   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<TemplateCreatedHandler> getAssociatedType()
   {
      return TYPE;
   }

   /**
    * @return the template
    */
   public Template getTemplate()
   {
      return template;
   }

}
