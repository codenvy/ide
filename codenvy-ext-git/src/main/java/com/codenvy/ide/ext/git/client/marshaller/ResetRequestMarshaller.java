/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package com.codenvy.ide.ext.git.client.marshaller;

import com.codenvy.ide.ext.git.shared.ResetRequest;
import com.codenvy.ide.rest.Marshallable;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Marshaller for reset files request in JSON format.
 *
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Apr 13, 2011 5:51:18 PM anya $
 */
public class ResetRequestMarshaller implements Marshallable, Constants {
    /** Reset request. */
    private ResetRequest resetRequest;

    /**
     * @param resetRequest
     *         reset request
     */
    public ResetRequestMarshaller(ResetRequest resetRequest) {
        this.resetRequest = resetRequest;
    }

    /** {@inheritDoc} */
    @Override
    public String marshal() {
        JSONObject jsonObject = new JSONObject();

        if (resetRequest.getCommit() != null) {
            jsonObject.put(COMMIT, new JSONString(resetRequest.getCommit()));
        }
        if (resetRequest.getType() != null) {
            jsonObject.put(TYPE, new JSONString(resetRequest.getType().name()));
        }
        return jsonObject.toString();
    }
}