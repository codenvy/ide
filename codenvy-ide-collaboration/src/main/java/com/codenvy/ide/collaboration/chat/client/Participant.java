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
package com.codenvy.ide.collaboration.chat.client;


import com.codenvy.ide.collaboration.dto.client.DtoClientImpls.UserDetailsImpl;

/**
 * Model object for a participant. This extends the
 * {@link UserDetailsImpl} class used for data transfer.
 *
 * @author <a href="mailto:evidolob@codenvy.com">Evgen Vidolob</a>
 * @version $Id:
 */
public class Participant extends UserDetailsImpl {

    private static final String COLOR_KEY = "__color";

    private static final String CLIENT_ID_KEY = "__client_id";

    protected Participant() {
    }

    public final String getColor() {
        return getStringField(COLOR_KEY);
    }

    public final void setColor(String color) {
        addField(COLOR_KEY, color);
    }

    public final String getClientId() {
        return getStringField(CLIENT_ID_KEY);
    }

    public final void setClientId(String clientId) {
        addField(CLIENT_ID_KEY, clientId);
    }

}