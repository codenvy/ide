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
package org.exoplatform.ide.git.client;

import com.google.gwt.core.client.GWT;

import org.exoplatform.ide.client.framework.application.event.InitializeServicesEvent;
import org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler;
import org.exoplatform.ide.client.framework.module.Extension;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.util.Utils;
import org.exoplatform.ide.git.client.add.AddToIndexPresenter;
import org.exoplatform.ide.git.client.branch.BranchPresenter;
import org.exoplatform.ide.git.client.clone.CloneRepositoryPresenter;
import org.exoplatform.ide.git.client.commit.CommitPresenter;
import org.exoplatform.ide.git.client.control.AddFilesControl;
import org.exoplatform.ide.git.client.control.BranchesControl;
import org.exoplatform.ide.git.client.control.CloneRepositoryControl;
import org.exoplatform.ide.git.client.control.CommitControl;
import org.exoplatform.ide.git.client.control.DeleteRepositoryControl;
import org.exoplatform.ide.git.client.control.FetchControl;
import org.exoplatform.ide.git.client.control.InitRepositoryControl;
import org.exoplatform.ide.git.client.control.MergeControl;
import org.exoplatform.ide.git.client.control.PullControl;
import org.exoplatform.ide.git.client.control.PushToRemoteControl;
import org.exoplatform.ide.git.client.control.RemoteControl;
import org.exoplatform.ide.git.client.control.RemotesControl;
import org.exoplatform.ide.git.client.control.RemoveFilesControl;
import org.exoplatform.ide.git.client.control.ResetFilesControl;
import org.exoplatform.ide.git.client.control.ResetToCommitControl;
import org.exoplatform.ide.git.client.control.ShowHistoryControl;
import org.exoplatform.ide.git.client.control.ShowProjectGitReadOnlyUrl;
import org.exoplatform.ide.git.client.control.ShowStatusControl;
import org.exoplatform.ide.git.client.delete.DeleteRepositoryCommandHandler;
import org.exoplatform.ide.git.client.fetch.FetchPresenter;
import org.exoplatform.ide.git.client.history.HistoryPresenter;
import org.exoplatform.ide.git.client.init.InitRepositoryPresenter;
import org.exoplatform.ide.git.client.init.ShowProjectGitReadOnlyUrlPresenter;
import org.exoplatform.ide.git.client.merge.MergePresenter;
import org.exoplatform.ide.git.client.pull.PullPresenter;
import org.exoplatform.ide.git.client.push.PushToRemotePresenter;
import org.exoplatform.ide.git.client.remote.RemotePresenter;
import org.exoplatform.ide.git.client.remove.RemoveFromIndexPresenter;
import org.exoplatform.ide.git.client.reset.ResetFilesPresenter;
import org.exoplatform.ide.git.client.reset.ResetToCommitPresenter;
import org.exoplatform.ide.git.client.status.StatusCommandHandler;

/**
 * Git extension to be added to IDE application.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Mar 22, 2011 12:53:29 PM anya $
 */
public class GitExtension extends Extension implements InitializeServicesHandler {

    public static final GitLocalizationConstant MESSAGES            = GWT.create(GitLocalizationConstant.class);

    public static final String                  GIT_REPOSITORY_PROP = "isGitRepository";

    public static final GitAutoBeanFactory      AUTO_BEAN_FACTORY   = GWT.create(GitAutoBeanFactory.class);

    /** @see org.exoplatform.ide.client.framework.module.Extension#initialize() */
    @Override
    public void initialize() {
        IDE.addHandler(InitializeServicesEvent.TYPE, this);

        // Add controls:
        IDE.getInstance().addControl(new InitRepositoryControl());
        IDE.getInstance().addControl(new CloneRepositoryControl());
        IDE.getInstance().addControl(new DeleteRepositoryControl());
        IDE.getInstance().addControl(new AddFilesControl());
        IDE.getInstance().addControl(new ResetFilesControl());
        IDE.getInstance().addControl(new ResetToCommitControl());
        IDE.getInstance().addControl(new RemoveFilesControl());
        IDE.getInstance().addControl(new CommitControl());
        IDE.getInstance().addControl(new BranchesControl());
        IDE.getInstance().addControl(new MergeControl());
        IDE.getInstance().addControl(new PushToRemoteControl());
        IDE.getInstance().addControl(new FetchControl());
        IDE.getInstance().addControl(new PullControl());
        IDE.getInstance().addControl(new RemoteControl());
        IDE.getInstance().addControl(new RemotesControl());

        IDE.getInstance().addControl(new ShowHistoryControl());
        IDE.getInstance().addControl(new ShowStatusControl());
        IDE.getInstance().addControl(new ShowProjectGitReadOnlyUrl());
        IDE.getInstance().addControlsFormatter(new GitControlsFormatter());


        // Create presenters:
        new CloneRepositoryPresenter();
        new InitRepositoryPresenter();
        new StatusCommandHandler();
        new AddToIndexPresenter();
        new RemoveFromIndexPresenter();
        new ResetFilesPresenter();
        new ResetToCommitPresenter();
        new RemotePresenter();

        new CommitPresenter();
        new PushToRemotePresenter();
        new BranchPresenter();
        new FetchPresenter();
        new PullPresenter();
        new HistoryPresenter();
        new DeleteRepositoryCommandHandler();
        new MergePresenter();

        new ShowProjectGitReadOnlyUrlPresenter();

    }

    /**
     * @see org.exoplatform.ide.client.framework.application.event.InitializeServicesHandler#onInitializeServices(org.exoplatform.ide
     *      .client.framework.application.event.InitializeServicesEvent)
     */
    @Override
    public void onInitializeServices(InitializeServicesEvent event) {
        new GitClientServiceImpl(Utils.getRestContext(), Utils.getWorkspaceName(), event.getLoader(), IDE.messageBus());
    }


}
