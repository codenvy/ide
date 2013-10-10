/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.java.client;

import com.codenvy.ide.api.editor.EditorRegistry;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.resources.FileType;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.wizard.WizardAgent;
import com.codenvy.ide.ext.java.client.codeassistant.ContentAssistHistory;
import com.codenvy.ide.ext.java.client.core.JavaCore;
import com.codenvy.ide.ext.java.client.editor.JavaEditorProvider;
import com.codenvy.ide.ext.java.client.internal.codeassist.impl.AssistOptions;
import com.codenvy.ide.ext.java.client.internal.compiler.impl.CompilerOptions;
import com.codenvy.ide.ext.java.client.projectmodel.JavaProject;
import com.codenvy.ide.ext.java.client.projectmodel.JavaProjectModelProvider;
import com.codenvy.ide.ext.java.client.templates.*;
import com.codenvy.ide.ext.java.client.wizard.NewJavaClassPagePresenter;
import com.codenvy.ide.ext.java.client.wizard.NewPackagePagePresenter;
import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.resources.ProjectTypeAgent;
import com.codenvy.ide.rest.MimeType;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import java.util.HashMap;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 */
@Extension(title = "Java Support : syntax highlighting and autocomplete.", version = "3.0.0")
public class JavaExtension {
    private static final String JAVA_PERSPECTIVE                  = "Java";
    public static final  String JAVA_APPLICATION_PROJECT_TYPE     = "Jar";
    public static final  String JAVA_WEB_APPLICATION_PROJECT_TYPE = "War";

    private static JavaExtension instance;

    private HashMap<String, String> options;

    private ContextTypeRegistry codeTemplateContextTypeRegistry;

    private TemplateStore templateStore;

    private ContentAssistHistory contentAssistHistory;

    /**
     *
     */
    @Inject
    public JavaExtension(ResourceProvider resourceProvider, EditorRegistry editorRegistry, JavaEditorProvider javaEditorProvider,
                         EventBus eventBus, WizardAgent wizardAgent, Provider<NewPackagePagePresenter> packageProvider,
                         Provider<NewJavaClassPagePresenter> classProvider, ProjectTypeAgent projectTypeAgent) {

        this();
        FileType javaFile = new FileType(JavaClientBundle.INSTANCE.java(), MimeType.APPLICATION_JAVA, "java");
        editorRegistry.register(javaFile, javaEditorProvider);
        resourceProvider.registerFileType(javaFile);
        resourceProvider.registerModelProvider(JavaProject.PRIMARY_NATURE, new JavaProjectModelProvider(eventBus));
        JavaClientBundle.INSTANCE.css().ensureInjected();

        projectTypeAgent.register(JavaProject.PRIMARY_NATURE, "Java application", JavaClientBundle.INSTANCE.newJavaProject(),
                                  JavaProject.PRIMARY_NATURE, JsonCollections.<String>createArray(JAVA_APPLICATION_PROJECT_TYPE));
        projectTypeAgent.register(JAVA_WEB_APPLICATION_PROJECT_TYPE, "Java web application",
                                  JavaClientBundle.INSTANCE.newJavaProject(),
                                  JavaProject.PRIMARY_NATURE,
                                  JsonCollections.<String>createArray(JAVA_WEB_APPLICATION_PROJECT_TYPE));

        wizardAgent.registerNewResourceWizard(JAVA_PERSPECTIVE, "Package", JavaClientBundle.INSTANCE.packageItem(), packageProvider);
        wizardAgent.registerNewResourceWizard(JAVA_PERSPECTIVE, "Java Class", JavaClientBundle.INSTANCE.newClassWizz(), classProvider);
    }

    /** For test use only. */
    public JavaExtension() {
        options = new HashMap<String, String>();
        instance = this;
        initOptions();
    }

    /** @return  */
    public static JavaExtension get() {
        return instance;
    }

    private void initOptions() {
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
        options.put(JavaCore.CORE_ENCODING, "UTF-8");
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
        options.put(CompilerOptions.OPTION_TargetPlatform, JavaCore.VERSION_1_6);
        options.put(AssistOptions.OPTION_PerformVisibilityCheck, AssistOptions.ENABLED);
        options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING);
        options.put(CompilerOptions.OPTION_TaskTags, CompilerOptions.WARNING);
        options.put(CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.WARNING);
        options.put(CompilerOptions.OPTION_SuppressWarnings, CompilerOptions.DISABLED);
        options.put(JavaCore.COMPILER_TASK_TAGS, "TODO,FIXME,XXX");
        options.put(JavaCore.COMPILER_PB_UNUSED_PARAMETER_INCLUDE_DOC_COMMENT_REFERENCE, JavaCore.ENABLED);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        options.put(CompilerOptions.OPTION_Process_Annotations, JavaCore.DISABLED);

    }

    /** @return  */
    public HashMap<String, String> getOptions() {
        return options;
    }

    /** @return  */
    public TemplateStore getTemplateStore() {
        if (templateStore == null) {
            templateStore = new TemplateStore();
        }
        return templateStore;
    }

    /** @return  */
    public ContextTypeRegistry getTemplateContextRegistry() {
        if (codeTemplateContextTypeRegistry == null) {
            codeTemplateContextTypeRegistry = new ContextTypeRegistry();

            CodeTemplateContextType.registerContextTypes(codeTemplateContextTypeRegistry);
            JavaContextType contextTypeAll = new JavaContextType(JavaContextType.ID_ALL);

            contextTypeAll.initializeContextTypeResolvers();

            FieldResolver fieldResolver = new FieldResolver();
            fieldResolver.setType("field");
            contextTypeAll.addResolver(fieldResolver);

            LocalVarResolver localVarResolver = new LocalVarResolver();
            localVarResolver.setType("localVar");
            contextTypeAll.addResolver(localVarResolver);
            VarResolver varResolver = new VarResolver();
            varResolver.setType("var");
            contextTypeAll.addResolver(varResolver);
            NameResolver nameResolver = new NameResolver();
            nameResolver.setType("newName");
            contextTypeAll.addResolver(nameResolver);
            TypeResolver typeResolver = new TypeResolver();
            typeResolver.setType("newType");
            contextTypeAll.addResolver(typeResolver);
            ElementTypeResolver elementTypeResolver = new ElementTypeResolver();
            elementTypeResolver.setType("elemType");
            contextTypeAll.addResolver(elementTypeResolver);
            TypeVariableResolver typeVariableResolver = new TypeVariableResolver();
            typeVariableResolver.setType("argType");
            contextTypeAll.addResolver(typeVariableResolver);
            LinkResolver linkResolver = new LinkResolver();
            linkResolver.setType("link");
            contextTypeAll.addResolver(linkResolver);
            ImportsResolver importsResolver = new ImportsResolver();
            importsResolver.setType("import");
            StaticImportResolver staticImportResolver = new StaticImportResolver();
            staticImportResolver.setType("importStatic");
            contextTypeAll.addResolver(staticImportResolver);
            ExceptionVariableNameResolver exceptionVariableNameResolver = new ExceptionVariableNameResolver();
            exceptionVariableNameResolver.setType("exception_variable_name");
            contextTypeAll.addResolver(exceptionVariableNameResolver);
            codeTemplateContextTypeRegistry.addContextType(contextTypeAll);
            codeTemplateContextTypeRegistry.addContextType(new JavaDocContextType());
            JavaContextType contextTypeMembers = new JavaContextType(JavaContextType.ID_MEMBERS);
            JavaContextType contextTypeStatements = new JavaContextType(JavaContextType.ID_STATEMENTS);
            contextTypeMembers.initializeResolvers(contextTypeAll);
            contextTypeStatements.initializeResolvers(contextTypeAll);
            codeTemplateContextTypeRegistry.addContextType(contextTypeMembers);
            codeTemplateContextTypeRegistry.addContextType(contextTypeStatements);
        }

        return codeTemplateContextTypeRegistry;
    }

    /** @return  */
    public ContentAssistHistory getContentAssistHistory() {
        if (contentAssistHistory == null) {
            Preferences preferences = GWT.create(Preferences.class);
            contentAssistHistory =
                    //TODO get user name
                    ContentAssistHistory.load(preferences, Preferences.CODEASSIST_LRU_HISTORY + "todo");

            if (contentAssistHistory == null) {
                contentAssistHistory = new ContentAssistHistory();
            }
        }

        return contentAssistHistory;
    }
}