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
package net.sf.jdnp3.dnp3.stack.main;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_WITHOUT_TIME;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationServiceImpl;
import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.Class0ReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.Class1ReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.EventObjectInstanceSelector;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.TcpIpServerDataLink;

public class App {
	public static void main(String[] args) throws InterruptedException {
		OutstationServiceImpl outstation = new OutstationServiceImpl();
		outstation.addServiceRequestHandler(new BinaryInputStaticReadRequestHandler() {
			public List<BinaryInputStaticObjectInstance> doReadStatics(long startIndex, long stopIndex) {
				List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
				for (long i = startIndex; i <= stopIndex; ++i) {
					BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
					binaryInputStaticObjectInstance.setActive(i%2 == 0);
					binaryInputStaticObjectInstance.setOnline(true);
					binaryInputStaticObjectInstance.setIndex(i);
					points.add(binaryInputStaticObjectInstance);
				}
				return points;
			}
			
			public List<BinaryInputStaticObjectInstance> doReadStatics() {
				return new ArrayList<>();
			}
		});
		outstation.addServiceRequestHandler(new Class0ReadRequestHandler() {
			public List<ObjectInstance> doReadClass() {
				List<ObjectInstance> items = new ArrayList<>();
				for (int i = 250; i < 257; ++i) {
					BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
					binaryInputStaticObjectInstance.setActive(i%2 == 0);
					binaryInputStaticObjectInstance.setOnline(true);
					binaryInputStaticObjectInstance.setIndex(i);
					items.add(binaryInputStaticObjectInstance);
				}
				AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
				analogInputStaticObjectInstance.setValue(56.73);
				analogInputStaticObjectInstance.setIndex(0);
				items.add(analogInputStaticObjectInstance);
				return items;
			}
			
			public List<ObjectInstance> doReadClass(long returnLimit) {
				return new ArrayList<>();
			}
		});
		outstation.addServiceRequestHandler(new Class1ReadRequestHandler() {
			public List<ObjectInstance> doReadClass() {
				EventObjectInstanceSelector selector = new EventObjectInstanceSelector() {
					public boolean select(EventObjectInstance eventObjectInstance) {
						return eventObjectInstance.getEventClass() == 1;
					}
				};
				return outstation.getOutstationEventQueue().request(selector);
			}

			public List<ObjectInstance> doReadClass(long returnLimit) {
				EventObjectInstanceSelector selector = new EventObjectInstanceSelector() {
					public boolean select(EventObjectInstance eventObjectInstance) {
						return eventObjectInstance.getEventClass() == 1;
					}
				};
				return outstation.getOutstationEventQueue().request(selector, returnLimit);
			}
		});
		
		TcpIpServerDataLink dataLink = new TcpIpServerDataLink();
		
		outstation.setDataLinkLayer(dataLink);
		dataLink.enable();
		
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					BinaryInputEventObjectInstance binaryInputEventObjectInstance = new BinaryInputEventObjectInstance();
					binaryInputEventObjectInstance.setRequestedType(BINARY_INPUT_EVENT_WITHOUT_TIME);
					outstation.getOutstationEventQueue().addEvent(binaryInputEventObjectInstance);
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
