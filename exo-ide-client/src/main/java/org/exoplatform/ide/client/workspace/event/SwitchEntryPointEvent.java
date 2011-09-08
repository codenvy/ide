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
package org.exoplatform.ide.client.workspace.event;

import org.exoplatform.ide.client.framework.discovery.EntryPoint;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class SwitchEntryPointEvent extends GwtEvent<SwitchEntryPointHandler>
{

   public static final GwtEvent.Type<SwitchEntryPointHandler> TYPE = new GwtEvent.Type<SwitchEntryPointHandler>();

   private EntryPoint entryPoint;

   public SwitchEntryPointEvent(EntryPoint entryPoint)
   {
      this.entryPoint = entryPoint;
   }

   public EntryPoint getEntryPoint()
   {
      return entryPoint;
   }

   @Override
   protected void dispatch(SwitchEntryPointHandler handler)
   {
      handler.onSwitchEntryPoint(this);
   }

   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<SwitchEntryPointHandler> getAssociatedType()
   {
      return TYPE;
   }

}
