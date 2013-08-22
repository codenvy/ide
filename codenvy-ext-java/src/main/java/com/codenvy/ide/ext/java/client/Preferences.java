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
package com.codenvy.ide.ext.java.client;

import com.google.gwt.storage.client.Storage;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: 4:30:50 PM Mar 29, 2012 evgen $
 */
public class Preferences {

    /**
     * A named preference that stores the content assist LRU history
     * <p>
     * Value is an JSON encoded version of the history.
     * </p>
     */
    public static final String CODEASSIST_LRU_HISTORY = "content_assist_lru_history_";

    public static final String QUALIFIED_TYPE_NAMEHISTORY = "Qualified_Type_Name_History_";

    private Storage storage;

    private boolean supported = false;

    /**
     *
     */
    public Preferences() {
        if (Storage.isSupported()) {
            storage = Storage.getLocalStorageIfSupported();
            supported = true;
        }

    }

    /**
     * @param key
     * @param string
     */
    public void setValue(String key, String string) {
        if (supported)
            storage.setItem(key, string);
    }

    /**
     * @param key
     * @return
     */
    public String getString(String key) {
        if (supported)
            return storage.getItem(key);
        return "";
    }

}