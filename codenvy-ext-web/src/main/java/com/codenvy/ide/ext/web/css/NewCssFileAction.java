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
package com.codenvy.ide.ext.web.css;

import com.codenvy.api.analytics.logger.AnalyticsEventLogger;
import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.selection.SelectionAgent;
import com.codenvy.ide.ext.web.WebExtensionResource;
import com.codenvy.ide.ext.web.WebLocalizationConstant;
import com.codenvy.ide.newresource.DefaultNewResourceAction;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.ui.dialogs.DialogFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Action to create new CSS file.
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class NewCssFileAction extends DefaultNewResourceAction {
    private static final String DEFAULT_CONTENT = "@CHARSET \"UTF-8\";";

    @Inject
    public NewCssFileAction(AppContext appContext,
                            WebExtensionResource webExtensionResource,
                            WebLocalizationConstant localizationConstant,
                            SelectionAgent selectionAgent,
                            EditorAgent editorAgent,
                            ProjectServiceClient projectServiceClient,
                            EventBus eventBus,
                            AnalyticsEventLogger eventLogger,
                            DtoUnmarshallerFactory unmarshallerFactory,
                            DialogFactory dialogFactory) {
        super(localizationConstant.newCssFileActionTitle(),
              localizationConstant.newCssFileActionDescription(),
              webExtensionResource.css(),
              null,
              appContext,
              selectionAgent,
              editorAgent,
              projectServiceClient,
              eventBus,
              eventLogger,
              unmarshallerFactory,
              dialogFactory);
    }

    @Override
    protected String getExtension() {
        return "css";
    }

    @Override
    protected String getDefaultContent() {
        return DEFAULT_CONTENT;
    }
}
