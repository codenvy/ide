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
package com.codenvy.ide.ext.tutorials.server;

import com.codenvy.api.vfs.server.MountPoint;
import com.codenvy.api.vfs.server.VirtualFile;
import com.codenvy.api.vfs.server.VirtualFileSystemProvider;
import com.codenvy.api.vfs.server.VirtualFileSystemRegistry;
import com.codenvy.api.vfs.server.exceptions.VirtualFileSystemException;
import com.codenvy.api.vfs.shared.PropertyFilter;
import com.codenvy.api.vfs.shared.dto.Property;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * RESTful service for creating 'Tutorial' projects.
 *
 * @author Artem Zatsarynnyy
 */
@Path("create-tutorial/{ws-name}")
public class CreateProjectFromTemplateService {
    @Inject
    private VirtualFileSystemRegistry vfsRegistry;

    @Inject
    @Named("tutorials-url")
    private String                    baseUrl;

    /**
     * Create 'Notification tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("notification")
    @POST
    public void createNotificationTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/notification-api-tutorial.zip");
    }

    /**
     * Create 'Action tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("action")
    @POST
    public void createActionTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/action-api-tutorial.zip");
    }

    /**
     * Create 'Wizard tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("wizard")
    @POST
    public void createWizardTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/wizard-api-tutorial.zip");
    }

    /**
     * Create 'New project wizard tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("newproject")
    @POST
    public void createNewProjectWizardTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/new-project-wizard-tutorial.zip");
    }

    /**
     * Create 'New resource wizard tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("newresource")
    @POST
    public void createNewResourceWizardTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/new-resource-wizard-tutorial.zip");
    }

    /**
     * Create 'Parts tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("parts")
    @POST
    public void createPartsTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/parts-api-tutorial.zip");
    }

    /**
     * Create 'Editor tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("editor")
    @POST
    public void createEditorTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/editor-api-tutorial.zip");
    }

    /**
     * Create 'Editor tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("wysiwyg")
    @POST
    public void createWysiwygEditorTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/wysiwyg-editor-tutorial.zip");
    }

    /**
     * Create 'GIN tutorial' project.
     *
     * @param vfsId
     *         identifier of virtual file system
     * @param name
     *         name of the newly created project
     * @param properties
     *         properties to set to project
     * @throws VirtualFileSystemException
     *         if any error occurred in VFS
     * @throws IOException
     *         if any error occurred while input-output operations
     */
    @Path("gin")
    @POST
    public void createGinTutorialProject(@QueryParam("vfsid") String vfsId, @QueryParam("name") String name)
            throws VirtualFileSystemException, IOException {
        unzipTemplate(vfsId, name, baseUrl + "/gin-tutorial.zip");
    }

    private void unzipTemplate(String vfsId, String projectName, String templatePath) throws VirtualFileSystemException, IOException {
        VirtualFileSystemProvider provider = vfsRegistry.getProvider(vfsId);
        MountPoint mountPoint = provider.getMountPoint(false);
        VirtualFile root = mountPoint.getRoot();
        VirtualFile projectFolder = root.getChild(projectName);
        try (InputStream templateStream = new FileInputStream(new File(templatePath))) {
            projectFolder.unzip(templateStream, true);
        }
    }
}