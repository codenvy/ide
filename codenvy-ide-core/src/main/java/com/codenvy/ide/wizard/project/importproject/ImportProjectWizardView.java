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
package com.codenvy.ide.wizard.project.importproject;

import com.codenvy.ide.api.mvp.Presenter;
import com.codenvy.ide.api.mvp.View;
import com.google.inject.ImplementedBy;

/**
 * Import project wizard dialog's view.
 * 
 * @author Ann Shumilova
 */
@ImplementedBy(ImportProjectWizardViewImpl.class)
public interface ImportProjectWizardView extends View<ImportProjectWizardView.ActionDelegate> {

    /**
     * Show wizard page.
     * 
     * @param presenter
     */
    void showPage(Presenter presenter);

    /**
     * Show wizard dialog.
     */
    void showDialog();

    /**
     * Close wizard dialog.
     */
    void close();

    /**
     * Set the enabled state of the next button.
     * 
     * @param enabled <code>true</code> if enabled.
     */
    void setNextButtonEnabled(boolean enabled);
    
    /**
     * Set the enabled state of the import button.
     * 
     * @param enabled <code>true</code> if enabled.
     */
    void setImportButtonEnabled(boolean enabled);
    
    /**
     * Set the enabled state of the back button.
     * 
     * @param enabled <code>true</code> if enabled.
     */
    void setBackButtonEnabled(boolean enabled);
    
    /**
     * Set the visibility state of the loader.
     * 
     * @param isVisible <code>true</code> if visible.
     */
    void setLoaderVisibility(boolean isVisible);

    public interface ActionDelegate {
        /** Performs any actions appropriate in response to the user having pressed the Next button */
        void onNextClicked();

        /** Performs any actions appropriate in response to the user having pressed the Back button */
        void onBackClicked();

        /** Performs any actions appropriate in response to the user having pressed the Import button */
        void onImportClicked();

        /** Performs any actions appropriate in response to the user having pressed the Cancel button */
        void onCancelClicked();
    }
}