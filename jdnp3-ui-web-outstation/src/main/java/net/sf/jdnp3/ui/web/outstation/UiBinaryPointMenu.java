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

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSeparator;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@RequestScoped
public class UiBinaryPointMenu {
	public MenuModel constructMenu(UiPoint uiPoint) {
		DefaultMenuItem createEvent = new DefaultMenuItem();
		createEvent.setValue("Create Event");
		createEvent.setIcon("ui-icon-calendar");

		DefaultMenuItem staticPackedMenuItem = new DefaultMenuItem();
		staticPackedMenuItem.setValue("Packed");
		staticPackedMenuItem.setIcon("ui-icon-radio-on");

		DefaultMenuItem staticFlagsMenuItem = new DefaultMenuItem();
		staticFlagsMenuItem.setValue("Flags");
		staticFlagsMenuItem.setIcon("ui-icon-radio-on");

		DefaultSubMenu staticSubMenu = new DefaultSubMenu("Default Static Type", "ui-icon-wrench");
		staticSubMenu.addElement(staticPackedMenuItem);
		staticSubMenu.addElement(staticFlagsMenuItem);

		DefaultMenuItem eventWithoutTimeMenuItem = new DefaultMenuItem();
		eventWithoutTimeMenuItem.setValue("Without Time");
		eventWithoutTimeMenuItem.setIcon(uiPoint.getDnpClass().equals("dummy") ? "ui-icon-check" : "ui-icon-radio-on");
		
		DefaultSubMenu eventSubMenu = new DefaultSubMenu("Default Event Type", "ui-icon-star");
		eventSubMenu.addElement(eventWithoutTimeMenuItem);
		
		DefaultMenuItem class1MenuItem = new DefaultMenuItem();
		class1MenuItem.setValue("Class 1");
		class1MenuItem.setIcon(uiPoint.getDnpClass().equals("Class1") ? "ui-icon-check" : "ui-icon-radio-on");

		DefaultMenuItem class2MenuItem = new DefaultMenuItem();
		class2MenuItem.setValue("Class 2");
		class2MenuItem.setIcon(uiPoint.getDnpClass().equals("Class2") ? "ui-icon-check" : "ui-icon-radio-on");

		DefaultMenuItem class3MenuItem = new DefaultMenuItem();
		class3MenuItem.setValue("Class 3");
		class3MenuItem.setIcon(uiPoint.getDnpClass().equals("Class3") ? "ui-icon-check" : "ui-icon-radio-on");
		
		DefaultSubMenu classSubMenu = new DefaultSubMenu("Event Class", "ui-icon-link");
		classSubMenu.addElement(class1MenuItem);
		classSubMenu.addElement(class2MenuItem);
		classSubMenu.addElement(class3MenuItem);
		
		MenuModel menuModel = new DefaultMenuModel();
		menuModel.addElement(createEvent);
		menuModel.addElement(new DefaultSeparator());
		menuModel.addElement(staticSubMenu);
		menuModel.addElement(eventSubMenu);
		menuModel.addElement(classSubMenu);
		
		return menuModel;
	}
}
