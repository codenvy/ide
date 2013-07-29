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
package com.codenvy.ide.extension.cloudfoundry.client.rename;

import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryAsyncRequestCallback;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryClientService;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryExtension;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryLocalizationConstant;
import com.codenvy.ide.extension.cloudfoundry.client.login.LoggedInHandler;
import com.codenvy.ide.extension.cloudfoundry.client.login.LoginPresenter;
import com.codenvy.ide.extension.cloudfoundry.client.marshaller.CloudFoundryApplicationUnmarshaller;
import com.codenvy.ide.extension.cloudfoundry.dto.client.DtoClientImpls;
import com.codenvy.ide.extension.cloudfoundry.shared.CloudFoundryApplication;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Presenter for rename operation with application.
 *
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: RenameApplicationPresenter.java Jul 15, 2011 11:32:02 AM vereshchaka $
 */
@Singleton
public class RenameApplicationPresenter implements RenameApplicationView.ActionDelegate {
    private RenameApplicationView               view;
    private EventBus                            eventBus;
    private ResourceProvider                    resourceProvider;
    private ConsolePart                         console;
    /** The name of application. */
    private String                              applicationName;
    private CloudFoundryLocalizationConstant    constant;
    private LoginPresenter                      loginPresenter;
    private CloudFoundryClientService           service;
    private CloudFoundryExtension.PAAS_PROVIDER paasProvider;

    /**
     * Create presenter.
     *
     * @param view
     * @param eventBus
     * @param resourceProvider
     * @param console
     * @param constant
     * @param loginPresenter
     * @param service
     */
    @Inject
    protected RenameApplicationPresenter(RenameApplicationView view, EventBus eventBus, ResourceProvider resourceProvider,
                                         ConsolePart console, CloudFoundryLocalizationConstant constant, LoginPresenter loginPresenter,
                                         CloudFoundryClientService service) {
        this.view = view;
        this.view.setDelegate(this);
        this.resourceProvider = resourceProvider;
        this.console = console;
        this.eventBus = eventBus;
        this.constant = constant;
        this.loginPresenter = loginPresenter;
        this.service = service;
    }

    /** {@inheritDoc} */
    @Override
    public void onNameChanged() {
        String newName = view.getName();
        boolean enable = !applicationName.equals(newName) && newName != null && !newName.isEmpty();
        view.setEnableRenameButton(enable);
    }

    /** {@inheritDoc} */
    @Override
    public void onRenameClicked() {
        renameApplication();
    }

    /** If user is not logged in to CloudFoundry, this handler will be called, after user logged in. */
    private LoggedInHandler renameAppLoggedInHandler = new LoggedInHandler() {
        @Override
        public void onLoggedIn() {
            renameApplication();
        }
    };

    /** Renames application. */
    private void renameApplication() {
        final String newName = view.getName();
        String projectId = resourceProvider.getActiveProject().getId();

        try {
            service.renameApplication(resourceProvider.getVfsId(), projectId, applicationName, null, newName,
                                      new CloudFoundryAsyncRequestCallback<String>(null, renameAppLoggedInHandler, null, eventBus, console,
                                                                                   constant, loginPresenter, paasProvider) {
                                          @Override
                                          protected void onSuccess(String result) {
                                              view.close();

                                              console.print(constant.renameApplicationSuccess(applicationName, newName));
                                          }
                                      });
        } catch (RequestException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            console.print(e.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onCancelClicked() {
        view.close();
    }

    /** If user is not logged in to CloudFoundry, this handler will be called, after user logged in. */
    private LoggedInHandler appInfoLoggedInHandler = new LoggedInHandler() {
        @Override
        public void onLoggedIn() {
            getApplicationInfo();
        }
    };

    /** Get the application's information. */
    private void getApplicationInfo() {
        String projectId = resourceProvider.getActiveProject().getId();
        DtoClientImpls.CloudFoundryApplicationImpl cloudFoundryApplication = DtoClientImpls.CloudFoundryApplicationImpl.make();
        CloudFoundryApplicationUnmarshaller unmarshaller = new CloudFoundryApplicationUnmarshaller(cloudFoundryApplication);

        try {
            service.getApplicationInfo(resourceProvider.getVfsId(), projectId, null, null,
                                       new CloudFoundryAsyncRequestCallback<CloudFoundryApplication>(unmarshaller, appInfoLoggedInHandler,
                                                                                                     null, eventBus, console, constant,
                                                                                                     loginPresenter, paasProvider) {
                                           @Override
                                           protected void onSuccess(CloudFoundryApplication result) {
                                               applicationName = result.getName();
                                               view.showDialog();
                                           }
                                       });
        } catch (RequestException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            console.print(e.getMessage());
        }
    }

    /** Shows dialog. */
    public void showDialog(CloudFoundryExtension.PAAS_PROVIDER paasProvider) {
        this.paasProvider = paasProvider;

        view.setName(applicationName);
        view.selectValueInRenameField();
        view.setEnableRenameButton(false);

        getApplicationInfo();
    }
}