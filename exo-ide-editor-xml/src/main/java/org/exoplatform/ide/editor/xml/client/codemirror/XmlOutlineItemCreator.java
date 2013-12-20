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
package org.exoplatform.ide.editor.xml.client.codemirror;

import com.google.gwt.resources.client.ImageResource;

import org.exoplatform.ide.client.framework.outline.OutlineItemCreatorImpl;
import org.exoplatform.ide.editor.api.codeassitant.TokenBeenImpl;
import org.exoplatform.ide.editor.xml.client.XmlEditorExtension;

/**
 * @author <a href="mailto:dnochevnov@exoplatform.com">Dmytro Nochevnov</a>
 * @version $Id
 */
public class XmlOutlineItemCreator extends OutlineItemCreatorImpl {

    @Override
    public ImageResource getTokenIcon(TokenBeenImpl token) {
        switch (token.getType()) {
            case TAG:
                return XmlEditorExtension.RESOURCES.tag();

            case CDATA:
                return XmlEditorExtension.RESOURCES.cdata();

            default:
                return null;
        }
    }

    @Override
    public String getTokenDisplayTitle(TokenBeenImpl token) {
        return token.getName();
    }

}