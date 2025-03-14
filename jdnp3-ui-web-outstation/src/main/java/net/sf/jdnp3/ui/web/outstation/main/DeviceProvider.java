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

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseListener;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.core.GlobalDatabaseListener;
import net.sf.jdnp3.ui.web.outstation.database.core.GlobalDatabaseListenerAdaptor;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class DeviceProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceProvider.class);

	private static Map<String, Map<String, OutstationDevice>> devices = new HashMap<>();
	private static Map<String, Map<String, List<DatabaseListener>>> databaseListeners = new HashMap<>();
	private static List<DeviceProviderListener> deviceProviderListeners = new ArrayList<>();
	private static List<GlobalDatabaseListenerAdaptor> globalDatabaseListeners = new ArrayList<>();

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
		
		triggerDeviceProviderListeners();
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
		triggerDeviceProviderListeners();
	}

	// FIXME Typo
	public synchronized static SiteListing gettDeviceList() {
		return fetchDeviceListings();
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
		DeviceProvider.addDatabaseListener(station, device, databaseListener, true);
	}

	public synchronized static void addDatabaseListener(String station, String device, DatabaseListener databaseListener, boolean dumpData) {
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
			LOGGER.debug("Cannot retreive device {}:{}.", station, device, e);
			LOGGER.info("No device found for {}:{}.  Registered interest.", station, device);
		}
		
		if (dumpData && outstationDevice != null) {
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
		
		OutstationDevice outstationDevice = null;
		try {
			outstationDevice = DeviceProvider.getDevice(station, device);
			outstationDevice.getDatabaseManager().removeDatabaseListener(databaseListener);
		} catch (Exception e) {
			LOGGER.error("No device found for {}:{}.", station, device);
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
	
	public synchronized static void addGlobalDatabaseListener(GlobalDatabaseListener databaseListener) {
		SiteListing globalDeviceList = DeviceProvider.gettDeviceList();
		for (SiteDeviceList site : globalDeviceList.getSiteDeviceLists()) {
			for (String device : site.getDevices()) {
				GlobalDatabaseListenerAdaptor adaptor = new GlobalDatabaseListenerAdaptor(site.getSite(), device, databaseListener);
				DeviceProvider.addDatabaseListener(site.getSite(), device, adaptor, false);
				globalDatabaseListeners.add(adaptor);
			}
		}
	}
	
	public synchronized static void removeGlobalDatabaseListener(GlobalDatabaseListener databaseListener) {
		for (GlobalDatabaseListenerAdaptor adaptor : new ArrayList<>(globalDatabaseListeners)) {
			if (adaptor.getDatabaseListener() == databaseListener) {
				DeviceProvider.removeDatabaseListener(adaptor.getSite(), adaptor.getDevice(), adaptor);
				globalDatabaseListeners.remove(adaptor);
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

	public static synchronized void addDeviceProviderListener(DeviceProviderListener deviceProviderListener) {
		deviceProviderListeners.add(deviceProviderListener);
		triggerDeviceProviderListeners();
	}

	public static synchronized void removeDeviceProviderListener(DeviceProviderListener deviceProviderListener) {
		deviceProviderListeners.remove(deviceProviderListener);
	}

	public static synchronized void startDataLinkManager(String dataLink) {
		DataLinkManager dataLinkManager = DataLinkManagerProvider.getDataLinkManager(dataLink);
		dataLinkManager.start();

		for (OutstationDevice outstationDevice : dataLinkManager.getOutstationDevices()) {
			triggerBindingsUpdate(outstationDevice);
		}
    }

    public static synchronized void stopDataLinkManager(String dataLink) {
		DataLinkManager dataLinkManager = DataLinkManagerProvider.getDataLinkManager(dataLink);
		dataLinkManager.stop();

		for (OutstationDevice outstationDevice : dataLinkManager.getOutstationDevices()) {
			triggerBindingsUpdate(outstationDevice);
		}
    }

	private static void triggerDeviceProviderListeners() {
		SiteListing deviceListings = fetchDeviceListings();
		for (DeviceProviderListener deviceProviderListener : deviceProviderListeners) {
			deviceProviderListener.updatedSiteList(deviceListings);
		}
	}

	private static SiteListing fetchDeviceListings() {
		List<String> siteNames = new ArrayList<>(devices.keySet());
		Collections.sort(siteNames);
		
		SiteListing siteList = new SiteListing();
		for (String siteName : siteNames) {
			List<String> deviceNames = new ArrayList<>(devices.get(siteName).keySet());
			Collections.sort(deviceNames);
			
			SiteDeviceList siteDeviceList = new SiteDeviceList();
			siteDeviceList.setSite(siteName);
			siteDeviceList.setDevices(deviceNames);
			siteList.addSiteDeviceList(siteDeviceList);
		}
		return siteList;
	}

	private static void triggerDatabaseListener(DatabaseListener databaseListener, OutstationDevice outstationDevice) {
		DatabaseManager databaseManager = outstationDevice.getDatabaseManager();
		
		databaseListener.valueChanged(databaseManager.getInternalIndicatorsDataPoint());
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		List<DoubleBitBinaryInputDataPoint> doubleBitBinaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();
		for (DoubleBitBinaryInputDataPoint doubleBitBinaryDataPoint : doubleBitBinaryDataPoints) {
			databaseListener.valueChanged(doubleBitBinaryDataPoint);
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
