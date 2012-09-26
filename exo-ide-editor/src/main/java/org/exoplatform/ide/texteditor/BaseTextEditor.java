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
package org.exoplatform.ide.texteditor;


import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.exoplatform.ide.AppContext;
import org.exoplatform.ide.editor.api.DocumentProvider;
import org.exoplatform.ide.editor.api.EditorInitException;
import org.exoplatform.ide.editor.api.EditorInput;
import org.exoplatform.ide.editor.api.SelectionProvider;
import org.exoplatform.ide.editor.api.TextEditorPartPresenter;
import org.exoplatform.ide.text.DocumentImpl;
import org.exoplatform.ide.texteditor.api.TextEditorPartDisplay;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 *
 */
public class BaseTextEditor implements TextEditorPartPresenter, IsWidget
{

   private TextEditorPartDisplay editor;

   /**
    * 
    */
   @Inject
   public BaseTextEditor(AppContext appContext)
   {
      editor = Editor.create(appContext);
      
      
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#init(org.exoplatform.ide.editor.api.EditorSite, org.exoplatform.ide.editor.api.EditorInput)
    */
   @Override
   public void init(EditorInput input) throws EditorInitException
   {
      DocumentImpl d = new DocumentImpl(input.getName());
      editor.setDocument(d);
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#doSave()
    */
   @Override
   public void doSave()
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#doSaveAs()
    */
   @Override
   public void doSaveAs()
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#isDirty()
    */
   @Override
   public boolean isDirty()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#isSaveAsAllowed()
    */
   @Override
   public boolean isSaveAsAllowed()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * @see org.exoplatform.ide.editor.api.EditorPartPresenter#isSaveOnCloseNeeded()
    */
   @Override
   public boolean isSaveOnCloseNeeded()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#getDocumentProvider()
    */
   @Override
   public DocumentProvider getDocumentProvider()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#close(boolean)
    */
   @Override
   public void close(boolean save)
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#isEditable()
    */
   @Override
   public boolean isEditable()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#doRevertToSaved()
    */
   @Override
   public void doRevertToSaved()
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#getSelectionProvider()
    */
   @Override
   public SelectionProvider getSelectionProvider()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @see org.exoplatform.ide.editor.api.TextEditorPartPresenter#selectAndReveal(int, int)
    */
   @Override
   public void selectAndReveal(int offset, int length)
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
    */
   @Override
   public Widget asWidget()
   {
      HTML h = new HTML();
      h.getElement().appendChild((Node)editor.getElement());
      return h;
   }

}
