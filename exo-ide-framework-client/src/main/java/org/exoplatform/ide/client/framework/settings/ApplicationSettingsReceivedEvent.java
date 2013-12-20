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
package org.exoplatform.ide.client.framework.settings;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.gwtframework.commons.exception.ServerExceptionEvent;

/**
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class ApplicationSettingsReceivedEvent extends ServerExceptionEvent<ApplicationSettingsReceivedHandler> {

    public static final GwtEvent.Type<ApplicationSettingsReceivedHandler> TYPE =
            new GwtEvent.Type<ApplicationSettingsReceivedHandler>();

    private ApplicationSettings applicationSettings;

    public ApplicationSettingsReceivedEvent(ApplicationSettings applicationSettings) {
        super(null);
        this.applicationSettings = applicationSettings;
    }

    @Override
    protected void dispatch(ApplicationSettingsReceivedHandler handler) {
        handler.onApplicationSettingsReceived(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ApplicationSettingsReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

}