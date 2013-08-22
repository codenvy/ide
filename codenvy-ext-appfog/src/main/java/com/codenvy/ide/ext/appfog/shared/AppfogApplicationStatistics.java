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
package com.codenvy.ide.ext.appfog.shared;

import com.codenvy.ide.json.JsonArray;

/** @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a> */
public interface AppfogApplicationStatistics {
    String getName();

    String getState();

    String getHost();

    int getPort();

    JsonArray<String> getUris();

    String getUptime();

    int getCpuCores();

    double getCpu();

    int getMem();

    int getDisk();

    int getMemLimit();

    int getDiskLimit();
}