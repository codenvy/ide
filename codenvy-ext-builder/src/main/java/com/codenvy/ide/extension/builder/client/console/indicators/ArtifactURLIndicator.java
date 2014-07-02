/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.extension.builder.client.console.indicators;

import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.action.Presentation;
import com.codenvy.ide.extension.builder.client.BuilderResources;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Action used to show artifact download URL.
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class ArtifactURLIndicator extends IndicatorAction {
    private final BuildProjectPresenter buildProjectPresenter;

    @Inject
    public ArtifactURLIndicator(BuildProjectPresenter buildProjectPresenter, BuilderResources resources) {
        super("Artifact URL", true, 190, resources);
        this.buildProjectPresenter = buildProjectPresenter;
    }

    @Override
    public void update(ActionEvent e) {
        final Presentation presentation = e.getPresentation();
        presentation.putClientProperty(Properties.DATA_PROPERTY, buildProjectPresenter.getLastBuildResultURL());
    }
}