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
package com.codenvy.ide.wizard.template;

import com.codenvy.ide.api.template.CreateProjectProvider;
import com.codenvy.ide.api.template.Template;
import com.codenvy.ide.api.template.TemplateAgent;
import com.codenvy.ide.api.ui.wizard.WizardPagePresenter;
import com.codenvy.ide.json.JsonArray;
import com.codenvy.ide.json.JsonCollections;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * The implementation of {@link TemplateAgent}.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@Singleton
public class TemplateAgentImpl implements TemplateAgent {
    private final JsonArray<Template> templates;
    private       Template            selectedTemplate;

    /** Create agent. */
    @Inject
    protected TemplateAgentImpl() {
        this.templates = JsonCollections.createArray();
    }

    /** {@inheritDoc} */
    @Override
    public void registerTemplate(String title, ImageResource icon, JsonArray<String> projectTypes,
                                 CreateProjectProvider createProjectProvider, Provider<? extends WizardPagePresenter> wizardPage) {
        Template template = new Template(icon, title, createProjectProvider, wizardPage, projectTypes);
        templates.add(template);
    }

    /** {@inheritDoc} */
    @Override
    public Template getSelectedTemplate() {
        return selectedTemplate;
    }

    /**
     * Sets selected template for creating project.
     *
     * @param template
     */
    public void setSelectedTemplate(Template template) {
        selectedTemplate = template;
    }

    /**
     * Returns all available templates for creating project.
     *
     * @param projectType
     * @return
     */
    public JsonArray<Template> getTemplatesForProjectType(String projectType) {
        JsonArray<Template> templates = JsonCollections.createArray();

        for (int i = 0; i < this.templates.size(); i++) {
            Template template = this.templates.get(i);
            if (template.getProjectTypes().contains(projectType)) {
                templates.add(template);
            }
        }

        return templates;
    }
}