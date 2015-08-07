/**
 * Copyright 2015 Graeme Farquharson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jdnp3.ui.web.outstation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@RequestScoped
public class UiBinaryPointMenu {
    public void save() {
    	System.exit(1);
    }
	
	public MenuModel constructMenu(UiPoint uiPoint) {
//		DatabaseManager databaseManager = DatabaseManagerProvider.getDatabaseManager();
//		BinaryDataPoint binaryDataPoint = databaseManager.getBinaryDataPoints().get((int) uiPoint.getIndex());
//		
//		DefaultMenuItem createEvent = new DefaultMenuItem();
//		createEvent.setValue("Create Event");
//		createEvent.setIcon("ui-icon-calendar");
//		
//		Class<?>[] paramTypes = new Class<?>[0];
//		MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{uiBinaryPointMenu.performOutput}", null, paramTypes);
//
//		DefaultMenuItem staticDefaultMenuItem = new DefaultMenuItem();
//		staticDefaultMenuItem.setValue("DefaultValue");
//		staticDefaultMenuItem.setId("asdf");
//		staticDefaultMenuItem.setId("_" + RandomStringUtils.randomAlphabetic(30));
//		staticDefaultMenuItem.setIcon((binaryDataPoint.getStaticType().equals(BINARY_INPUT_STATIC_ANY) || binaryDataPoint.getStaticType().getGroup() != BINARY_INPUT_STATIC_ANY.getGroup()) ? "ui-icon-check" : "ui-icon-radio-on");
//		staticDefaultMenuItem.setActionExpression(methodExpression);
//
//		DefaultMenuItem staticPackedMenuItem = new DefaultMenuItem();
//		staticPackedMenuItem.setValue("Packed");
//		staticPackedMenuItem.setIcon(binaryDataPoint.getStaticType().equals(BINARY_INPUT_STATIC_PACKED) ? "ui-icon-check" : "ui-icon-radio-on");
//
//		DefaultMenuItem staticFlagsMenuItem = new DefaultMenuItem();
//		staticFlagsMenuItem.setValue("Flags");
//		staticFlagsMenuItem.setIcon(binaryDataPoint.getStaticType().equals(BINARY_INPUT_STATIC_FLAGS) ? "ui-icon-check" : "ui-icon-radio-on");
//
//		DefaultSubMenu staticSubMenu = new DefaultSubMenu("Default Static Type", "ui-icon-wrench");
//		staticSubMenu.addElement(staticDefaultMenuItem);
//		staticSubMenu.addElement(staticPackedMenuItem);
//		staticSubMenu.addElement(staticFlagsMenuItem);
//
//		DefaultMenuItem eventDefaultMenuItem = new DefaultMenuItem();
//		eventDefaultMenuItem.setValue("Default");
//		eventDefaultMenuItem.setIcon((binaryDataPoint.getEventType().equals(BINARY_INPUT_EVENT_ANY) || binaryDataPoint.getEventType().getGroup() != BINARY_INPUT_EVENT_ANY.getGroup()) ? "ui-icon-check" : "ui-icon-radio-on");
//		
//		DefaultMenuItem eventWithoutTimeMenuItem = new DefaultMenuItem();
//		eventWithoutTimeMenuItem.setValue("Without Time");
//		eventWithoutTimeMenuItem.setIcon(binaryDataPoint.getEventType().equals(BINARY_INPUT_EVENT_WITHOUT_TIME) ? "ui-icon-check" : "ui-icon-radio-on");
//		
//		DefaultMenuItem eventAbsoluteTimeTimeMenuItem = new DefaultMenuItem();
//		eventAbsoluteTimeTimeMenuItem.setValue("Absolute Time");
//		eventAbsoluteTimeTimeMenuItem.setIcon(binaryDataPoint.getEventType().equals(BINARY_INPUT_EVENT_ABSOLUTE_TIME) ? "ui-icon-check" : "ui-icon-radio-on");
//		
//		DefaultMenuItem eventRelativeTimeTimeMenuItem = new DefaultMenuItem();
//		eventRelativeTimeTimeMenuItem.setValue("Relative Time");
//		eventRelativeTimeTimeMenuItem.setIcon(binaryDataPoint.getEventType().equals(BINARY_INPUT_EVENT_RELATIVE_TIME) ? "ui-icon-check" : "ui-icon-radio-on");
//
//		DefaultSubMenu eventSubMenu = new DefaultSubMenu("Default Event Type", "ui-icon-star");
//		eventSubMenu.addElement(eventDefaultMenuItem);
//		eventSubMenu.addElement(eventWithoutTimeMenuItem);
//		eventSubMenu.addElement(eventAbsoluteTimeTimeMenuItem);
//		eventSubMenu.addElement(eventRelativeTimeTimeMenuItem);
//		
//		DefaultMenuItem class1MenuItem = new DefaultMenuItem();
//		class1MenuItem.setValue("Class 1");
//		class1MenuItem.setIcon(binaryDataPoint.getEventClass() == 1 ? "ui-icon-check" : "ui-icon-radio-on");
//
//		DefaultMenuItem class2MenuItem = new DefaultMenuItem();
//		class2MenuItem.setValue("Class 2");
//		class2MenuItem.setIcon(binaryDataPoint.getEventClass() == 2 ? "ui-icon-check" : "ui-icon-radio-on");
//
//		DefaultMenuItem class3MenuItem = new DefaultMenuItem();
//		class3MenuItem.setValue("Class 3");
//		class3MenuItem.setIcon(binaryDataPoint.getEventClass() == 3 ? "ui-icon-check" : "ui-icon-radio-on");
//		
//		DefaultSubMenu classSubMenu = new DefaultSubMenu("Event Class", "ui-icon-link");
//		classSubMenu.addElement(class1MenuItem);
//		classSubMenu.addElement(class2MenuItem);
//		classSubMenu.addElement(class3MenuItem);
//		
		MenuModel menuModel = new DefaultMenuModel();
//		menuModel.addElement(createEvent);
//		menuModel.addElement(new DefaultSeparator());
//		menuModel.addElement(staticSubMenu);
//		menuModel.addElement(eventSubMenu);
//		menuModel.addElement(classSubMenu);
		
		return menuModel;
	}
}
