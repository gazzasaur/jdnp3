/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.database.core;

import java.util.List;

import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;

public class GlobalDatabaseListenerAdaptor implements DatabaseListener {
	private String site;
	private String device;
	private GlobalDatabaseListener databaseListener;

	public GlobalDatabaseListenerAdaptor(String site, String device, GlobalDatabaseListener databaseListener) {
		this.site = site;
		this.device = device;
		this.databaseListener = databaseListener;
	}
	
	public String getSite() {
		return site;
	}

	public String getDevice() {
		return device;
	}

	public GlobalDatabaseListener getDatabaseListener() {
		return databaseListener;
	}
	
	@Override
	public void modelChanged() {
		// TODO Only support value changed on global listeners for now
	}

	@Override
	public void valueChanged(DataPoint dataPoint) {
		databaseListener.valueChanged(site, device, dataPoint);
	}

	@Override
	public void bindingsChanged(List<OutstationBinding> outstationBindings) {
		// TODO Only support value changed on global listeners for now
	}
}
