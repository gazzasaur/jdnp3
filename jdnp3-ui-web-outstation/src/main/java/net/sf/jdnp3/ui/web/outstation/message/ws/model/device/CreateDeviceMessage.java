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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.device;

import net.sf.jdnp3.ui.web.outstation.main.ExtendedConfiguration;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class CreateDeviceMessage implements Message {
	private String type = "createDevice";
	private String site = "";
	private String device = "";
	private int primaryAddress = 0;
	private String deviceFactory = "";

	private ExtendedConfiguration extendedConfiguration = new ExtendedConfiguration();
	
	public String getType() {
		return type;
	}

	public int getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(int primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public String getDeviceFactory() {
		return deviceFactory;
	}

	public void setDeviceFactory(String deviceFactory) {
		this.deviceFactory = deviceFactory;
	}

	public ExtendedConfiguration getExtendedConfiguration() {
		return extendedConfiguration;
	}

	public void setExtendedConfiguration(ExtendedConfiguration extendedConfiguration) {
		this.extendedConfiguration = extendedConfiguration;
	}

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
}
