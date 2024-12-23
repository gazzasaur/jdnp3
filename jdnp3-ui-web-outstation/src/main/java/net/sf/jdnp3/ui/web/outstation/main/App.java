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
 *  Bind/Unbind from web-page.
 *  Timestamp from web-page.
 * 
 * Complete BinaryOutput type:
 * - Add the ability to create control events.
 * 
 * Complete AnalogOutput type:
 * - Add the ability to create control events.
 * 
 * Freeze/Unfreeze
 * 
 * Direct Operate NR
 * 
 * Select/Operate
 */
public class App {
	public static void main(String[] args) throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		try (ClassPathXmlApplicationContext loadContext = new ClassPathXmlApplicationContext("outstation-config.xml")) {
			Map<String, DeviceFactory> deviceFactories = loadContext.getBeansOfType(DeviceFactory.class);
			for (Entry<String, DeviceFactory> entry : deviceFactories.entrySet()) {
				DeviceFactoryRegistry.registerFactory(entry.getKey(), entry.getValue());
			}
			
			Map<String, DataLinkFactory> dataLinkFactories = loadContext.getBeansOfType(DataLinkFactory.class);
			for (Entry<String, DataLinkFactory> entry : dataLinkFactories.entrySet()) {
				DataLinkFactoryRegistry.registerFactory(entry.getKey(), entry.getValue());
			}
		}

		Server server;
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jetty-config.xml")) {
			server = context.getBean(Server.class);
		}
		server.start();
		server.join();
	}
}
