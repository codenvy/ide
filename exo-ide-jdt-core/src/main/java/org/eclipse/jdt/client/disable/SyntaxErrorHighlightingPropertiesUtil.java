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
package org.eclipse.jdt.client.disable;

import com.codenvy.ide.client.util.logging.Log;
import com.google.collide.client.collaboration.CollaborationPropertiesUtil;
import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.vfs.client.VirtualFileSystem;
import org.exoplatform.ide.vfs.client.marshal.ItemUnmarshaller;
import org.exoplatform.ide.vfs.client.model.ItemWrapper;
import org.exoplatform.ide.vfs.client.model.ProjectModel;
import org.exoplatform.ide.vfs.shared.Property;
import org.exoplatform.ide.vfs.shared.PropertyImpl;

import java.util.Collections;

/**
 * Utils for operations with syntax error highlighting property.
 *
 * @author <a href="mailto:vsvydenko@codenvy.com">Valeriy Svydenko</a>
 */
public class SyntaxErrorHighlightingPropertiesUtil {
    public static final String SYNTAX_ERROR_HIGHLIGHTING = "javaSyntaxErrorHighlighting";

    public static boolean isSyntaxErrorHighlightingEnabled(ProjectModel project) {
        String value = project.getPropertyValue(SYNTAX_ERROR_HIGHLIGHTING);
        if (value == null) {
            //by default code assistant disabled
            return false;
        }


        return Boolean.valueOf(value);
    }

    /**
     * Updating syntax error highlighting property.
     *
     * @param project
     *         current project
     * @param isEnabled
     *         flag of a syntax error highlighting property
     */
    public static void updateSyntaxErrorHighlighting(final ProjectModel project, boolean isEnabled) {
        Property property = project.getProperty(SYNTAX_ERROR_HIGHLIGHTING);
        if (property == null) {
            property = new PropertyImpl(SYNTAX_ERROR_HIGHLIGHTING, "");
            project.getProperties().add(property);
        }

        property.setValue(Collections.singletonList(String.valueOf(isEnabled)));

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

    /**
     * Updating project properties.
     *
     * @param project
     *         current project
     */
    private static void updateProjectProperties(ProjectModel project) {
        try {
            VirtualFileSystem.getInstance().updateItem(project, null, new AsyncRequestCallback<ItemWrapper>() {
                @Override
                protected void onSuccess(ItemWrapper result) {
                    // ignore
                }

                @Override
                protected void onFailure(Throwable exception) {
                    Log.debug(SyntaxErrorHighlightingPropertiesUtil.class, exception);
                }
            });
        } catch (RequestException e) {
            Log.debug(CollaborationPropertiesUtil.class, e);
        }
    }
}