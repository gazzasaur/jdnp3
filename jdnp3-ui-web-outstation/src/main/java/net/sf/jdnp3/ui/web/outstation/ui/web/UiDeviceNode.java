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
package net.sf.jdnp3.ui.web.outstation.ui.web;

import java.net.URLEncoder;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiDeviceNode {
	private Logger logger = LoggerFactory.getLogger(UiDeviceNode.class);
	
    private String station;
    private String device;
    
	public String getStation() {
		return station;
	}
	
	public void setStation(String station) {
		this.station = station;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public void submit() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(String.format("/device_exp.jsf?stationCode=%s&deviceCode=%s", URLEncoder.encode(station, "UTF-8"), URLEncoder.encode(device, "UTF-8")));
		} catch (Exception e) {
			logger.error("Cannot redirect to device.", e);
		}
	}
}