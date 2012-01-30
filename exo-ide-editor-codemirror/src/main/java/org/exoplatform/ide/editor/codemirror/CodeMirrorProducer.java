///*
// * Copyright (C) 2011 eXo Platform SAS.
// *
// * This is free software; you can redistribute it and/or modify it
// * under the terms of the GNU Lesser General Public License as
// * published by the Free Software Foundation; either version 2.1 of
// * the License, or (at your option) any later version.
// *
// * This software is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this software; if not, write to the Free
// * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
// * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
// */
//package org.exoplatform.ide.editor.codemirror;
//
//import com.google.gwt.event.shared.HandlerManager;
//import com.google.gwt.resources.client.ImageResource;
//
//import org.exoplatform.ide.editor.api.Editor;
//import org.exoplatform.ide.editor.api.EditorParameters;
//import org.exoplatform.ide.editor.api.EditorProducer;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
// * @version $Id: CodeMirrorProducer Feb 11, 2011 2:30:49 PM evgen $
// * 
// */
//public class CodeMirrorProducer extends EditorProducer
//{
//
//   private Map<String, Object> params = new HashMap<String, Object>();
//
//   /**
//    * @param mimeType
//    * @param description
//    * @param ext
//    * @param isDefault
//    */
//   public CodeMirrorProducer(String mimeType, String description, String ext, ImageResource icon, boolean isDefault)
//   {
//      super(mimeType, description, ext, icon, isDefault);
//      params.put(EditorParameters.MIME_TYPE, mimeType);
//   }
//
//   public CodeMirrorProducer(String mimeType, String description, String ext, ImageResource icon, boolean isDefault,
//      CodeMirrorConfiguration configuration)
//   {
//      this(mimeType, description, ext, icon, isDefault);
//      params.put(EditorParameters.CONFIGURATION, configuration);
//   }
//
//   /**
//    * @see org.exoplatform.ide.editor.api.EditorProducer#createEditor(org.exoplatform.ide.client.framework.vfs.File,
//    *      com.google.gwt.event.shared.HandlerManager, java.util.HashMap)
//    */
//   @Override
//   public Editor createEditor(String content, HandlerManager eventBus, HashMap<String, Object> params)
//   {
//      params.putAll(this.params);
//      return new CodeMirror(content, params, eventBus);
//   }
//
//}
