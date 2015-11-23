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

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jetty.server.Server;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Forced Variation:
 *  - Add the ability to force a variation.
 *  - The ability to disable a point.
 *  
 * Complete BinaryOutput type:
 * - Add the ability to create events.
 * - Add the ability to create control events.
 * 
 * Counters
 * 
 * Complete AnalogOutput type:
 * - Add the ability to create events.
 * - Add the ability to create control events.
 * - Add JSON AnalogOutput to python create module.
 * 
 * Freeze/Unfreeze
 * 
 * Direct Operate NR
 * 
 * Slect/Operate
 * 
 * Wish list:
 * - Subsystem interface to control data pump and other subsystems.
 * - Data pump should stop after 3 successive failures.
 * - Keep a history of the 10 most recent visits (requires a dynamic list and session state).
 * - Add per outstation message log.
 * 
 * Other Thoughts:
 * - The ability to send automatic events when a user changes the value from the web page/
 * 
 * Sandpit (Only by popular demand/A long way off):
 * - Ability to change scheme in Runtime.
 * - The ability to send automatic events when a user changes the value from the web page/
 */
public class App {
	public static void main(String[] args) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		ClassPathXmlApplicationContext loadContext = new ClassPathXmlApplicationContext("outstation-config.xml");
		Map<String, DeviceFactory> deviceFactories = loadContext.getBeansOfType(DeviceFactory.class);
		for (Entry<String, DeviceFactory> entry : deviceFactories.entrySet()) {
			DeviceFactoryRegistry.registerFactory(entry.getKey(), entry.getValue());
		}
		loadContext.close();
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jetty-config.xml");
		try {
			Server server = context.getBean(Server.class);
			context.close();
			server.start();
			server.join();
		} catch (Exception e) {
			context.close();
			e.printStackTrace();
		}
	}
}
