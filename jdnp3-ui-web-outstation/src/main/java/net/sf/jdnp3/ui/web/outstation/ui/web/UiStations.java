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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;

@ManagedBean
@ViewScoped
public class UiStations {
	private Logger logger = LoggerFactory.getLogger(UiStations.class);
	
	private String station = "";
	private String device = "";
	
	public int getStationCount() {
		return DeviceProvider.getStationCount();
	}
	
	public int getDeviceCount() {
		return DeviceProvider.getDeviceCount();
	}
	
	public int getDataLinkCount() {
		return DataLinkManagerProvider.getDataLinkManagerCount();
	}

	public int getDataLinkConnectionCount() {
		return DataLinkManagerProvider.getDataLinkConnectionCount();
	}

	public int getDataLinkBindingCount() {
		return DataLinkManagerProvider.getDataLinkBindingCount();
	}

	public List<String> getStations() {
		List<String> stationNames = DeviceProvider.getStationNames();
		Collections.sort(stationNames);
		return stationNames;
	}

	public List<String> getDevices() {
		if (station != null && !station.isEmpty()) {
			return DeviceProvider.getDeviceNames(station);
		}
		return new ArrayList<String>();
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		device = "";
		this.station = station;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public boolean isEnabled() {
		return station != null && device != null && this.getStations().contains(station) && this.getDevices().contains(device);
	}
	
	public void submit() {
		if (!this.isEnabled()) {
			return;
		}
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(String.format("/device.jsf?stationCode=%s&deviceCode=%s", URLEncoder.encode(station, "UTF-8"), URLEncoder.encode(device, "UTF-8")));
		} catch (Exception e) {
			logger.error("Cannot redirect to device page.", e);
		}
	}
}