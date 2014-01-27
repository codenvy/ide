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
package org.exoplatform.ide.client.framework.application.event;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.gwtframework.commons.loader.Loader;
import org.exoplatform.ide.client.framework.configuration.IDEConfiguration;

/**
 * Called after success configuration loading (IDEConficuration, UserInfo, ApplicationSettings)
 * <p/>
 *
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $Id: $
 */

public class InitializeServicesEvent extends GwtEvent<InitializeServicesHandler> {

    public static final GwtEvent.Type<InitializeServicesHandler> TYPE = new GwtEvent.Type<InitializeServicesHandler>();

    private IDEConfiguration applicationConfiguration;

    private Loader loader;

    public InitializeServicesEvent(IDEConfiguration applicationConfiguration, Loader loader) {
        this.applicationConfiguration = applicationConfiguration;
        this.loader = loader;
    }

    public IDEConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public Loader getLoader() {
        return loader;
    }

    @Override
    protected void dispatch(InitializeServicesHandler handler) {
        handler.onInitializeServices(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<InitializeServicesHandler> getAssociatedType() {
        return TYPE;
    }

}