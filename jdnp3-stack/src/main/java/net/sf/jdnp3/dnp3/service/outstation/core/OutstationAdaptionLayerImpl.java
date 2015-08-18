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

import net.sf.jdnp3.dnp3.service.outstation.handler.RequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.OutstationApplicationLayer;

public class OutstationAdaptionLayerImpl implements OutstationAdaptionLayer {
	private OutstationApplicationLayer outstationApplicationLayer;
	private List<OutstationRequestHandlerAdaptor> adaptors = new ArrayList<>();

	public void addRequestHandler(RequestHandler requestHandler) {
		for (OutstationRequestHandlerAdaptor adaptor : adaptors) {
			adaptor.setRequestHandler(requestHandler);
		}
	}

	public void removeRequestHandler(RequestHandler requestHandler) {
		throw new UnsupportedOperationException();
	}

	public void setApplicationLayer(OutstationApplicationLayer outstationApplicationLayer) {
		if (this.outstationApplicationLayer != null) {
			throw new IllegalStateException("Application layer has already been set.");
		}
		this.outstationApplicationLayer = outstationApplicationLayer;
	}

	public void addOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor) {
		if (outstationApplicationLayer == null) {
			throw new IllegalStateException("Application layer has not been set.");
		}
		adaptors.add(outstationRequestHandlerAdaptor);
		outstationApplicationLayer.addRequestHandler(outstationRequestHandlerAdaptor);
	}

	public void removeOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor outstationRequestHandlerAdaptor) {
		throw new UnsupportedOperationException();
	}
}
