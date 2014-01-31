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
package com.codenvy.ide.ext.tutorials.client.template;

import com.codenvy.api.project.shared.dto.ProjectTypeDescriptor;
import com.codenvy.ide.api.resources.ManageProjectsClientService;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.wizard.template.AbstractTemplatePage;
import com.codenvy.ide.ext.tutorials.client.TutorialsClientService;
import com.codenvy.ide.resources.ProjectTypeDescriptorRegistry;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;

import static com.codenvy.ide.api.ui.wizard.newproject.NewProjectWizard.PROJECT;
import static com.codenvy.ide.api.ui.wizard.newproject.NewProjectWizard.PROJECT_NAME;
import static com.codenvy.ide.ext.tutorials.client.TutorialsExtension.PARTS_TUTORIAL_ID;
import static com.codenvy.ide.ext.tutorials.client.TutorialsExtension.TUTORIAL_PROJECT_TYPE_ID;

/**
 * The wizard page for creating parts tutorial template.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class CreatePartsTutorialPage extends AbstractTemplatePage {
    private ManageProjectsClientService   manageProjectsClientService;
    private ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry;
    private TutorialsClientService        unzipTemplateClientService;
    private ResourceProvider              resourceProvider;

    /**
     * Create page.
     *
     * @param service
     *         service that provides create this kind of project
     * @param resourceProvider
     */
    @Inject
    public CreatePartsTutorialPage(ManageProjectsClientService manageProjectsClientService,
                                   ProjectTypeDescriptorRegistry projectTypeDescriptorRegistry,
                                   TutorialsClientService unzipTemplateClientService,
                                   ResourceProvider resourceProvider) {
        super(null, null, PARTS_TUTORIAL_ID);
        this.manageProjectsClientService = manageProjectsClientService;
        this.projectTypeDescriptorRegistry = projectTypeDescriptorRegistry;
        this.unzipTemplateClientService = unzipTemplateClientService;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void commit(final CommitCallback callback) {
        final String projectName = wizardContext.getData(PROJECT_NAME);
        ProjectTypeDescriptor projectTypeDescriptor = projectTypeDescriptorRegistry.getDescriptor(TUTORIAL_PROJECT_TYPE_ID);
        try {
            manageProjectsClientService.createProject(projectName, projectTypeDescriptor,
                                                      Collections.<String, List<String>>emptyMap(), new AsyncRequestCallback<Void>() {
                @Override
                protected void onSuccess(Void result) {
                    unzipTemplate(projectName, callback);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }

    private void unzipTemplate(final String projectName, final CommitCallback callback) {
        try {
            unzipTemplateClientService.unzipPartsTutorial(projectName, new AsyncRequestCallback<Void>() {
                @Override
                protected void onSuccess(Void result) {
                    resourceProvider.getProject(projectName, new AsyncCallback<Project>() {
                        @Override
                        public void onSuccess(Project result) {
                            wizardContext.putData(PROJECT, result);
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }
                    });
                }

                @Override
                protected void onFailure(Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }
}