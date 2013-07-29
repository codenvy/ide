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
package com.codenvy.ide.extension.cloudfoundry.client.apps;

import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryAsyncRequestCallback;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryClientService;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryExtension;
import com.codenvy.ide.extension.cloudfoundry.client.CloudFoundryLocalizationConstant;
import com.codenvy.ide.extension.cloudfoundry.client.delete.DeleteApplicationPresenter;
import com.codenvy.ide.extension.cloudfoundry.client.login.LoggedInHandler;
import com.codenvy.ide.extension.cloudfoundry.client.login.LoginPresenter;
import com.codenvy.ide.extension.cloudfoundry.client.marshaller.ApplicationListUnmarshaller;
import com.codenvy.ide.extension.cloudfoundry.client.marshaller.TargetsUnmarshaller;
import com.codenvy.ide.extension.cloudfoundry.client.start.StartApplicationPresenter;
import com.codenvy.ide.extension.cloudfoundry.shared.CloudFoundryApplication;
import com.codenvy.ide.json.JsonArray;
import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * The applications presenter manager CloudFounry application.
 * The presenter can start, stop, update, delete application.
 *
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: Aug 18, 2011 evgen $
 */
@Singleton
public class ApplicationsPresenter implements ApplicationsView.ActionDelegate {
    private ApplicationsView                    view;
    private String                              currentServer;
    private JsonArray<String>                   servers;
    private EventBus                            eventBus;
    private ConsolePart                         console;
    private CloudFoundryLocalizationConstant    constant;
    private StartApplicationPresenter           startAppPresenter;
    private DeleteApplicationPresenter          deleteAppPresenter;
    private LoginPresenter                      loginPresenter;
    private CloudFoundryClientService           service;
    private CloudFoundryExtension.PAAS_PROVIDER paasProvider;

    /** The callback what execute when some application's information was changed. */
    private AsyncCallback<String> appInfoChangedCallback = new AsyncCallback<String>() {
        @Override
        public void onSuccess(String result) {
            getApplicationList();
        }

        @Override
        public void onFailure(Throwable caught) {
            Log.error(ApplicationsPresenter.class, "Can not change information", caught);
        }
    };

    /**
     * Create presenter.
     *
     * @param view
     * @param eventBus
     * @param console
     * @param startAppPresenter
     * @param deleteAppPresenter
     * @param loginPresenter
     * @param constant
     * @param service
     */
    @Inject
    protected ApplicationsPresenter(ApplicationsView view, EventBus eventBus, ConsolePart console,
                                    StartApplicationPresenter startAppPresenter, DeleteApplicationPresenter deleteAppPresenter,
                                    LoginPresenter loginPresenter, CloudFoundryLocalizationConstant constant,
                                    CloudFoundryClientService service) {
        this.view = view;
        this.view.setDelegate(this);
        this.eventBus = eventBus;
        this.console = console;
        this.constant = constant;
        this.startAppPresenter = startAppPresenter;
        this.deleteAppPresenter = deleteAppPresenter;
        this.loginPresenter = loginPresenter;
        this.service = service;
        this.servers = JsonCollections.createArray();
    }

    /** {@inheritDoc} */
    @Override
    public void onCloseClicked() {
        view.close();
    }

    /** {@inheritDoc} */
    @Override
    public void onShowClicked() {
        checkLogginedToServer();
    }

    /** Gets list of available application for current user. */
    private void getApplicationList() {
        try {
            ApplicationListUnmarshaller unmarshaller =
                    new ApplicationListUnmarshaller(JsonCollections.<CloudFoundryApplication>createArray());
            LoggedInHandler loggedInHandler = new LoggedInHandler() {
                @Override
                public void
                onLoggedIn() {
                    getApplicationList();
                }
            };

            service.getApplicationList(currentServer, paasProvider,
                                       new CloudFoundryAsyncRequestCallback<JsonArray<CloudFoundryApplication>>(unmarshaller,
                                                                                                                loggedInHandler, null,
                                                                                                                currentServer, eventBus,
                                                                                                                console, constant,
                                                                                                                loginPresenter,
                                                                                                                paasProvider) {
                                           @Override
                                           protected void onSuccess(JsonArray<CloudFoundryApplication> result) {
                                               view.setApplications(result);
                                               view.setServer(currentServer);

                                               // update the list of servers, if was enter value, that doesn't present in list
                                               if (!servers.contains(currentServer)) {
                                                   getServers();
                                               }

                                               if (!view.isShown()) {
                                                   view.showDialog();
                                               }
                                           }
                                       });
        } catch (RequestException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            console.print(e.getMessage());
        }
    }

    /** Gets servers. */
    private void getServers() {
        try {
            TargetsUnmarshaller unmarshaller = new TargetsUnmarshaller(JsonCollections.<String>createArray());
            service.getTargets(paasProvider, new AsyncRequestCallback<JsonArray<String>>(unmarshaller) {
                @Override
                protected void onSuccess(JsonArray<String> result) {
                    servers = result;
                    view.setServers(servers);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    eventBus.fireEvent(new ExceptionThrownEvent(exception));
                    console.print(exception.getMessage());
                }
            });
        } catch (RequestException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            console.print(e.getMessage());
        }
    }

    /** Show dialog. */
    public void showDialog(CloudFoundryExtension.PAAS_PROVIDER paasProvider) {
        this.paasProvider = paasProvider;

        checkLogginedToServer();
    }

    /** Gets target from CloudFoundry server. If this works well then we will know we have connect to CloudFoundry server. */
    private void checkLogginedToServer() {
        try {
            TargetsUnmarshaller unmarshaller = new TargetsUnmarshaller(JsonCollections.<String>createArray());
            service.getTargets(paasProvider, new AsyncRequestCallback<JsonArray<String>>(unmarshaller) {
                @Override
                protected void onSuccess(JsonArray<String> result) {
                    if (!result.isEmpty()) {
                        servers = result;
                    } else if (paasProvider == CloudFoundryExtension.PAAS_PROVIDER.CLOUD_FOUNDRY) {
                        servers = JsonCollections.createArray(CloudFoundryExtension.DEFAULT_CF_SERVER);
                    }
                    // open view
                    openView();
                }

                @Override
                protected void onFailure(Throwable exception) {
                    eventBus.fireEvent(new ExceptionThrownEvent(exception));
                    console.print(exception.getMessage());
                }
            });
        } catch (RequestException e) {
            eventBus.fireEvent(new ExceptionThrownEvent(e));
            console.print(e.getMessage());
        }
    }

    /** Opens view. */
    private void openView() {
        view.setServers(servers);
        // fill the list of applications
        currentServer = servers.get(0);
        getApplicationList();
    }

    /** {@inheritDoc} */
    @Override
    public void onStartClicked(CloudFoundryApplication app) {
        startAppPresenter.startApp(app.getName(), currentServer, paasProvider, appInfoChangedCallback);
    }

    /** {@inheritDoc} */
    @Override
    public void onStopClicked(CloudFoundryApplication app) {
        startAppPresenter.stopApp(app.getName(), currentServer, paasProvider, appInfoChangedCallback);
    }

    /** {@inheritDoc} */
    @Override
    public void onRestartClicked(CloudFoundryApplication app) {
        startAppPresenter.restartApp(app.getName(), currentServer, paasProvider, appInfoChangedCallback);
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteClicked(CloudFoundryApplication app) {
        deleteAppPresenter.deleteApp(app.getName(), currentServer, paasProvider, appInfoChangedCallback);
    }
}