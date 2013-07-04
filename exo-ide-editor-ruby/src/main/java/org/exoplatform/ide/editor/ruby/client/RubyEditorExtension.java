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
package org.exoplatform.ide.editor.ruby.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesEvent;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler;
import org.exoplatform.ide.client.framework.control.GroupNames;
import org.exoplatform.ide.client.framework.control.NewItemControl;
import org.exoplatform.ide.client.framework.editor.AddCommentsModifierEvent;
import org.exoplatform.ide.client.framework.module.EditorCreator;
import org.exoplatform.ide.client.framework.module.Extension;
import org.exoplatform.ide.client.framework.module.FileType;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.editor.client.api.Editor;

/**
 * Provides a text editing area along with UI for executing text commands on the.<br>
 * Support syntax coloration for Ruby language (http://www.ruby-lang.org/en/)
 * 
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @author <a href="mailto:dnochevnov@exoplatform.com">Dmytro Nochevnov</a>
 * @version $Revision$
 */
public class RubyEditorExtension extends Extension implements InitializeServicesHandler {

    interface DefaultContent extends ClientBundle {
        @Source("hello.rb")
        TextResource getSource();
    }

    public final static DefaultContent DEFAULT_CONTENT = GWT.create(DefaultContent.class);

    /** @see org.exoplatform.ide.client.framework.module.Extension#initialize() */
    @Override
    public void initialize() {
        IDE.addHandler(InitializeServicesEvent.TYPE, this);

        IDE.getInstance().addControl(new NewItemControl("File/New/New Ruby File", "Ruby File", "Create Ruby File",
                                                        RubyClientBundle.INSTANCE.ruby(), RubyClientBundle.INSTANCE.rubyDisabled(),
                                                        MimeType.APPLICATION_RUBY).setGroupName(GroupNames.NEW_SCRIPT));

        RubyClientBundle.INSTANCE.css().ensureInjected();
    }

    @Override
    public void onInitializeServices(InitializeServicesEvent event) {
        IDE.getInstance().getFileTypeRegistry()
           .addFileType(
                        new FileType(MimeType.APPLICATION_RUBY, "rb", RubyClientBundle.INSTANCE.ruby()),
                        new EditorCreator() {
                            @Override
                            public Editor createEditor() {
                                return new RubyEditor(MimeType.APPLICATION_RUBY);
                                // return new CodeMirror(MimeType.APPLICATION_RUBY, new CodeMirrorConfiguration()
                                // .setGenericParsers("['parseruby.js', 'tokenizeruby.js']")
                                // .setGenericStyles("['" + CodeMirrorConfiguration.PATH + "css/rubycolors.css']")
                                // .setParser(new RubyParser())
                                // .setCanBeOutlined(true)
                                // .setAutocompleteHelper(new RubyAutocompleteHelper())
                                // .setCodeAssistant(new RubyCodeAssistant()));
                            }
                        });

        IDE.getInstance().getFileTypeRegistry()
           .addFileType(
                        new FileType("MimeType.APPLICATION_RUBY_HTML", "rb", RubyClientBundle.INSTANCE.ruby()),
                        new EditorCreator() {
                            @Override
                            public Editor createEditor() {
                                return new RubyEditor(MimeType.APPLICATION_RUBY_HTML);
                                // return new CodeMirror(MimeType.APPLICATION_RUBY_HTML, new CodeMirrorConfiguration()
                                // .setGenericParsers("['parseruby.js', 'tokenizeruby.js']")
                                // .setGenericStyles("['" + CodeMirrorConfiguration.PATH + "css/rubycolors.css']")
                                // .setParser(new RubyParser())
                                // .setCanBeOutlined(true)
                                // .setAutocompleteHelper(new RubyAutocompleteHelper())
                                // .setCodeAssistant(new RubyCodeAssistant()));
                            }
                        });

        // IDE.getInstance().addOutlineItemCreator(MimeType.APPLICATION_RUBY, new RubyOutlineItemCreator());
        // IDE.getInstance().addOutlineItemCreator(MimeType.APPLICATION_RUBY_HTML, new RubyOutlineItemCreator());

        IDE.fireEvent(new AddCommentsModifierEvent(MimeType.APPLICATION_RUBY, new RubyCommentModifier()));
    }

}