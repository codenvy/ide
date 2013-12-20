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
package org.exoplatform.ide.extension.php.client;

import com.google.gwt.http.client.Response;

import org.exoplatform.gwtframework.commons.rest.Unmarshallable;

/**
 * Deserializer for response's body.
 * 
 * @author <a href="mailto:azatsarynnyy@codenvy.com">Artem Zatsarynnyy</a>
 * @version $Id: StringUnmarshaller.java Apr 17, 2013 4:21:17 PM azatsarynnyy $
 *
 */
public class StringUnmarshaller implements Unmarshallable<StringBuilder> {

    protected StringBuilder builder;

    public StringUnmarshaller(StringBuilder builder) {
        this.builder = builder;
    }

    /** @see org.exoplatform.gwtframework.commons.rest.Unmarshallable#unmarshal(com.google.gwt.http.client.Response) */
    @Override
    public void unmarshal(Response response) {
        builder.append(response.getText());
    }

    @Override
    public StringBuilder getPayload() {
        return builder;
    }
}