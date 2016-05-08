/**
 * Copyright 2016 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.main;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.ui.web.outstation.channel.TransportBindingItem;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;

public class OutstationDevice {
	private String site = "";
	private String device = "";
	private Outstation outstation;
	private DatabaseManager databaseManager;
	private List<TransportBindingItem> transportBindings = new ArrayList<>();
	
	public String getSite() {
		return site;
	}
	
	public void setSite(String site) {
		this.site = site;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Outstation getOutstation() {
		if (outstation == null) {
			throw new IllegalStateException("No outstation has been assigned.");
		}
		return outstation;
	}

	public void setOutstation(Outstation outstation) {
		this.outstation = outstation;
	}

	public DatabaseManager getDatabaseManager() {
		if (outstation == null) {
			throw new IllegalStateException("No database manager has been assigned.");
		}
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	public void addTransportBinding(TransportBindingItem dataLinkBinding) {
		transportBindings.add(dataLinkBinding);
	}
	
	public void unbind() {
		for (TransportBindingItem transportBindingItem : transportBindings) {
			transportBindingItem.unbind();
		}
		transportBindings.clear();
	}
}
