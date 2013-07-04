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
package org.exoplatform.ide.extension.php.client.logs;

import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.exception.ExceptionThrownEvent;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.extension.php.client.PhpRuntimeExtension;
import org.exoplatform.ide.extension.php.client.PhpRuntimeService;
import org.exoplatform.ide.extension.php.client.StringUnmarshaller;
import org.exoplatform.ide.extension.php.client.run.event.ApplicationStartedEvent;
import org.exoplatform.ide.extension.php.client.run.event.ApplicationStartedHandler;
import org.exoplatform.ide.extension.php.client.run.event.ApplicationStoppedEvent;
import org.exoplatform.ide.extension.php.client.run.event.ApplicationStoppedHandler;
import org.exoplatform.ide.extension.php.shared.ApplicationInstance;

/**
 * 
 * @author <a href="mailto:azatsarynnyy@codenvy.com">Artem Zatsarynnyy</a>
 * @version $Id: LogsHandler.java Apr 17, 2013 4:01:33 PM azatsarynnyy $
 *
 */
public class LogsHandler implements ShowLogsHandler, ApplicationStartedHandler, ApplicationStoppedHandler {
    private ApplicationInstance runApplication;

    public LogsHandler() {
        IDE.getInstance().addControl(new ShowLogsControl());

        IDE.addHandler(ShowLogsEvent.TYPE, this);
        IDE.addHandler(ApplicationStartedEvent.TYPE, this);
        IDE.addHandler(ApplicationStoppedEvent.TYPE, this);
    }

    /** @see org.exoplatform.ide.extension.php.client.logs.ShowLogsHandler#onShowLogs(org.exoplatform.ide.extension.php.client.logs
     * .ShowLogsEvent) */
    @Override
    public void onShowLogs(ShowLogsEvent event) {
        if (runApplication != null) {
            getLogs();
        } else {
            Dialogs.getInstance().showInfo(PhpRuntimeExtension.PHP_LOCALIZATION.noRunningApplication());
        }
    }

    private void getLogs() {
        try {
            PhpRuntimeService.getInstance().getLogs(runApplication.getName(),
                                                       new AsyncRequestCallback<StringBuilder>(
                                                               new StringUnmarshaller(new StringBuilder())) {

                                                           @Override
                                                           protected void onSuccess(StringBuilder result) {
                                                               IDE.fireEvent(new OutputEvent("<pre>" + result.toString() + "</pre>",
                                                                                             Type.OUTPUT));
                                                           }

                                                           @Override
                                                           protected void onFailure(Throwable exception) {
                                                               IDE.fireEvent(new ExceptionThrownEvent(exception));
                                                           }
                                                       });
        } catch (RequestException e) {
            IDE.fireEvent(new ExceptionThrownEvent(e));
        }
    }

    /** @see org.exoplatform.ide.extension.php.client.run.event.ApplicationStoppedHandler#onApplicationStopped(org.exoplatform.ide.extension.php.client.run.event.ApplicationStoppedEvent) */
    @Override
    public void onApplicationStopped(ApplicationStoppedEvent event) {
        this.runApplication = null;
    }

    /** @see org.exoplatform.ide.extension.php.client.run.event.ApplicationStartedHandler#onApplicationStarted(org.exoplatform.ide.extension.php.client.run.event.ApplicationStartedEvent) */
    @Override
    public void onApplicationStarted(ApplicationStartedEvent event) {
        this.runApplication = event.getApplication();
    }
}