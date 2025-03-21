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
package net.sf.jdnp3.ui.web.outstation.channel;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;

public class DataLinkManagerProvider {
	private static Map<String, DataLinkManager> dataLinkManagers = new HashMap<>();

	public synchronized static int getDataLinkManagerCount() {
		return dataLinkManagers.size();
	}

	public synchronized static int getDataLinkBindingCount() {
		int total = 0;
		for (DataLinkManager dataLinkManager : dataLinkManagers.values()) {
			total += dataLinkManager.getBindingCount();
		}
		return total;
	}

	public synchronized static DataLinkManager getDataLinkManager(String dataLinkName) {
		if (dataLinkManagers.containsKey(dataLinkName)) {
			return dataLinkManagers.get(dataLinkName);
		}
		throw new IllegalArgumentException(format("The data link %s could not be found.", dataLinkName));
	}
	
	public synchronized static DataLinkManager registerDataLink(String dataLinkName) {
		if (!dataLinkManagers.containsKey(dataLinkName)) {
			dataLinkManagers.put(dataLinkName, new DataLinkManager(dataLinkName));
		} else {
			throw new IllegalArgumentException("DataLink already exists: " + dataLinkName);
		}
		return dataLinkManagers.get(dataLinkName);
	}
	
	public synchronized static void unregisterDataLink(String dataLinkName) {
		if (!dataLinkManagers.containsKey(dataLinkName)) {
			throw new IllegalArgumentException("DataLink does not exist: " + dataLinkName);
		}
		DataLinkManager dataLinkManager = dataLinkManagers.remove(dataLinkName);
		dataLinkManager.close();
	}

	public synchronized static List<String> getDataLinkNames() {
		return new ArrayList<>(dataLinkManagers.keySet());
	}
	
	public synchronized static List<OutstationBinding> getDataLinkBindings(OutstationDevice outstationDevice) {
		List<OutstationBinding> outstationBindings = new ArrayList<>();
		for (DataLinkManager dataLinkManager : dataLinkManagers.values()) {
			outstationBindings.addAll(dataLinkManager.getBindings(outstationDevice));
		}
		return outstationBindings;
	}

	public synchronized static void disableDataLink(String name) {
		DataLinkManager dataLinkManager = dataLinkManagers.get(name);
		dataLinkManager.stop();
	}
}
