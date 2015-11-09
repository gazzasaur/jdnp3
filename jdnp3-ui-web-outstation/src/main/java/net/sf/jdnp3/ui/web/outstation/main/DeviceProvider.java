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
package net.sf.jdnp3.ui.web.outstation.main;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class DeviceProvider {
	private static Map<String, Map<String, OutstationDevice>> devices = new HashMap<>();

	public synchronized static boolean exists(String stationCode, String deviceCode) {
		return devices.containsKey(stationCode) && devices.get(stationCode).containsKey(deviceCode);
	}

	public synchronized static int getStationCount() {
		return devices.size();
	}

	public synchronized static int getDeviceCount() {
		int total = 0;
		Collection<Map<String, OutstationDevice>> values = devices.values();
		for (Map<String, OutstationDevice> map : values) {
			total += map.size();
		}
		return total;
	}
	
	public synchronized static OutstationDevice getDevice(String stationCode, String deviceCode) {
		if (devices.containsKey(stationCode) && devices.get(stationCode).containsKey(deviceCode)) {
			return devices.get(stationCode).get(deviceCode);
		}
		throw new IllegalArgumentException(format("No device could be found for station %s device %s.", stationCode, deviceCode));
	}
	
	public synchronized static OutstationDevice registerDevice(OutstationDevice device) {
		if (!devices.containsKey(device.getSite())) {
			devices.put(device.getSite(), new HashMap<>());
		}
		if (devices.get(device.getSite()).containsKey(device.getDevice())) {
			LoggerFactory.getLogger(DeviceProvider.class).warn(format("Replacing device %s:%s.", device.getSite(), device.getDevice()));
			unregisterDevice(device.getSite(), device.getDevice());
		}
		devices.get(device.getSite()).put(device.getDevice(), device);
		return devices.get(device.getSite()).get(device.getDevice());
	}
	
	public synchronized static void unregisterDevice(String stationCode, String deviceCode) {
		if (!devices.containsKey(stationCode)) {
			return;
		}
		if (devices.get(stationCode).containsKey(deviceCode)) {
			devices.get(stationCode).remove(deviceCode);
		}
		if (devices.get(stationCode).isEmpty()) {
			devices.remove(stationCode);
		}
	}

	public synchronized static List<String> getStationNames() {
		return new ArrayList<>(devices.keySet());
	}

	public synchronized static List<String> getDeviceNames(String station) {
		Map<String, OutstationDevice> siteDevices = devices.get(station);
		if (siteDevices != null) {
			return new ArrayList<>(siteDevices.keySet());
		}
		return new ArrayList<>();
	}
}
