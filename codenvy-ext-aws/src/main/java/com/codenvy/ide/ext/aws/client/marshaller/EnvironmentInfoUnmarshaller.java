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
package com.codenvy.ide.ext.aws.client.marshaller;

import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.ext.aws.dto.client.DtoClientImpls;
import com.codenvy.ide.ext.aws.shared.beanstalk.EnvironmentInfo;
import com.codenvy.ide.rest.Unmarshallable;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Unmarshaller for environment info.
 *
 * @author <a href="mailto:vzhukovskii@codenvy.com">Vladislav Zhukovskii</a>
 * @version $Id: $
 */
public class EnvironmentInfoUnmarshaller implements Unmarshallable<EnvironmentInfo> {
    private DtoClientImpls.EnvironmentInfoImpl environmentInfo;

    /**
     * Create unmarshaller.
     *
     * @param environmentInfo
     */
    public EnvironmentInfoUnmarshaller(DtoClientImpls.EnvironmentInfoImpl environmentInfo) {
        this.environmentInfo = environmentInfo;
    }

    /** {@inheritDoc} */
    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        String text = response.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        JSONObject environmentObject = JSONParser.parseStrict(text).isObject();
        if (environmentObject == null) {
            return;
        }

        DtoClientImpls.EnvironmentInfoImpl dtoEnvironmentInfo = DtoClientImpls.EnvironmentInfoImpl.deserialize(text);
        environmentInfo.setSolutionStackName(dtoEnvironmentInfo.getSolutionStackName());
        environmentInfo.setVersionLabel(dtoEnvironmentInfo.getVersionLabel());
        environmentInfo.setApplicationName(dtoEnvironmentInfo.getApplicationName());
        environmentInfo.setDescription(dtoEnvironmentInfo.getDescription());
        environmentInfo.setCname(dtoEnvironmentInfo.getCname());
        environmentInfo.setCreated(dtoEnvironmentInfo.getCreated());
        environmentInfo.setEndpointUrl(dtoEnvironmentInfo.getEndpointUrl());
        environmentInfo.setHealth(dtoEnvironmentInfo.getHealth());
        environmentInfo.setStatus(dtoEnvironmentInfo.getStatus());
        environmentInfo.setTemplateName(dtoEnvironmentInfo.getTemplateName());
        environmentInfo.setUpdated(dtoEnvironmentInfo.getUpdated());
        environmentInfo.setId(dtoEnvironmentInfo.getId());
        environmentInfo.setName(dtoEnvironmentInfo.getName());
    }

    /** {@inheritDoc} */
    @Override
    public EnvironmentInfo getPayload() {
        return environmentInfo;
    }
}