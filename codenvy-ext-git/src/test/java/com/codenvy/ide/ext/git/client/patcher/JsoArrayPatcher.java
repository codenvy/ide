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
package com.codenvy.ide.ext.git.client.patcher;

import com.codenvy.ide.json.JsonCollections;
import com.codenvy.ide.json.js.JsoArray;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for JsoArray class. Replace native method into JsonArray.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
@PatchClass(JsoArray.class)
public class JsoArrayPatcher<T> {

    /** Patch create method. */
    @PatchMethod
    public static JsoArray create(JsoArray jsoArray) {
        // TODO This doesn't work now.
        return (JsoArray)JsonCollections.createArray();
    }
}