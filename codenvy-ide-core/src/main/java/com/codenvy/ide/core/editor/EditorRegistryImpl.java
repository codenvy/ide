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
package com.codenvy.ide.core.editor;

import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.editor.EditorRegistry;

import com.codenvy.ide.api.extension.SDK;

import com.codenvy.ide.api.resources.FileType;


import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.json.JsonIntegerMap;

import com.google.inject.Inject;
import com.google.inject.name.Named;


/**
 * Registry for holding {@link EditorProvider} for specific {@link FileType}.
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 *
 */
@SDK(title = "ide.api.editorRegistry")
public class EditorRegistryImpl implements EditorRegistry
{

   private JsonIntegerMap<EditorProvider> registry;

   @Inject
   public EditorRegistryImpl(@Named("defaulEditor") EditorProvider defaultProvider,
      @Named("defaultFileType") FileType defaultFile)
   {
      super();
      registry = JsonCollections.createIntegerMap();
      register(defaultFile, defaultProvider);
   }

    /**
    * {@inheritDoc}
    */
   @Override
   public void register(FileType fileType, EditorProvider provider)
   {
      registry.put(fileType.getId(), provider);
   }

    /**
    * {@inheritDoc}
    */
   @Override
   public EditorProvider getDefaultEditor(FileType fileType)
   {
      return registry.get(fileType.getId());
   }
}
