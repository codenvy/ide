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
package com.codenvy.ide.ext.git.client.pull;

import com.codenvy.ide.annotations.NotNull;
import com.codenvy.ide.ext.git.client.GitResources;
import com.codenvy.ide.ext.git.client.GitLocalizationConstant;
import com.codenvy.ide.ext.git.shared.Remote;
import com.codenvy.ide.json.JsonArray;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The implementation of {@link PullView}.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@Singleton
public class PullViewImpl extends DialogBox implements PullView {
    interface PullViewImplUiBinder extends UiBinder<Widget, PullViewImpl> {
    }

    private static PullViewImplUiBinder ourUiBinder = GWT.create(PullViewImplUiBinder.class);

    @UiField
    ListBox                   repository;
    @UiField
    ListBox                   localBranch;
    @UiField
    ListBox                   remoteBranch;
    @UiField
    com.codenvy.ide.ui.Button btnPull;
    @UiField
    com.codenvy.ide.ui.Button btnCancel;
    @UiField(provided = true)
    final   GitResources            res;
    @UiField(provided = true)
    final   GitLocalizationConstant locale;
    private ActionDelegate          delegate;

    /**
     * Create view.
     *
     * @param resources
     * @param locale
     */
    @Inject
    protected PullViewImpl(GitResources resources, GitLocalizationConstant locale) {
        this.res = resources;
        this.locale = locale;

        Widget widget = ourUiBinder.createAndBindUi(this);

        this.setText(locale.pullTitle());
        this.setWidget(widget);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getRepositoryName() {
        int index = repository.getSelectedIndex();
        return index != -1 ? repository.getItemText(index) : "";
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getRepositoryUrl() {
        int index = repository.getSelectedIndex();
        return repository.getValue(index);
    }

    /** {@inheritDoc} */
    @Override
    public void setRepositories(@NotNull JsonArray<Remote> repositories) {
        this.repository.clear();
        for (int i = 0; i < repositories.size(); i++) {
            Remote repository = repositories.get(i);
            this.repository.addItem(repository.getName(), repository.getUrl());
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getLocalBranch() {
        int index = localBranch.getSelectedIndex();
        return index != -1 ? localBranch.getItemText(index) : "";
    }

    /** {@inheritDoc} */
    @Override
    public void setLocalBranches(@NotNull JsonArray<String> branches) {
        this.localBranch.clear();
        for (int i = 0; i < branches.size(); i++) {
            String branch = branches.get(i);
            this.localBranch.addItem(branch);
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getRemoteBranch() {
        int index = remoteBranch.getSelectedIndex();
        return index != -1 ? remoteBranch.getItemText(index) : "";
    }

    /** {@inheritDoc} */
    @Override
    public void setRemoteBranches(@NotNull JsonArray<String> branches) {
        this.remoteBranch.clear();
        for (int i = 0; i < branches.size(); i++) {
            String branch = branches.get(i);
            this.remoteBranch.addItem(branch);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setEnablePullButton(boolean enabled) {
        btnPull.setEnabled(enabled);
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        this.hide();
    }

    /** {@inheritDoc} */
    @Override
    public void showDialog() {
        this.center();
        this.show();
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("btnPull")
    public void onPullClicked(ClickEvent event) {
        delegate.onPullClicked();
    }

    @UiHandler("btnCancel")
    public void onCancelClicked(ClickEvent event) {
        delegate.onCancelClicked();
    }
}