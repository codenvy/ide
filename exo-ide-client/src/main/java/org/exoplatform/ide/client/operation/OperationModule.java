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
package org.exoplatform.ide.client.operation;

import org.exoplatform.ide.client.documentation.DocumentationPresenter;
import org.exoplatform.ide.client.framework.control.event.RegisterControlEvent;
import org.exoplatform.ide.client.framework.control.event.RegisterControlEvent.DockTarget;
import org.exoplatform.ide.client.outline.OutlinePresenter;
import org.exoplatform.ide.client.outline.ShowOutlineControl;
import org.exoplatform.ide.client.output.OutputPresenter;
import org.exoplatform.ide.client.preview.PreviewHTMLControl;
import org.exoplatform.ide.client.preview.PreviewHTMLPresenter;
import org.exoplatform.ide.client.properties.PropertiesPresenter;
import org.exoplatform.ide.client.properties.ShowPropertiesControl;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class OperationModule
{

   private HandlerManager eventBus;

   public OperationModule(HandlerManager eventBus)
   {
      this.eventBus = eventBus;

      eventBus.fireEvent(new RegisterControlEvent(new ShowPropertiesControl(), DockTarget.TOOLBAR, true));
      new PropertiesPresenter(eventBus);

      eventBus.fireEvent(new RegisterControlEvent(new ShowOutlineControl(), DockTarget.TOOLBAR));
      new OutlinePresenter(eventBus);

      eventBus.fireEvent(new RegisterControlEvent(new PreviewHTMLControl(), DockTarget.TOOLBAR, true));
      new PreviewHTMLPresenter(eventBus);

      new DocumentationPresenter(eventBus);

      new OutputPresenter(eventBus);

   }

}
