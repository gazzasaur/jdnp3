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

import net.sf.jdnp3.dnp3.service.outstation.handler.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationTransport;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationLayer;

public class OutstationImpl implements Outstation {
	private OutstationAdaptionLayer outstationAdaptionLayer;
	private OutstationApplicationLayer outstationApplicationLayer;
	private List<OutstationRequestHandler> requestHandlers = new ArrayList<>();
	
	public void setOutstationAdaptionLayer(OutstationAdaptionLayer outstationAdaptionLayer) {
		this.outstationAdaptionLayer = outstationAdaptionLayer;
	}
	
	public void setOutstationApplicationLayer(OutstationApplicationLayer outstationApplicationLayer) {
		this.outstationApplicationLayer = outstationApplicationLayer;
	}
	
	public void addRequestHandler(OutstationRequestHandler requestHandler) {
		requestHandlers.add(requestHandler);
		outstationAdaptionLayer.addRequestHandler(requestHandler);
	}
	
	public void removeRequestHandler(OutstationRequestHandler requestHandler) {
		throw new UnsupportedOperationException();
	}
	
	public void sendEvent(EventObjectInstance eventObjectInstance) {
		outstationApplicationLayer.getOutstationEventQueue().addEvent(eventObjectInstance);
	}

	public void setApplicationTransport(ApplicationTransport applicationTransport) {
		outstationApplicationLayer.setApplicationTransport(applicationTransport);
	}

	public ApplicationLayer getApplicationLayer() {
		return outstationApplicationLayer;
	}

	public void setPrimaryAddress(int address) {
		outstationApplicationLayer.setPrimaryAddress(address);
	}
}
