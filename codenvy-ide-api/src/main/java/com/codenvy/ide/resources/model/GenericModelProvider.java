/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
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
package com.codenvy.ide.resources.model;

import com.codenvy.ide.api.resources.ModelProvider;

import com.google.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;


/**
 * Model provider for generic Project 
 * 
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public class GenericModelProvider implements ModelProvider
{

   private final EventBus eventBus;

   /**
    * Creates GenericModel provider
    */
   @Inject
   public GenericModelProvider(EventBus eventBus)
   {
      this.eventBus = eventBus;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Project createProjectInstance()
   {
      return new Project(eventBus);
   }

}
