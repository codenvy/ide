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
package com.codenvy.ide.client.marshaller;

import com.codenvy.ide.client.DtoClientImpls;
import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.json.js.Jso;
import com.codenvy.ide.rest.Unmarshallable;
import com.codenvy.ide.api.user.User;
import com.google.gwt.http.client.Response;

/**
 * Unmarshaller for User's information.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public class UserUnmarshaller implements Unmarshallable<User> {

    private DtoClientImpls.UserImpl user;

    /**
     * Create unmarshaller.
     *
     * @param user
     */
    public UserUnmarshaller(DtoClientImpls.UserImpl user) {
        this.user = user;
    }

    /** {@inheritDoc} */
    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        DtoClientImpls.UserImpl user = Jso.deserialize(response.getText()).cast();

        this.user.setUserId(user.getUserId());
        this.user.setProfileAttributes(user.getProfileAttributes());
    }

    /** {@inheritDoc} */
    @Override
    public User getPayload() {
        return user;
    }
}