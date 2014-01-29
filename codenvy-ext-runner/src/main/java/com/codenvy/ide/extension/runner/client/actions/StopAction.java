/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
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
package com.codenvy.ide.extension.runner.client.actions;

import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.collections.StringSet;
import com.codenvy.ide.extension.runner.client.RunnerController;
import com.codenvy.ide.extension.runner.client.RunnerLocalizationConstant;
import com.codenvy.ide.extension.runner.client.RunnerResources;
import com.codenvy.ide.resources.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static com.codenvy.ide.ext.java.client.JavaExtension.JAVA_WEB_APPLICATION_PROJECT_TYPE;
import static com.codenvy.ide.ext.java.client.JavaExtension.SPRING_APPLICATION_PROJECT_TYPE;

/**
 * Action to stop application server where app is launched.
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class StopAction extends Action {

    private final ResourceProvider resourceProvider;
    private       RunnerController controller;

    @Inject
    public StopAction(RunnerController controller, RunnerResources resources,
                      ResourceProvider resourceProvider, RunnerLocalizationConstant localizationConstants) {
        super(localizationConstants.stopAppActionText(), localizationConstants.stopAppActionDescription(),
              resources.stopApp());
        this.controller = controller;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.stopActiveProject();
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        Project activeProject = resourceProvider.getActiveProject();
        if (activeProject != null) {
            StringSet natures = activeProject.getDescription().getNatures();
            e.getPresentation().setVisible(natures.contains(SPRING_APPLICATION_PROJECT_TYPE)
                                           || natures.contains(JAVA_WEB_APPLICATION_PROJECT_TYPE));
            e.getPresentation().setEnabled(controller.isAnyAppLaunched());
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}