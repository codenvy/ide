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
package org.exoplatform.gwtframework.ui.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: CellTableResource.java Mar 11, 2011 11:33:27 AM vereshchaka $
 */
public interface CellTableResource extends CellTable.Resources {
    @Source({CellTable.Style.DEFAULT_CSS, "eXoStyle/skin/default/CellTable.css"})
    TableStyle cellTableStyle();

    @Source("eXoStyle/skin/default/images/listGrid/header.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource header();

    interface TableStyle extends CellTable.Style {
        String cellTableBox();

        String scrollTable();
    }

}