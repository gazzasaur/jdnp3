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

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.AnalogInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.EventListener;
import net.sf.jdnp3.ui.web.outstation.database.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.AnalogInputStaticReader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.BinaryInputStaticReader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class0Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class1Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class2Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class3Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.CrobOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.InternalIndicatorWriter;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.AnalogInputEventMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.AnalogInputMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.BinaryInputEventMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.BinaryInputMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.BinaryOutputMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.HeartbeatMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.InternalIndicatorMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.AnalogInputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.AnalogInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.HeartbeatMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.InternalIndicatorMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.InternalIndicatorsMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * For the first build, multiple outstations must be implemented.
 * 
 * Complete BinaryOutput type:
 * - Add the ability to create events.
 * - Add the ability to create control events.
 * - Counter for the number of control events received.
 * - Ability to choose return attributes including preferred encoding.
 * - The ability to auto-update upon successfuly control request.
 * 
 * All types:
 * - The ability to send automatic events when a user changes the value from the web page/
 * 
 * Output types:
 * - The ability to send automatic events when a master changes the value.
 * - The ability to send automatic control events when a master changes the value.
 * 
 * Near future:
 * - Add a JSON API.
 * - Develop python modules to control JSON interface. 
 * 
 * Wish list:
 * - Subsystem interface to control data pump and other subsystems.
 * - Data pump should stop after 3 successive failures.
 * - Keep a history of the 10 most recent visits (requires a dynamic list and session state).
 * - Add per outstation message log.
 * 
 * Sandpit (Only by popular demand/A long way off):
 * - Ability to change scheme in Runtime.
 */
public class App {
	
	public static void main(String[] args) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		Logger logger = LoggerFactory.getLogger(App.class);
		
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new HeartbeatMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new InternalIndicatorMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryInputMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryInputEventMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryOutputMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new AnalogInputMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new AnalogInputEventMessageHandler());
		
		GenericMessageRegistry registry = GenericMessageRegistryProvider.getRegistry();
		registry.register("heartbeat", HeartbeatMessage.class);
		registry.register("binaryInputEvent", BinaryInputEventMessage.class);
		registry.register("analogInputEvent", AnalogInputEventMessage.class);
		registry.register("internalIndicator", InternalIndicatorMessage.class);
		registry.register("internalIndicators", InternalIndicatorsDataPoint.class, InternalIndicatorsMessage.class);
		registry.register("binaryInputPoint", BinaryInputDataPoint.class, BinaryInputMessage.class);
		registry.register("binaryOutputPoint", BinaryOutputDataPoint.class, BinaryOutputMessage.class);
		registry.register("analogInputPoint", AnalogInputDataPoint.class, AnalogInputMessage.class);

		ClassPathXmlApplicationContext loadContext = new ClassPathXmlApplicationContext("outstation-config.xml");
		Map<String, DataLinkLayer> dataLinkServices = loadContext.getBeansOfType(DataLinkLayer.class);
		loadContext.close();
		for (Entry<String, DataLinkLayer> entry : dataLinkServices.entrySet()) {
			DataLinkManager dataLinkManager = DataLinkManagerProvider.registerDataLink(entry.getKey());
			dataLinkManager.setDataLinkLayer(entry.getValue());
		}
		
		for (int i = 0; i < 1000; ++i) {
			DatabaseManager databaseManager = DatabaseManagerProvider.registerDevice("Pump Station " + i, "Primary Pump 1");
			databaseManager.addBinaryInputDataPoints("Running", "Low Fuel", "Non-Urgent Fail", "Urgent Fail");
			databaseManager.addBinaryOutputDataPoints("Operate");
			databaseManager.addAnalogInputDataPoints("Speed", "Volume");
			
			OutstationFactory outstationFactory = new OutstationFactory();
			outstationFactory.addStandardObjectTypeDecoders();
			outstationFactory.addStandardItemEnumeratorFactories();
			outstationFactory.addStandardOutstationRequestHandlerAdaptors();
			outstationFactory.setInternalStatusProvider(databaseManager.getInternalStatusProvider());
			
			Outstation outstation = outstationFactory.createOutstation();
			outstation.addRequestHandler(new BinaryInputStaticReader(databaseManager));
			outstation.addRequestHandler(new AnalogInputStaticReader(databaseManager));
			outstation.addRequestHandler(new Class0Reader(databaseManager));
			outstation.addRequestHandler(new Class1Reader());
			outstation.addRequestHandler(new Class2Reader());
			outstation.addRequestHandler(new Class3Reader());
			outstation.addRequestHandler(new CrobOperator(databaseManager));
			outstation.addRequestHandler(new InternalIndicatorWriter(databaseManager.getInternalStatusProvider()));
			
			databaseManager.addEventListener(new EventListener() {
				public void eventReceived(DataPoint dataPoint) {
					if (dataPoint instanceof BinaryInputDataPoint) {
						BinaryInputEventObjectInstance binaryInputEventObjectInstance = new BinaryInputEventObjectInstance();
						try {
							BinaryInputDataPoint binaryDataPoint = (BinaryInputDataPoint) dataPoint;
							BeanUtils.copyProperties(binaryInputEventObjectInstance, binaryDataPoint);
							binaryInputEventObjectInstance.setTimestamp(new Date().getTime());
							binaryInputEventObjectInstance.setEventClass(binaryDataPoint.getEventClass());
							binaryInputEventObjectInstance.setRequestedType(binaryDataPoint.getEventType());
							outstation.sendEvent(binaryInputEventObjectInstance);
						} catch (Exception e) {
							logger.error("Failed to send event.", e);
						}
					} else if (dataPoint instanceof AnalogInputDataPoint) {
						AnalogInputEventObjectInstance analogInputEventObjectInstance = new AnalogInputEventObjectInstance();
						try {
							AnalogInputDataPoint analogDataPoint = (AnalogInputDataPoint) dataPoint;
							BeanUtils.copyProperties(analogInputEventObjectInstance, analogDataPoint);
							analogInputEventObjectInstance.setTimestamp(new Date().getTime());
							analogInputEventObjectInstance.setEventClass(analogDataPoint.getEventClass());
							analogInputEventObjectInstance.setRequestedType(analogDataPoint.getEventType());
							outstation.sendEvent(analogInputEventObjectInstance);
						} catch (Exception e) {
							logger.error("Failed to send event.", e);
						}
					}
				}
			});

		}
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jetty-config.xml");
		try {
			Server server = context.getBean(Server.class);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.close();
		}
	}
}
