/*
 * Copyright (C) 2013 eXo Platform SAS.
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
package com.codenvy.ide.api.paas;

import com.codenvy.ide.api.extension.SDK;
import com.codenvy.ide.api.ui.preferences.PreferencesPagePresenter;
import com.codenvy.ide.api.ui.wizard.WizardPagePresenter;
import com.codenvy.ide.json.JsonArray;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Provider;

/**
 * Provides a way to register a new PaaS Extension.
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
@SDK(title = "ide.api.ui.paas")
public interface PaaSAgent {
    /**
     * Registers new PaaS.
     *
     * @param id
     * @param title
     * @param image
     * @param requiredTypes
     * @param wizardPage
     * @param preferencePage
     */
    void registerPaaS(String id, String title, ImageResource image, JsonArray<String> requiredTypes,
                      Provider<? extends WizardPagePresenter> wizardPage, PreferencesPagePresenter preferencePage);

    /**
     * Returns selected PaaS.
     *
     * @return paas
     */
    PaaS getSelectedPaaS();
}