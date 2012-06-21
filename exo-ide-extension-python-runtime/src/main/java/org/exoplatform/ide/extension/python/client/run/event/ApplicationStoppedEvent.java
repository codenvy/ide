/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.ide.extension.python.client.run.event;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.ide.extension.python.shared.ApplicationInstance;

/**
 * Event occurs, when Python application has been stopped.
 * 
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: Jun 21, 2012 10:16:34 AM anya $
 * 
 */
public class ApplicationStoppedEvent extends GwtEvent<ApplicationStoppedHandler>
{
   /**
    * Type used to register the event.
    */
   public static final GwtEvent.Type<ApplicationStoppedHandler> TYPE = new GwtEvent.Type<ApplicationStoppedHandler>();

   /**
    * Stopped application.
    */
   private ApplicationInstance application;

   /**
    * If <code>true</code>, then application was stopped manually.
    */
   private boolean manually;

   public ApplicationStoppedEvent(ApplicationInstance application, boolean manually)
   {
      this.application = application;
      this.manually = manually;
   }

   /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    */
   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<ApplicationStoppedHandler> getAssociatedType()
   {
      return TYPE;
   }

   /**
    * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    */
   @Override
   protected void dispatch(ApplicationStoppedHandler handler)
   {
      handler.onApplicationStopped(this);
   }

   /**
    * @return the application
    */
   public ApplicationInstance getApplication()
   {
      return application;
   }

   /**
    * @return the manually
    */
   public boolean isManually()
   {
      return manually;
   }
}
