/**
 * Copyright (C) 2009 eXo Platform SAS.
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
 *
 */
package org.exoplatform.ideall.client.model.settings;

import org.exoplatform.gwt.commons.initializer.RegistryConstants;
import org.exoplatform.gwt.commons.rest.AsyncRequest;
import org.exoplatform.gwt.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwt.commons.rest.HTTPHeader;
import org.exoplatform.gwt.commons.rest.MimeType;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.configuration.Configuration;
import org.exoplatform.ideall.client.model.settings.event.ApplicationContextReceivedEvent;
import org.exoplatform.ideall.client.model.settings.event.ApplicationContextSavedEvent;
import org.exoplatform.ideall.client.model.settings.marshal.ApplicationContextMarshaller;
import org.exoplatform.ideall.client.model.settings.marshal.ApplicationContextUnmarshaller;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Random;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class SettingsServiceImpl extends SettingsService
{

   private static final String CONTEXT = "/settings";

   private HandlerManager eventBus;

   public SettingsServiceImpl(HandlerManager eventBus)
   {
      this.eventBus = eventBus;
   }

   private String getURL(ApplicationContext context)
   {
      String url =
         Configuration.getRegistryURL() + "/" + RegistryConstants.EXO_USERS + "/" + context.getUserInfo().getName()
            + "/" + Configuration.APPLICATION;
      return url;
   }

   @Override
   public void getSettings(ApplicationContext context)
   {
      String url = getURL(context) + "/?nocache=" + Random.nextInt();

      ApplicationContextReceivedEvent event = new ApplicationContextReceivedEvent(context);
      ApplicationContextUnmarshaller unmarshaller = new ApplicationContextUnmarshaller(context);

      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, unmarshaller, event, event);
      AsyncRequest.build(RequestBuilder.GET, url).send(callback);
   }

   @Override
   public void saveSetting(ApplicationContext context)
   {
      String url = getURL(context) + "/?createIfNotExist=true";

      ApplicationContextMarshaller marshaller = new ApplicationContextMarshaller(context);
      ApplicationContextSavedEvent event = new ApplicationContextSavedEvent(context);

      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, event);
      AsyncRequest.build(RequestBuilder.POST, url).header(HTTPHeader.X_HTTP_METHOD_OVERRIDE, "PUT").header(
         HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_XML).data(marshaller).send(callback);
   }

}
