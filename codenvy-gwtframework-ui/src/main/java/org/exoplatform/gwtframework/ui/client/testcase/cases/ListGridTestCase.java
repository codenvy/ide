/**
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
 *
 */

package org.exoplatform.gwtframework.ui.client.testcase.cases;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;

import org.exoplatform.gwtframework.ui.client.CellTableResource;
import org.exoplatform.gwtframework.ui.client.SelectItemResource;
import org.exoplatform.gwtframework.ui.client.component.ListGrid;
import org.exoplatform.gwtframework.ui.client.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class ListGridTestCase extends TestCase {

    public static final SelectItemResource resource = GWT.create(SelectItemResource.class);

    private CellTableResource resources1 = GWT.create(CellTableResource.class);

    private class Employee {
        String name;

        boolean isMale;

        public Employee(String name, boolean isMale) {
            this.name = name;
            this.isMale = isMale;
        }
    }

    @Override
    public void draw() {
        final List<Employee> values = new ArrayList<Employee>();
        values.add(new Employee("Aaa", false));
        values.add(new Employee("bbb", true));
        values.add(new Employee("ccc", false));
        values.add(new Employee("ddd", false));
        values.add(new Employee("eee", true));
        values.add(new Employee("fff", true));
        values.add(new Employee("ccc1", false));
        values.add(new Employee("ddd1", false));
        values.add(new Employee("eee1", true));
        values.add(new Employee("fff1", true));
        values.add(new Employee("ccc2", false));
        values.add(new Employee("ddd2", false));
        values.add(new Employee("eee2", true));
        values.add(new Employee("fff2", true));

        final ListGrid<Employee> listGrid = new ListGrid<Employee>();
        listGrid.setValue(values);
        listGrid.setID("CELL_TABLE");
        NodeList<Element> elements = listGrid.getCellTable().getElement().getElementsByTagName("tbody");
        Element element = elements.getItem(0);
        element.addClassName(resources1.cellTableStyle().scrollTable());
        element.setAttribute("height", "250px");

        Column<Employee, Boolean> checkColumn = new Column<Employee, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(Employee object) {
                return object.isMale;
            }
        };
        listGrid.getCellTable().addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        listGrid.getCellTable().setColumnWidth(checkColumn, 40, Unit.PX);

        EditTextCell textCell = new EditTextCell();
        Column<Employee, String> firstNameColumn = new Column<Employee, String>(textCell) {
            @Override
            public String getValue(Employee object) {
                return object.name;
            }
        };
        listGrid.getCellTable().addColumn(firstNameColumn, "first name");
        listGrid.getCellTable().setColumnWidth(firstNameColumn, 100, Unit.PCT);

        firstNameColumn.setFieldUpdater(new FieldUpdater<ListGridTestCase.Employee, String>() {

            public void update(int index, Employee object, String value) {
                object.name = value;
            }
        });

        listGrid.addSelectionHandler(new SelectionHandler<ListGridTestCase.Employee>() {
            @Override
            public void onSelection(SelectionEvent<Employee> event) {
            }
        });

        listGrid.addDoubleClickHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                Window.alert("db click");
            }
        });

        testCasePanel().add(listGrid);

    }

}