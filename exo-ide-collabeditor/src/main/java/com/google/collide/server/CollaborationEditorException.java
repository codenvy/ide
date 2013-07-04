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
package com.google.collide.server;

import com.codenvy.ide.dtogen.server.ServerErrorImpl;
import com.codenvy.ide.dtogen.shared.ServerError;


/**
 * Wraps any exception in collaboration editor at server side.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 */
@SuppressWarnings("serial")
public final class CollaborationEditorException extends RuntimeException {
    private final ServerError error;

    public CollaborationEditorException(Throwable cause) {
        super(cause);
        ServerErrorImpl myError = ServerErrorImpl.make();
        myError.setDetails(cause.getMessage());
        myError.setFailureReason(ServerError.FailureReason.UNKNOWN);
        this.error = myError;
    }

    public CollaborationEditorException(ServerError error) {
        this.error = error;
    }

    public ServerError getError() {
        return error;
    }
}