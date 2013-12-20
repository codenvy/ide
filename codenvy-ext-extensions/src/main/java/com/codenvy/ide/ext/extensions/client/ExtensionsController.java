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
package com.codenvy.ide.ext.extensions.client;

import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.core.rest.shared.dto.ServiceError;
import com.codenvy.api.runner.ApplicationStatus;
import com.codenvy.api.runner.dto.ApplicationProcessDescriptor;
import com.codenvy.ide.api.event.ProjectActionEvent;
import com.codenvy.ide.api.event.ProjectActionHandler;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.commons.exception.ServerException;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import java.util.List;

import static com.codenvy.ide.api.notification.Notification.Status.FINISHED;
import static com.codenvy.ide.api.notification.Notification.Status.PROGRESS;
import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

/**
 * This class controls launching extensions.
 *
 * @author <a href="mailto:azatsarynnyy@codenvy.com">Artem Zatsarynnyy</a>
 * @version $Id: ExtensionsController.java Jul 3, 2013 3:07:52 PM azatsarynnyy $
 */
@Singleton
public class ExtensionsController implements Notification.OpenNotificationHandler {
    private WorkspaceAgent                 workspaceAgent;
    private ResourceProvider               resourceProvider;
    private ConsolePart                    console;
    private ExtRuntimeClientService        service;
    private ExtRuntimeLocalizationConstant constant;
    private NotificationManager            notificationManager;
    private Notification                   notification;
    private DtoFactory                     dtoFactory;
    private Project                        currentProject;
    /** Launched app. */
    private ApplicationProcessDescriptor   applicationProcessDescriptor;
    /** Is launching of any application in progress? */
    private boolean                        isLaunchingInProgress;

    /**
     * Create controller.
     *
     * @param resourceProvider
     *         {@link ResourceProvider}
     * @param workspaceAgent
     *         {@link WorkspaceAgent}
     * @param eventBus
     *         {@link EventBus}
     * @param console
     *         {@link ConsolePart}
     * @param service
     *         {@link ExtRuntimeClientService}
     * @param constant
     *         {@link ExtRuntimeLocalizationConstant}
     * @param notificationManager
     *         {@link NotificationManager}
     * @param dtoFactory
     *         {@link DtoFactory}
     */
    @Inject
    protected ExtensionsController(ResourceProvider resourceProvider, EventBus eventBus, WorkspaceAgent workspaceAgent,
                                   final ConsolePart console, ExtRuntimeClientService service,
                                   ExtRuntimeLocalizationConstant constant, NotificationManager notificationManager,
                                   DtoFactory dtoFactory) {
        this.resourceProvider = resourceProvider;
        this.workspaceAgent = workspaceAgent;
        this.console = console;
        this.service = service;
        this.constant = constant;
        this.notificationManager = notificationManager;
        this.dtoFactory = dtoFactory;

        eventBus.addHandler(ProjectActionEvent.TYPE, new ProjectActionHandler() {
            @Override
            public void onProjectOpened(ProjectActionEvent event) {
                isLaunchingInProgress = false;
                applicationProcessDescriptor = null;
            }

            @Override
            public void onProjectClosed(ProjectActionEvent event) {
                isLaunchingInProgress = false;
                if (isAnyAppLaunched()) {
                    stop();
                    console.clear();
                }
                currentProject = null;
                applicationProcessDescriptor = null;
            }

            @Override
            public void onProjectDescriptionChanged(ProjectActionEvent event) {
                // do nothing
            }
        });
    }

    /**
     * Check whether any application is launched.
     *
     * @return <code>true</code> if any application is launched, and <code>false</code> otherwise
     */
    public boolean isAnyAppLaunched() {
        return applicationProcessDescriptor != null && !isLaunchingInProgress;
    }

    /** Launch Codenvy extension. */
    public void launch() {
        currentProject = resourceProvider.getActiveProject();
        if (currentProject == null) {
            Window.alert("Project is not opened.");
            return;
        }

        if (isLaunchingInProgress) {
            Window.alert("Launching of another application is in progress now.");
            return;
        }

        isLaunchingInProgress = true;
        notification = new Notification(constant.extensionLaunching(currentProject.getName()), PROGRESS, this);
        notificationManager.showNotification(notification);

        try {
            service.launch(currentProject.getName(),
                           new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                               @Override
                               protected void onSuccess(String result) {
                                   applicationProcessDescriptor =
                                           dtoFactory.createDtoFromJson(result, ApplicationProcessDescriptor.class);
                                   startCheckingStatus(applicationProcessDescriptor);
                               }

                               @Override
                               protected void onFailure(Throwable exception) {
                                   isLaunchingInProgress = false;
                                   applicationProcessDescriptor = null;
                                   onFail(constant.launchExtensionFailed(currentProject.getName()), exception);
                               }
                           });
        } catch (RequestException e) {
            isLaunchingInProgress = false;
            applicationProcessDescriptor = null;
            onFail(constant.launchExtensionFailed(currentProject.getName()), e);
        }
    }

    /** Get logs of the currently launched application. */
    public void getLogs() {
        final Link viewLogsLink = getAppLink(applicationProcessDescriptor, LinkRel.VIEW_LOGS);
        if (viewLogsLink == null) {
            onFail(constant.getExtensionLogsFailed(), null);
        }

        try {
            service.getLogs(viewLogsLink, new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    console.printf(result);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    onFail(constant.getExtensionLogsFailed(), exception);
                }
            });
        } catch (RequestException e) {
            onFail(constant.getExtensionLogsFailed(), e);
        }
    }

    /** Stop the currently launched application. */
    public void stop() {
        final Link stopLink = getAppLink(applicationProcessDescriptor, LinkRel.STOP);
        if (stopLink == null) {
            onFail(constant.stopExtensionFailed(currentProject.getName()), null);
        }

        try {
            service.stop(stopLink, new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    applicationProcessDescriptor = null;
                    console.print(constant.extensionStopped(currentProject.getName()));
                }

                @Override
                protected void onFailure(Throwable exception) {
                    onFail(constant.stopExtensionFailed(currentProject.getName()), exception);
                }
            });
        } catch (RequestException e) {
            onFail(constant.stopExtensionFailed(currentProject.getName()), e);
        }
    }

    private void afterApplicationLaunched(ApplicationProcessDescriptor appDescriptor) {
        this.applicationProcessDescriptor = appDescriptor;

        UrlBuilder uriBuilder = new UrlBuilder().setProtocol(Window.Location.getProtocol())
                                                .setHost(Window.Location.getHostName())
                                                .setPort(appDescriptor.getPort())
                                                .setPath("ide/dev-monit");

        final Link codeServerLink = getAppLink(appDescriptor, LinkRel.CODE_SERVER);
        if (codeServerLink != null) {
            // Since code server link has been provided it should contains at least host name/address and port.
            String[] split = codeServerLink.getHref().split(":");
            String port = null;
            String host = null;
            if (split.length == 2) {
                host = split[0];
                port = split[1];
            } else if (split.length == 3) {
                host = split[0] + ':' + split[1];
                port = split[2];
            }
            uriBuilder.setParameter("h", host).setParameter("p", port);
        }

        final String uri = uriBuilder.buildString();
        console.print(constant.extensionLaunchedOnUrls(currentProject.getName(),
                                                       "<a href=\"" + uri + "\" target=\"_blank\">" + uri + "</a>"));
        notification.setStatus(FINISHED);
    }

    private void onFail(String message, Throwable exception) {
        if (notification != null) {
            notification.setStatus(FINISHED);
            notification.setType(ERROR);
            notification.setMessage(message);
        }

        if (exception != null && exception.getMessage() != null) {
            message += ": " + exception.getMessage();
        }
        console.printf(message);
    }

    private void startCheckingStatus(final ApplicationProcessDescriptor appDescriptor) {
        new Timer() {
            @Override
            public void run() {
                try {
                    service.getStatus(
                            getAppLink(appDescriptor, LinkRel.STATUS),
                            new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                @Override
                                protected void onSuccess(String response) {
                                    ApplicationProcessDescriptor newAppDescriptor =
                                            dtoFactory.createDtoFromJson(response,
                                                                         ApplicationProcessDescriptor.class);

                                    ApplicationStatus status = newAppDescriptor.getStatus();
                                    if (status == ApplicationStatus.RUNNING) {
                                        isLaunchingInProgress = false;
                                        afterApplicationLaunched(newAppDescriptor);
                                    } else if (status == ApplicationStatus.STOPPED || status == ApplicationStatus.NEW) {
                                        schedule(3000);
                                    } else if (status == ApplicationStatus.CANCELLED) {
                                        isLaunchingInProgress = false;
                                        applicationProcessDescriptor = null;
                                        onFail(constant.launchExtensionFailed(currentProject.getName()), null);
                                    }
                                }

                                @Override
                                protected void onFailure(Throwable exception) {
                                    isLaunchingInProgress = false;
                                    applicationProcessDescriptor = null;

                                    if (exception instanceof ServerException &&
                                        ((ServerException)exception).getHTTPStatus() == 500) {
                                        ServiceError e = dtoFactory
                                                .createDtoFromJson(exception.getMessage(), ServiceError.class);
                                        onFail(constant.launchExtensionFailed(currentProject.getName()) + ": " +
                                               e.getMessage(), null);
                                    } else {
                                        onFail(constant.launchExtensionFailed(currentProject.getName()), exception);
                                    }
                                }
                            });
                } catch (RequestException e) {
                    isLaunchingInProgress = false;
                    applicationProcessDescriptor = null;
                    onFail(constant.launchExtensionFailed(currentProject.getName()), e);
                }
            }
        }.run();
    }

    private Link getAppLink(ApplicationProcessDescriptor appDescriptor, LinkRel linkRel) {
        Link linkToReturn = null;
        List<Link> links = appDescriptor.getLinks();
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            if (link.getRel().equalsIgnoreCase(linkRel.getValue()))
                linkToReturn = link;
        }
        return linkToReturn;
    }

    @Override
    public void onOpenClicked() {
        workspaceAgent.setActivePart(console);
    }

    /** Enum of known runner links with its rels. */
    private static enum LinkRel {
        STOP("stop"),
        VIEW_LOGS("view logs"),
        CODE_SERVER("code server"),
        STATUS("get status");
        private final String value;

        private LinkRel(String rel) {
            this.value = rel;
        }

        private String getValue() {
            return value;
        }
    }
}
