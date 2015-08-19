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
package net.sf.jdnp3.ui.web.outstation;

import java.util.Date;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.TcpIpServerDataLink;
import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.EventListener;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.BinaryInputStaticReader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class0Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class1Reader;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.BinaryInputEventMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.BinaryInputMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistryProvider;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		Logger logger = LoggerFactory.getLogger(App.class);
		
		DatabaseManagerProvider.getDatabaseManager().setBinaryDatabaseSize(10);
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryInputMessageHandler());
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryInputEventMessageHandler());
		
		OutstationFactory outstationFactory = new OutstationFactory();
		outstationFactory.addStandardOutstationRequestHandlerAdaptors();
		outstationFactory.addStandardObjectTypeDescoders();
		
		Outstation outstation = outstationFactory.createOutstation();
		outstation.addRequestHandler(new BinaryInputStaticReader());
		outstation.addRequestHandler(new Class0Reader());
		outstation.addRequestHandler(new Class1Reader());
		
		DatabaseManagerProvider.getDatabaseManager().addEventListener(new EventListener() {
			public void eventReceived(BinaryDataPoint binaryDataPoint) {
				BinaryInputEventObjectInstance binaryInputEventObjectInstance = new BinaryInputEventObjectInstance();
				try {
					BeanUtils.copyProperties(binaryInputEventObjectInstance, binaryDataPoint);
					binaryInputEventObjectInstance.setTimestamp(new Date().getTime());
					binaryInputEventObjectInstance.setEventClass(binaryDataPoint.getEventClass());
					binaryInputEventObjectInstance.setRequestedType(binaryDataPoint.getEventType());
					outstation.sendEvent(binaryInputEventObjectInstance);
				} catch (Exception e) {
					logger.error("Failed to send event.", e);
				}
			}
		});
		
		TcpIpServerDataLink dataLink = new TcpIpServerDataLink();

		outstation.setDataLinkLayer(dataLink);
		dataLink.enable();
		
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
