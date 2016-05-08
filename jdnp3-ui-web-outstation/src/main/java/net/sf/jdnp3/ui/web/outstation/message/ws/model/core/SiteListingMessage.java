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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.main.SiteDeviceList;

public class SiteListingMessage implements Message {
	private String type = "siteList";
	private List<SiteDeviceList> siteDeviceLists = new ArrayList<SiteDeviceList>();
	
	public String getType() {
		return type;
	}

	public List<SiteDeviceList> getSiteDeviceLists() {
		return siteDeviceLists;
	}

	public void setSiteDeviceLists(List<SiteDeviceList> siteDeviceLists) {
		this.siteDeviceLists = siteDeviceLists;
	}
}
