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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseListener;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class DeviceProvider {
	private static Map<String, Map<String, OutstationDevice>> devices = new HashMap<>();
	private static Map<String, Map<String, List<DatabaseListener>>> databaseListeners = new HashMap<>();

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
		if (devices.containsKey(device.getSite()) && devices.get(device.getSite()).containsKey(device.getDevice())) {
			throw new IllegalStateException(format("Cannot register device %s:%s.  Device already exists.", device.getSite(), device.getDevice()));
		}
		if (!devices.containsKey(device.getSite())) {
			devices.put(device.getSite(), new HashMap<>());
		}
		devices.get(device.getSite()).put(device.getDevice(), device);
		
		List<DatabaseListener> listeners = getDatabaseListeners(device.getSite(), device.getDevice());
		for (DatabaseListener listener : listeners) {
			device.getDatabaseManager().addDatabaseListener(listener);
			triggerDatabaseListener(listener, device);
		}
		
		return devices.get(device.getSite()).get(device.getDevice());
	}
	
	public synchronized static void unregisterDevice(String stationCode, String deviceCode) {
		if (!devices.containsKey(stationCode)) {
			return;
		}
		if (devices.get(stationCode).containsKey(deviceCode)) {
			OutstationDevice outstationDevice = devices.get(stationCode).remove(deviceCode);
			outstationDevice.unbind();
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

	public synchronized static void addDatabaseListener(String station, String device, DatabaseListener databaseListener) {
		if (!databaseListeners.containsKey(station)) {
			databaseListeners.put(station, new HashMap<>());
		}
		if (!databaseListeners.get(station).containsKey(device)) {
			databaseListeners.get(station).put(device, new ArrayList<>());
		}
		databaseListeners.get(station).get(device).add(databaseListener);
		
		OutstationDevice outstationDevice = null;
		try {
			outstationDevice = DeviceProvider.getDevice(station, device);
			outstationDevice.getDatabaseManager().addDatabaseListener(databaseListener);
		} catch (Exception e) {
			Logger logger = LoggerFactory.getLogger(DeviceProvider.class);
			logger.debug(format("Cannot retreive device %s:%s.", station, device), e);
			logger.info(format("No device found for %s:%s.  Registered interest.", station, device));
		}
		
		if (outstationDevice != null) {
			triggerDatabaseListener(databaseListener, outstationDevice);
		}
	}
	
	public synchronized static void removeDatabaseListener(String station, String device, DatabaseListener databaseListener) {
		if (!databaseListeners.containsKey(station)) {
			return;
		}
		if (!databaseListeners.get(station).containsKey(device)) {
			return;
		}
		databaseListeners.get(station).get(device).remove(databaseListener);
		
		if (databaseListeners.containsKey(station)) {
			if (databaseListeners.get(station).containsKey(device)) {
				if (databaseListeners.get(station).get(device).size() == 0) {
					databaseListeners.get(station).remove(device);
				}
			}
			if (databaseListeners.get(station).size() == 0) {
				databaseListeners.remove(station);
			}
		}
	}
	
	public static synchronized void triggerBindingsUpdate(OutstationDevice outstationDevice) {
		List<DatabaseListener> listeners = getDatabaseListeners(outstationDevice.getSite(), outstationDevice.getDevice());
		List<OutstationBinding> outstationBindings = DataLinkManagerProvider.getDataLinkBindings(outstationDevice);
		for (DatabaseListener databaseListener : listeners) {
			databaseListener.bindingsChanged(outstationBindings);
		}
	}
	
	private static void triggerDatabaseListener(DatabaseListener databaseListener, OutstationDevice outstationDevice) {
		DatabaseManager databaseManager = outstationDevice.getDatabaseManager();
		
		databaseListener.valueChanged(databaseManager.getInternalIndicatorsDataPoint());
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		List<BinaryOutputDataPoint> binaryOutputDataPoints = databaseManager.getBinaryOutputDataPoints();
		for (BinaryOutputDataPoint binaryDataPoint : binaryOutputDataPoints) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		List<AnalogInputDataPoint> analogInputDataPoints = databaseManager.getAnalogInputDataPoints();
		for (AnalogInputDataPoint analogDataPoint : analogInputDataPoints) {
			databaseListener.valueChanged(analogDataPoint);
		}
		List<AnalogOutputDataPoint> analogOutputDataPoints = databaseManager.getAnalogOutputDataPoints();
		for (AnalogOutputDataPoint analogDataPoint : analogOutputDataPoints) {
			databaseListener.valueChanged(analogDataPoint);
		}
		List<CounterDataPoint> counterDataPoints = databaseManager.getCounterDataPoints();
		for (CounterDataPoint counterDataPoint : counterDataPoints) {
			databaseListener.valueChanged(counterDataPoint);
		}
		List<OutstationBinding> outstationBindings = DataLinkManagerProvider.getDataLinkBindings(outstationDevice);
		databaseListener.bindingsChanged(outstationBindings);
	}
	
	private static List<DatabaseListener> getDatabaseListeners(String site, String device) {
		List<DatabaseListener> listeners = new ArrayList<>();
		if (databaseListeners.containsKey(site) && databaseListeners.get(site).containsKey(device)) {
			listeners.addAll(databaseListeners.get(site).get(device));
		}
		return listeners;
	}
}
