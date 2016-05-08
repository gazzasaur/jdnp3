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

import java.util.HashMap;
import java.util.Map;

public class DeviceFactoryRegistry {
	private static Map<String, DeviceFactory> factories = new HashMap<>();
	
	public static void registerFactory(String name, DeviceFactory factory) {
		factories.put(name, factory);
	}
	
	public static boolean hasFactory(String name) {
		DeviceFactory factory = factories.get(name);
		return factory != null;
	}
	
	public static DeviceFactory getFactory(String name) {
		DeviceFactory factory = factories.get(name);
		if (factory == null) {
			throw new IllegalArgumentException("No factory matches the given name: " + name);
		}
		return factory;
	}
}
