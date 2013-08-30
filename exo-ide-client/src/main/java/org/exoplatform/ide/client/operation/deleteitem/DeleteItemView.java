/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.ide.client.operation.deleteitem;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.*;

import org.exoplatform.gwtframework.ui.client.component.ImageButton;
import org.exoplatform.gwtframework.ui.client.component.Label;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.IDEImageBundle;
import org.exoplatform.ide.client.Images;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;
import org.exoplatform.ide.client.framework.ui.impl.ViewType;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class DeleteItemView extends ViewImpl implements
                                             org.exoplatform.ide.client.operation.deleteitem.DeleteItemsPresenter.Display {

    private static final String ID = "ideDeleteItemsView";

    public static final int DEFAULT_WIDTH = 500;

    public static final int DEFAULT_HEIGHT = 150;

    public static final String ID_OK_BUTTON = "ideDeleteItemFormOkButton";

    public static final String ID_CANCEL_BUTTON = "ideDeleteItemFormCancelButton";

    private static final String TITLE = IDE.NAVIGATION_CONSTANT.deleteItemTitle();

    private HorizontalPanel infoLayout;

    private ImageButton deleteButton;

    private ImageButton cancelButton;

    private Label promptField;

    public DeleteItemView() {
        super(ID, ViewType.MODAL, TITLE, new Image(IDEImageBundle.INSTANCE.delete()), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setCloseOnEscape(true);

        VerticalPanel mainLayout = new VerticalPanel();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        mainLayout.setSpacing(10);
        mainLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        add(mainLayout);

        infoLayout = new HorizontalPanel();
        infoLayout.setWidth("100%");
        infoLayout.setHeight(32 + "px");
        infoLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        createImage();
        createPromptField();

        mainLayout.add(infoLayout);
        mainLayout.add(createButtonsLayout());
    }

    private void createImage() {
        Image image = new Image(Images.Dialogs.ASK);
        image.setWidth(32 + "px");
        image.setHeight(32 + "px");
        infoLayout.add(image);
        infoLayout.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
    }

    private void createPromptField() {
        promptField = new Label();
        promptField.setIsHTML(true);
        infoLayout.add(promptField);
        infoLayout.setCellHorizontalAlignment(promptField, HasHorizontalAlignment.ALIGN_LEFT);
    }

    private HorizontalPanel createButtonsLayout() {
        HorizontalPanel buttonsLayout = new HorizontalPanel();
        buttonsLayout.setHeight(22 + "px");
        buttonsLayout.setSpacing(5);

        deleteButton = new ImageButton(IDE.IDE_LOCALIZATION_CONSTANT.yesButton());
        deleteButton.setButtonId(ID_OK_BUTTON);
        deleteButton.setWidth("90px");
        deleteButton.setHeight("22px");
        deleteButton.setImage(new Image(Images.Buttons.YES));

        cancelButton = new ImageButton(IDE.IDE_LOCALIZATION_CONSTANT.noButton());
        cancelButton.setButtonId(ID_CANCEL_BUTTON);
        cancelButton.setWidth("90px");
        cancelButton.setHeight("22px");
        cancelButton.setImage(new Image(Images.Buttons.NO));

        buttonsLayout.add(deleteButton);
        buttonsLayout.add(cancelButton);

        return buttonsLayout;
    }

    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    public HasClickHandlers getDeleteButton() {
        return deleteButton;
    }

    @Override
    public HasValue<String> getPromptField() {
        return promptField;
    }

}