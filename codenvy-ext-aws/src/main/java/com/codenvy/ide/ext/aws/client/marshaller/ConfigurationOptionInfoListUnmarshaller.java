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
package com.codenvy.ide.ext.aws.client.marshaller;

import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.ext.aws.dto.client.DtoClientImpls;
import com.codenvy.ide.ext.aws.shared.beanstalk.ConfigurationOptionInfo;
import com.codenvy.ide.json.JsonArray;
import com.codenvy.ide.rest.Unmarshallable;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * @author <a href="mailto:vzhukovskii@codenvy.com">Vladislav Zhukovskii</a>
 * @version $Id: $
 */
public class ConfigurationOptionInfoListUnmarshaller implements Unmarshallable<JsonArray<ConfigurationOptionInfo>> {
    JsonArray<ConfigurationOptionInfo> configurationOptionInfoJsonArray;

    public ConfigurationOptionInfoListUnmarshaller(
            JsonArray<ConfigurationOptionInfo> configurationOptionInfoJsonArray) {
        this.configurationOptionInfoJsonArray = configurationOptionInfoJsonArray;
    }

    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        String text = response.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        JSONArray jsonArray = JSONParser.parseStrict(text).isArray();
        if (jsonArray == null) {
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.get(i).isObject();
            String value = object.isObject().toString();

            DtoClientImpls.ConfigurationOptionInfoImpl dtoConfiguration = DtoClientImpls.ConfigurationOptionInfoImpl.deserialize(value);
            configurationOptionInfoJsonArray.add(dtoConfiguration);
        }
    }

    @Override
    public JsonArray<ConfigurationOptionInfo> getPayload() {
        return configurationOptionInfoJsonArray;
    }
}
