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
package com.codenvy.ide.factory.client.receive;


import com.codenvy.factory.SimpleFactoryUrlFormat;
import com.codenvy.ide.client.util.logging.Log;
import com.codenvy.ide.factory.client.FactoryExtension;
import com.codenvy.ide.factory.client.FactorySpec10;
import com.codenvy.ide.factory.client.copy.CopySpec10;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.AsyncRequest;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.ui.client.dialog.BooleanValueReceivedHandler;
import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.framework.application.IDELoader;
import org.exoplatform.ide.client.framework.application.event.VfsChangedEvent;
import org.exoplatform.ide.client.framework.application.event.VfsChangedHandler;
import org.exoplatform.ide.client.framework.event.IDELoadCompleteEvent;
import org.exoplatform.ide.client.framework.event.IDELoadCompleteHandler;
import org.exoplatform.ide.client.framework.event.OpenFileEvent;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.client.framework.paas.PaaS;
import org.exoplatform.ide.client.framework.project.ConvertToProjectEvent;
import org.exoplatform.ide.client.framework.project.OpenProjectEvent;
import org.exoplatform.ide.client.framework.project.ProjectOpenedEvent;
import org.exoplatform.ide.client.framework.project.ProjectOpenedHandler;
import org.exoplatform.ide.client.framework.ui.JsPopUpOAuthWindow;
import org.exoplatform.ide.client.framework.util.StringUnmarshaller;
import org.exoplatform.ide.client.framework.util.Utils;
import org.exoplatform.ide.client.framework.websocket.WebSocketException;
import org.exoplatform.ide.client.framework.websocket.rest.RequestCallback;
import org.exoplatform.ide.client.framework.websocket.rest.RequestMessage;
import org.exoplatform.ide.client.framework.websocket.rest.RequestMessageBuilder;
import org.exoplatform.ide.git.client.GitExtension;
import org.exoplatform.ide.vfs.client.VirtualFileSystem;
import org.exoplatform.ide.vfs.client.marshal.ChildrenUnmarshaller;
import org.exoplatform.ide.vfs.client.marshal.FolderUnmarshaller;
import org.exoplatform.ide.vfs.client.marshal.ItemUnmarshaller;
import org.exoplatform.ide.vfs.client.model.FileModel;
import org.exoplatform.ide.vfs.client.model.FolderModel;
import org.exoplatform.ide.vfs.client.model.ItemWrapper;
import org.exoplatform.ide.vfs.client.model.ProjectModel;
import org.exoplatform.ide.vfs.shared.File;
import org.exoplatform.ide.vfs.shared.Item;
import org.exoplatform.ide.vfs.shared.ItemType;
import org.exoplatform.ide.vfs.shared.Property;
import org.exoplatform.ide.vfs.shared.PropertyImpl;
import org.exoplatform.ide.vfs.shared.VirtualFileSystemInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:vparfonov@exoplatform.com">Vitaly Parfonov</a>
 * @version $Id: CodeNowHandler.java Dec 6, 2012 vetal $
 */
public class FanctoryHandler implements VfsChangedHandler, StartWithInitParamsHandler, ProjectOpenedHandler, IDELoadCompleteHandler {

    private final String                restServiceContext;
    private       VirtualFileSystemInfo vfs;
    private       String                filePathToOpen;
    private       String                copiedProjectIdToOpen;

    public FanctoryHandler() {
        IDE.addHandler(VfsChangedEvent.TYPE, this);
        IDE.addHandler(StartWithInitParamsEvent.TYPE, this);
        restServiceContext = Utils.getWorkspaceName();
    }

    /**
     * @see org.exoplatform.ide.client.framework.application.event.VfsChangedHandler#onVfsChanged(org.exoplatform.ide.client.framework
     *      .application.event.VfsChangedEvent)
     */
    @Override
    public void onVfsChanged(VfsChangedEvent event) {
        this.vfs = event.getVfsInfo();
    }

    @Override
    public void onStartWithInitParams(StartWithInitParamsEvent event) {
        if (isFactoryValidParam(event.getParameterMap())) {
            handleFactory(event.getParameterMap());
        } else if (isCopyAllProjectsValidParam(event.getParameterMap())) {
            handleCopyProjects(event.getParameterMap());
        }
    }

    private void handleFactory(Map<String, List<String>> parameterMap) {
        String giturl = parameterMap.get(FactorySpec10.VCS_URL).get(0);

        String prjName;

        if (parameterMap.get(FactorySpec10.PROJECT_NAME) != null
            && !parameterMap.get(FactorySpec10.PROJECT_NAME).isEmpty()) {
            prjName = parameterMap.get(FactorySpec10.PROJECT_NAME).get(0);
        } else {
            prjName = giturl.substring(giturl.lastIndexOf('/') + 1, giturl.lastIndexOf(".git"));
        }

        String prjType = null;

        if (parameterMap.get(FactorySpec10.PROJECT_TYPE) != null
            && !parameterMap.get(FactorySpec10.PROJECT_TYPE).isEmpty()) {
            prjType = URL.decodeQueryString(parameterMap.get(FactorySpec10.PROJECT_TYPE).get(0));
        }

        String idCommit = parameterMap.get(FactorySpec10.COMMIT_ID).get(0);


        String action = parameterMap.get(FactorySpec10.ACTION_PARAMETER).get(0);
        action = (action != null && !action.isEmpty()) ? "&action=" + action : "";
        prjType = (prjType != null && !prjType.isEmpty()) ? "&ptype=" + prjType : "";

        if (parameterMap.get(FactorySpec10.FILE_TO_OPEN) != null
            && !parameterMap.get(FactorySpec10.FILE_TO_OPEN).isEmpty()) {
            filePathToOpen = URL.decodeQueryString(parameterMap.get(FactorySpec10.FILE_TO_OPEN).get(0));
        }

        String keepVcsInfo = null;

        if (parameterMap.get(FactorySpec10.KEEP_VCS_INFO) != null
            && !parameterMap.get(FactorySpec10.KEEP_VCS_INFO).isEmpty()) {
            keepVcsInfo = URL.decodeQueryString(parameterMap.get(FactorySpec10.KEEP_VCS_INFO).get(0));
        }

        cloneProject(giturl, prjName, prjType, idCommit, action, keepVcsInfo);
    }

    /** @param initParam */
    private boolean isFactoryValidParam(Map<String, List<String>> initParam) {
        if (initParam == null || initParam.isEmpty()) {
            return false;
        }
        if (!initParam.containsKey(FactorySpec10.VERSION_PARAMETER)
            || initParam.get(FactorySpec10.VERSION_PARAMETER).size() != 1
            || !initParam.get(FactorySpec10.VERSION_PARAMETER).get(0).equals(FactorySpec10.CURRENT_VERSION)) {
            return false;
        }
        if (!initParam.containsKey(FactorySpec10.VCS) || initParam.get(FactorySpec10.VCS).isEmpty()
            || !initParam.get(FactorySpec10.VCS).get(0).equalsIgnoreCase(FactorySpec10.DEFAULT_VCS)) {
            return false;
        }
        if (!initParam.containsKey(FactorySpec10.VCS_URL) || initParam.get(FactorySpec10.VCS_URL) == null
            || initParam.get(FactorySpec10.VCS_URL).isEmpty()) {
            return false;
        }
        return true;
    }

    private void handleCopyProjects(Map<String, List<String>> parameterMap) {
        final String downloadUrl = parameterMap.get(CopySpec10.DOWNLOAD_URL).get(0);
        final String projectId = parameterMap.get(CopySpec10.PROJECT_ID).get(0);

        IDE.addHandler(IDELoadCompleteEvent.TYPE, this);

        //Lets the magic begin. need to rework.
        List<String> projectsToCopy = new ArrayList<String>();
        for (String project : projectId.split(";")) {
            String[] dividedIdAndName = project.split(":");
            if (dividedIdAndName.length > 1) {
                projectsToCopy.add(dividedIdAndName[0]);
            }
        }
        if (projectsToCopy.size() == 1) {
            copiedProjectIdToOpen = projectsToCopy.get(0);
        }
        projectsToCopy.clear();

        try {
            String uri = "/copy/projects?" + CopySpec10.DOWNLOAD_URL + "=" + downloadUrl + "&" + CopySpec10.PROJECT_ID + "=" + projectId;
            RequestMessage message = RequestMessageBuilder.build(RequestBuilder.POST, restServiceContext + uri).getRequestMessage();
            IDE.messageBus().send(message, new RequestCallback<Void>() {
                @Override
                protected void onSuccess(Void result) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            IDE.fireEvent(new IDELoadCompleteEvent());
                        }
                    });
                }

                @Override
                protected void onFailure(Throwable exception) {
                    IDE.fireEvent(new ExceptionThrownEvent(exception));
                }
            });
        } catch (WebSocketException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    @Override
    public void onIDELoadComplete(IDELoadCompleteEvent event) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                if (copiedProjectIdToOpen != null && !copiedProjectIdToOpen.isEmpty()) {
                    try {
                        VirtualFileSystem.getInstance().getItemById(copiedProjectIdToOpen, new AsyncRequestCallback<ItemWrapper>(
                                new ItemUnmarshaller(new ItemWrapper())) {
                            @Override
                            protected void onSuccess(ItemWrapper result) {
                                if (result.getItem() instanceof ProjectModel) {
                                    IDE.fireEvent(new OpenProjectEvent((ProjectModel)result.getItem()));
                                }
                                copiedProjectIdToOpen = null;
                                removeIDELoadCompleteHandler();
                            }

                            @Override
                            protected void onFailure(Throwable exception) {
                                Log.error(FanctoryHandler.class, exception.getMessage());
                                removeIDELoadCompleteHandler();
                            }
                        });
                    } catch (RequestException e) {
                        Log.error(FanctoryHandler.class, e.getMessage());
                        removeIDELoadCompleteHandler();
                    }
                }
            }
        });
    }

    private void removeIDELoadCompleteHandler() {
        IDE.removeHandler(IDELoadCompleteEvent.TYPE, this);
    }

    private boolean isCopyAllProjectsValidParam(Map<String, List<String>> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            return false;
        }
        return parameterMap.get(CopySpec10.DOWNLOAD_URL) != null &&
               parameterMap.get(CopySpec10.PROJECT_ID) != null;
    }

    private void cloneProject(final String giturl, final String prjName, final String prjType, final String idCommit, final String action,
                              final String keepVcsInfo) {
        try {

            VirtualFileSystem.getInstance()
                             .getChildren(vfs.getRoot(), ItemType.PROJECT,
                                          new AsyncRequestCallback<List<Item>>(new ChildrenUnmarshaller(new ArrayList<Item>())) {
                                              @Override
                                              protected void onSuccess(List<Item> result) {
                                                  boolean itemExist = false;
                                                  for (Item item : result) {
                                                      if (item.getName().equals(prjName)) {
                                                          itemExist = true;
                                                      }

                                                      if (item.hasProperty("codenow")) {
                                                          String codenow = item.getPropertyValue("codenow");
                                                          if (codenow.equals(giturl)) {
                                                              IDE.fireEvent(new OpenProjectEvent((ProjectModel)item));
                                                              return;
                                                          }
                                                      }
                                                  }

                                                  if (itemExist) {
                                                      doClone(giturl, "origin",
                                                              prjName + "-" + Random.nextInt(Integer.MAX_VALUE), prjType, idCommit, action,
                                                              keepVcsInfo);
                                                  } else {
                                                      doClone(giturl, "origin", prjName, prjType, idCommit, action, keepVcsInfo);
                                                  }
                                              }

                                              @Override
                                              protected void onFailure(Throwable exception) {
                                                  doClone(giturl, "origin", prjName, prjType, idCommit, action, keepVcsInfo);
                                              }
                                          });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    /**
     * Going to cloning repository. Clone process flow 3 steps: - create new folder with name workDir - clone repository to this folder -
     * convert folder to project. This need because by default project with out file and folder not empty. It content ".project" item.
     * Clone
     * is impossible to not empty folder
     *
     * @param remoteUri
     *         - git url
     * @param remoteName
     *         - remote name (by default origin)
     * @param workDir
     *         - name of target folder
     */
    public void doClone(final String remoteUri, final String remoteName, final String workDir, final String prjType,
                        final String idCommit, final String action, final String keepVcsInfo) {
        FolderModel folder = new FolderModel();
        folder.setName(workDir);
        try {
            VirtualFileSystem.getInstance().createFolder(vfs.getRoot(),
                                                         new AsyncRequestCallback<FolderModel>(new FolderUnmarshaller(folder)) {
                                                             @Override
                                                             protected void onSuccess(FolderModel result) {
                                                                 cloneRepository(remoteUri, remoteName, prjType, result, idCommit, action,
                                                                                 keepVcsInfo);
                                                             }

                                                             @Override
                                                             protected void onFailure(Throwable exception) {
                                                                 String errorMessage =
                                                                         (exception.getMessage() != null &&
                                                                          exception.getMessage().length() > 0)
                                                                         ? exception.getMessage()
                                                                         : GitExtension.MESSAGES.cloneFailed(remoteUri);
                                                                 IDE.fireEvent(new OutputEvent(errorMessage, Type.GIT));
                                                             }
                                                         });
        } catch (RequestException e) {
            e.printStackTrace();
            String errorMessage =
                    (e.getMessage() != null && e.getMessage().length() > 0) ? e.getMessage()
                                                                            : GitExtension.MESSAGES.cloneFailed(remoteUri);
            IDE.fireEvent(new OutputEvent(errorMessage, Type.GIT));
        }
    }

    /**
     * Open native js popup window with wso2 authorization page.
     *
     * @param authorizeCallback
     *         callback for authorization status.
     */
    private void openOauthPopupWindow(final AsyncCallback<Void> authorizeCallback) {
        final JsPopUpOAuthWindow.JsPopUpOAuthWindowCallback authWindowCallback =
                new JsPopUpOAuthWindow.JsPopUpOAuthWindowCallback() {
                    @Override
                    public void oAuthFinished(int authenticationStatus) {
                        if (authenticationStatus == 2) { //means that auth was successful
                            authorizeCallback.onSuccess(null);
                        } else if (authenticationStatus == 1) { //means that auth was fail
                            authorizeCallback.onFailure(new Exception(FactoryExtension.LOCALIZATION_CONSTANTS.privateRepoAuthFailed()));
                        } else { //if user permit login
                            authorizeCallback
                                    .onFailure(new Exception(FactoryExtension.LOCALIZATION_CONSTANTS.privateRepoAuthPermitted()));
                        }
                    }
                };

        Dialogs.getInstance().ask(FactoryExtension.LOCALIZATION_CONSTANTS.privateRepoNeedAuthTitle(),
                                  FactoryExtension.LOCALIZATION_CONSTANTS.privateRepoNeedAuthContent("wso2"),
                                  new BooleanValueReceivedHandler() {
                                      @Override
                                      public void booleanValueReceived(Boolean value) {
                                          if (value != null && value) {
                                              String authUrl = Utils.getAuthorizationContext()
                                                               + "/ide/oauth/authenticate?oauth_provider=wso2"
                                                               + "&userId=" + IDE.user.getName() +
                                                               "&redirect_after_login=/ide/" +
                                                               Utils.getWorkspaceName();

                                              JsPopUpOAuthWindow authWindow = new JsPopUpOAuthWindow(authUrl,
                                                                                                     Utils.getAuthorizationErrorPageURL(),
                                                                                                     950, 500, authWindowCallback);
                                              authWindow.loginWithOAuth();
                                          }
                                      }
                                  }, true);
    }

    /**
     * Clone of the repository by sending request over WebSocket or HTTP.
     *
     * @param remoteUri
     *         the location of the remote repository
     * @param remoteName
     *         remote name instead of "origin"
     * @param folder
     *         folder (root of GIT repository)
     */
    private void cloneRepository(final String remoteUri, final String remoteName, final String prjType, final FolderModel folder,
                                 final String idCommit, final String action, final String keepVcsInfo) {
        try {
            IDELoader.getInstance().setMessage("Cloning project ... ");
            IDELoader.getInstance().show();
            String uri = "/factory/clone?vfsid=" + vfs.getId() + "&projectid=" + folder.getId() + "&remoteuri=" + remoteUri + "&idcommit=" +
                         idCommit + prjType + action + "&keepvcsinfo=" + keepVcsInfo;
            RequestMessage message = RequestMessageBuilder.build(RequestBuilder.POST, restServiceContext + uri).getRequestMessage();

            IDE.messageBus().send(message, new RequestCallback<StringBuilder>(new StringUnmarshaller(new StringBuilder())) {
                @Override
                protected void onSuccess(StringBuilder result) {
                    IDELoader.getInstance().hide();
                    Log.info(FanctoryHandler.class, result.toString());
                    JSONObject object = JSONParser.parseLenient(result.toString()).isObject();
                    onCloneSuccess(object, prjType, remoteUri);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    IDELoader.getInstance().hide();

                    if (remoteUri.matches(SimpleFactoryUrlFormat.WSO_2_URL_STRING) && exception.getMessage().contains("not authorized")) {
                        openOauthPopupWindow(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                IDE.fireEvent(new OutputEvent(throwable.getMessage(), Type.WARNING));
                            }

                            @Override
                            public void onSuccess(Void var) {
                                cloneRepository(remoteUri, remoteName, prjType, folder, idCommit, action, keepVcsInfo);
                            }
                        });
                    } else {
                        handleError(exception, remoteUri);
                    }
                }
            });
        } catch (WebSocketException e) {
            cloneRepositoryREST(remoteUri, remoteName, prjType, folder, idCommit, action, keepVcsInfo);
        }
    }

    /** Get the necessary parameters values and call the clone repository method (over HTTP). */
    private void cloneRepositoryREST(final String remoteUri, final String remoteName, final String prjType, final FolderModel folder,
                                     final String idCommit, final String action, final String keepVcsInfo) {

        try {
            String uri = "/factory/clone?vfsid=" + vfs.getId() + "&projectid=" + folder.getId() + "&remoteuri=" + remoteUri + "&idcommit=" +
                         idCommit + prjType + action + "&keepvcsinfo=" + keepVcsInfo;
            AsyncRequest.build(RequestBuilder.POST, uri).send(new AsyncRequestCallback<Object>() {
                @Override
                protected void onSuccess(Object result) {
                    IDELoader.getInstance().hide();
                }

                @Override
                protected void onFailure(Throwable exception) {
                    IDELoader.getInstance().hide();

                    if (remoteUri.matches(SimpleFactoryUrlFormat.WSO_2_URL_STRING) && exception.getMessage().contains("not authorized")) {
                        openOauthPopupWindow(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                IDE.fireEvent(new OutputEvent(throwable.getMessage()));
                            }

                            @Override
                            public void onSuccess(Void var) {
                                cloneRepositoryREST(remoteUri, remoteName, prjType, folder, idCommit, action, keepVcsInfo);
                            }
                        });
                    } else {
                        handleError(exception, remoteUri);
                    }
                }
            });
        } catch (RequestException e) {
            IDELoader.getInstance().hide();
            handleError(e, remoteUri);
        }
    }

    private Property getTarget(String projectType) {
        org.exoplatform.ide.client.framework.project.ProjectType currentProjType =
                org.exoplatform.ide.client.framework.project.ProjectType.fromValue(projectType);
        List<String> target = new ArrayList<String>();
        List<PaaS> paases = IDE.getInstance().getPaaSes();
        for (PaaS paas : paases) {
            if (paas.getSupportedProjectTypes().contains(currentProjType)) {
                target.add(paas.getId());
            }
        }
        return new PropertyImpl("exoide:target", target);
    }

    /**
     * Perform actions when repository was successfully cloned.
     *
     * @param folder
     *         {@link FolderModel} to clone
     */
    private void onCloneSuccess(JSONObject object, String prjType, String remoteUri) {
        IDE.fireEvent(new OutputEvent(GitExtension.MESSAGES.cloneSuccess(remoteUri), Type.GIT));
        // TODO: not good, comment temporary need found other way
        // for inviting collaborators
        // showInvitation(repoInfo.getRemoteUri());
        try {
            String itemType = object.get("itemType").isString().stringValue();
            Log.info(FanctoryHandler.class, itemType);
            IDE.addHandler(ProjectOpenedEvent.TYPE, this);
            if (ItemType.PROJECT.toString().equalsIgnoreCase(itemType)) {
                ProjectModel projectModel = new ProjectModel(object);
                VirtualFileSystem.getInstance().getItemById(projectModel.getId(),
                                                            new AsyncRequestCallback<ItemWrapper>(new ItemUnmarshaller(new ItemWrapper())) {
                                                                @Override
                                                                protected void onSuccess(ItemWrapper result) {
                                                                    if (result.getItem() instanceof ProjectModel) {
                                                                        ProjectModel projectModel = (ProjectModel)result.getItem();
                                                                        Property target = getTarget(projectModel.getProjectType());
                                                                        Property oldTargetProperty =
                                                                                projectModel.getProperty("exoide:target");
                                                                        List<Property> properties = projectModel.getProperties();
                                                                        properties.add(target);
                                                                        properties.remove(oldTargetProperty);
                                                                        writeUserPropertiesToProject(projectModel);
                                                                    }
                                                                }

                                                                @Override
                                                                protected void onFailure(Throwable exception) {
                                                                }
                                                            });
            } else {
                List<Property> properties = new ArrayList<Property>();
                String id = object.get("id").isString().stringValue();
                properties.add(new PropertyImpl("codenow", remoteUri));
                IDE.fireEvent(new ConvertToProjectEvent(id, vfs.getId(), prjType, properties));
            }

        } catch (Throwable e) {
            Log.error(getClass(), e);
        }
    }

    private void writeUserPropertiesToProject(final ProjectModel project) {
        if (project.getLinks().isEmpty()) {
            try {
                VirtualFileSystem.getInstance()
                                 .getItemById(project.getId(),
                                              new AsyncRequestCallback<ItemWrapper>(new ItemUnmarshaller(new ItemWrapper(project))) {

                                                  @Override
                                                  protected void onSuccess(ItemWrapper result) {
                                                      project.setLinks(result.getItem().getLinks());
                                                      updateProjectProperties(project);
                                                  }

                                                  @Override
                                                  protected void onFailure(Throwable exception) {
                                                      IDE.fireEvent(new ExceptionThrownEvent(exception));
                                                  }
                                              });
            } catch (RequestException e) {
                IDE.fireEvent(new ExceptionThrownEvent(e));
            }
        } else {
            updateProjectProperties(project);
        }
    }
    
    private void updateProjectProperties(final ProjectModel projectModel) {
        try {
            VirtualFileSystem.getInstance().updateItem(projectModel, null, new AsyncRequestCallback<ItemWrapper>() {
                @Override
                protected void onSuccess(ItemWrapper result) {
                    IDE.fireEvent(new OpenProjectEvent(projectModel));
                }

                @Override
                protected void onFailure(Throwable e) {
                    Log.error(getClass(), e);
                }
            });
        } catch (RequestException e) {
            Log.error(getClass(), e);
        }
    }

    @Override
    public void onProjectOpened(final ProjectOpenedEvent event) {
        if (filePathToOpen != null && !filePathToOpen.isEmpty()) {
            final String fileFullPath = new StringBuilder(event.getProject().getPath()).append('/').append(filePathToOpen).toString();

            ItemUnmarshaller unmarshaller = new ItemUnmarshaller(new ItemWrapper());

            try {
                VirtualFileSystem.getInstance().getItemByPath(fileFullPath, new AsyncRequestCallback<ItemWrapper>(unmarshaller) {
                    @Override
                    protected void onSuccess(final ItemWrapper result) {
                        if (result.getItem() instanceof File) {
                            FileModel file = (FileModel)result.getItem();
                            file.setProject(event.getProject());
                            IDE.fireEvent(new OpenFileEvent(file));
                        }
                        removeProjectOpenedHandler();
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        IDE.fireEvent(new OutputEvent("File \"" + fileFullPath + "\" doesn't exist.", Type.WARNING));
                        removeProjectOpenedHandler();
                    }
                });
            } catch (RequestException e) {
                IDE.fireEvent(new ExceptionThrownEvent(e));
                removeProjectOpenedHandler();
            }
        }
    }

    private void removeProjectOpenedHandler() {
        IDE.removeHandler(ProjectOpenedEvent.TYPE, this);
    }

    private void handleError(Throwable e, String remoteUri) {
        String errorMessage = (e.getMessage() != null && e.getMessage().length() > 0) ? e.getMessage()
                                                                                      : GitExtension.MESSAGES.cloneFailed(remoteUri);
        IDE.fireEvent(new OutputEvent(errorMessage, Type.GIT));
    }

}
