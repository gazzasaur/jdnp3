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

import java.util.HashMap;
import java.util.Map;

public class DatabaseManagerProvider {
	private static Map<String, Map<String, DatabaseManager>> databaseManagers = new HashMap<String, Map<String, DatabaseManager>>();
	
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
		if (!databaseManagers.get(stationCode).containsKey(deviceCode)) {
			databaseManagers.get(stationCode).put(deviceCode, new DatabaseManager());
		}
		return databaseManagers.get(stationCode).get(deviceCode);
	}
}
