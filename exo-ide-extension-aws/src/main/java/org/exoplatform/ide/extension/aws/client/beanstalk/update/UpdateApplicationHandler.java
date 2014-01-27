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
package org.exoplatform.ide.extension.aws.client.beanstalk.update;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link UpdateApplicationEvent} event.
 *
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: Sep 19, 2012 4:31:55 PM anya $
 */
public interface UpdateApplicationHandler extends EventHandler {
    /**
     * Perform actions, when user tries to update application.
     *
     * @param event
     */
    void onUpdateApplication(UpdateApplicationEvent event);
}