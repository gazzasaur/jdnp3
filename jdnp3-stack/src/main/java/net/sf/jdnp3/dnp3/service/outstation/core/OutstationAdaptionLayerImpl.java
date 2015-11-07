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
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class OutstationAdaptionLayerImpl implements OutstationAdaptionLayer {
	private List<OutstationRequestHandlerAdaptor> adaptors = new ArrayList<>();

	public void addRequestHandler(OutstationRequestHandler requestHandler) {
		for (OutstationRequestHandlerAdaptor adaptor : adaptors) {
			adaptor.setRequestHandler(requestHandler);
		}
	}

	public void removeRequestHandler(OutstationRequestHandler requestHandler) {
		throw new UnsupportedOperationException();
	}

	public void addOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor) {
		adaptors.add(outstationRequestHandlerAdaptor);
	}

	public void removeOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor) {
		throw new UnsupportedOperationException();
	}

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		for (OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor : adaptors) {
			if (outstationRequestHandlerAdaptor.canHandle(functionCode, request)) {
				return true;
			}
		}
		return false;
	}

	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		for (OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor : adaptors) {
			if (outstationRequestHandlerAdaptor.canHandle(functionCode, request)) {
				outstationRequestHandlerAdaptor.doRequest(functionCode, outstationEventQueue, request, response);
			}
		}
	}
}
