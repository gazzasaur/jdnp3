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
package net.sf.jdnp3.ui.web.outstation.database;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class DatabaseManagerProvider {
	private static Map<String, Map<String, DatabaseManager>> databaseManagers = new HashMap<>();
	
	public synchronized static int getStationCount() {
		return databaseManagers.size();
	}

	public synchronized static int getDeviceCount() {
		int total = 0;
		Collection<Map<String, DatabaseManager>> values = databaseManagers.values();
		for (Map<String, DatabaseManager> map : values) {
			total += map.size();
		}
		return total;
	}
	
	public synchronized static DatabaseManager getDatabaseManager(String stationCode, String deviceCode) {
		if (databaseManagers.containsKey(stationCode) && databaseManagers.get(stationCode).containsKey(deviceCode)) {
			return databaseManagers.get(stationCode).get(deviceCode);
		}
		throw new IllegalArgumentException(format("No device could be found for station %s device %s.", stationCode, deviceCode));
	}
	
	public synchronized static DatabaseManager registerDevice(String stationCode, String deviceCode) {
		if (!databaseManagers.containsKey(stationCode)) {
			databaseManagers.put(stationCode, new HashMap<>());
		}
		if (databaseManagers.get(stationCode).containsKey(deviceCode)) {
			LoggerFactory.getLogger(DatabaseManagerProvider.class).warn(format("Replacing device %s:%s."));
		}
		databaseManagers.get(stationCode).put(deviceCode, new DatabaseManager());
		return databaseManagers.get(stationCode).get(deviceCode);
	}
	
	public synchronized static void unregisterDevice(String stationCode, String deviceCode) {
		if (!databaseManagers.containsKey(stationCode)) {
			return;
		}
		if (databaseManagers.get(stationCode).containsKey(deviceCode)) {
			databaseManagers.get(stationCode).remove(deviceCode);
		}
		if (databaseManagers.get(stationCode).isEmpty()) {
			databaseManagers.remove(stationCode);
		}
	}

	public synchronized static List<String> getStationNames() {
		return new ArrayList<>(databaseManagers.keySet());
	}

	public synchronized static List<String> getDeviceNames(String station) {
		Map<String, DatabaseManager> devices = databaseManagers.get(station);
		if (devices != null) {
			return new ArrayList<>(devices.keySet());
		}
		return new ArrayList<>();
	}
}
