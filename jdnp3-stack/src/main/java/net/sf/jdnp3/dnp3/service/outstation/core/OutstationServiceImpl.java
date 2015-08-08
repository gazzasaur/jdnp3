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
package net.sf.jdnp3.dnp3.service.outstation.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.OutstationApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.OutstationEventQueue;
import net.sf.jdnp3.dnp3.stack.layer.application.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;

public class OutstationServiceImpl implements OutstationService {
	@SuppressWarnings("serial")
	private List<OutstationRequestHandlerAdaptor> outstationRequestHandlers = new ArrayList<OutstationRequestHandlerAdaptor>() {{
		this.add(new Class0ReadRequestAdaptor());
		this.add(new Class1ReadRequestAdaptor());
		this.add(new BinaryInputStaticReadRequestAdaptor());
	}};
	
	private OutstationApplicationLayer applicationLayer = new OutstationApplicationLayer();
	
	public OutstationServiceImpl() {
		for (OutstationRequestHandler outstationRequestHandler : outstationRequestHandlers) {
			applicationLayer.addHandlerHelper(outstationRequestHandler);
		}
	}

	public void addServiceRequestHandler(ServiceRequestHandler serviceRequestHandler) {
		for (OutstationRequestHandlerAdaptor outstationRequestHandler : outstationRequestHandlers) {
			outstationRequestHandler.setServiceRequestHandler(serviceRequestHandler);
		}
	}
	
	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		applicationLayer.setDataLinkLayer(dataLinkLayer);
	}

	public OutstationEventQueue getOutstationEventQueue() {
		return applicationLayer.getOutstationEventQueue();
	}
}
