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
package com.codenvy.ide.ext.openshift.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.paas.PaaSAgent;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.action.IdeActions;
import com.codenvy.ide.api.ui.wizard.WizardPage;
import com.codenvy.ide.ext.openshift.client.actions.*;
import com.codenvy.ide.ext.openshift.client.wizard.OpenShiftPagePresenter;
import com.codenvy.ide.json.JsonArray;
import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.json.JsonStringMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Extension add OpenShift support to the IDE Application.
 *
 * @author <a href="mailto:vzhukovskii@exoplatform.com">Vladislav Zhukovskii</a>
 * @version $Id: $
 */
@Singleton
@Extension(title = "OpenShift Support.", version = "3.0.0")
public class OpenShiftExtension {
    private static final String ID = "OpenShift";

    /**
     * Create OpenShift extension.
     *
     * @param paasAgent
     * @param resources
     * @param wizardPage
     * @param actionManager
     */
    @Inject
    public OpenShiftExtension(PaaSAgent paasAgent,
                              OpenShiftResources resources,
                              Provider<OpenShiftPagePresenter> wizardPage,
                              ActionManager actionManager,
                              ChangeDomainAction changeDomainAction,
                              SwitchAccountAction switchAccountAction,
                              ShowApplicationsAction showApplicationsAction,
                              UpdatePublicKeyAction updatePublicKeyAction,
                              ShowProjectAction showProjectAction) {

        resources.openShiftCSS().ensureInjected();

        // TODO change hard code types
        JsonStringMap<JsonArray<String>> natures = JsonCollections.createStringMap();
        natures.put("java", JsonCollections.<String>createArray("Servlet/JSP", "War"));
        natures.put("javascript", JsonCollections.<String>createArray("nodejs"));
        natures.put("Ruby", JsonCollections.<String>createArray("Rails"));
        natures.put("Python", JsonCollections.<String>createArray());
        natures.put("PHP", JsonCollections.<String>createArray());

        JsonArray<Provider<? extends WizardPage>> wizardPages = JsonCollections.createArray();
        wizardPages.add(wizardPage);

        paasAgent.register(ID, ID, resources.openShift48(), natures, wizardPages, true);

        actionManager.registerAction("openShiftChangeDomain", changeDomainAction);
        actionManager.registerAction("openShiftSwitchAccount", switchAccountAction);
        actionManager.registerAction("openShiftShowApplications", showApplicationsAction);
        actionManager.registerAction("openShiftUpdatePublicKey", updatePublicKeyAction);
        actionManager.registerAction("openShiftShowProject", showProjectAction);

        DefaultActionGroup paas = (DefaultActionGroup)actionManager.getAction(IdeActions.GROUP_PAAS);
        DefaultActionGroup openShift = new DefaultActionGroup("OpenShift", true, actionManager);
        actionManager.registerAction("openShiftPaas", openShift);
        paas.add(openShift);

        openShift.add(changeDomainAction);
        openShift.add(switchAccountAction);
        openShift.add(showApplicationsAction);
        openShift.add(updatePublicKeyAction);

        DefaultActionGroup projectPaas = (DefaultActionGroup)actionManager.getAction(IdeActions.GROUP_PROJECT_PAAS);
        projectPaas.add(showProjectAction);
    }
}