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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationServiceImpl;
import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.Class0ReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.TcpIpServerDataLink;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.handler.BinaryInputMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.handler.MessageHandlerRegistryProvider;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.server.Server;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		DatabaseManagerProvider.getDatabaseManager().setBinaryDatabaseSize(10);
		MessageHandlerRegistryProvider.getMessageHandlerRegistry().registerHandler(new BinaryInputMessageHandler());
		
		OutstationServiceImpl outstation = new OutstationServiceImpl();
		outstation.addServiceRequestHandler(new BinaryInputStaticReadRequestHandler() {
			public List<BinaryInputStaticObjectInstance> doReadStatics(long startIndex, long stopIndex) {
				List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
				List<BinaryDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
				
				for (long i = startIndex; i <= stopIndex; ++i) {
					BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
					try {
						BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoints.get((int) i));
						points.add(binaryInputStaticObjectInstance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return points;
			}
			
			public List<BinaryInputStaticObjectInstance> doReadStatics() {
				List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
				List<BinaryDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
				
				for (BinaryDataPoint binaryDataPoint : binaryDataPoints) {
					BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
					try {
						BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoint);
						points.add(binaryInputStaticObjectInstance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return points;
			}
		});
		outstation.addServiceRequestHandler(new Class0ReadRequestHandler() {
			public List<ObjectInstance> doReadClass() {
				List<ObjectInstance> points = new ArrayList<>();
				List<BinaryDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
				
				for (BinaryDataPoint binaryDataPoint : binaryDataPoints) {
					BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
					try {
						BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoint);
						points.add(binaryInputStaticObjectInstance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return points;
			}
			
			public List<ObjectInstance> doReadClass(long returnLimit) {
				return new ArrayList<>();
			}
		});
		
		TcpIpServerDataLink dataLink = new TcpIpServerDataLink();
		dataLink.setDirection(Direction.OUTSTATION_TO_MASTER);
		dataLink.setDestination(64);
		dataLink.setSource(2);
		
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
