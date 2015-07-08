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
package net.sf.jdnp3.dnp3.stack.layer.application;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class OutstationApplicationLayerImpl implements OutstationApplicationLayer {
	private TransportLayer transportLayer = null;
	private List<Pair<RequestFilter, RequestHandler>> handlers = new ArrayList<>();
	
	public void doUnsolicitedResponse(Response response) {
		throw new UnsupportedOperationException("doUnsolicitedResponse");
	}
	
	public void addRequestHandler(RequestHandler requestHandler, RequestFilter filter) {
		handlers.add(new ImmutablePair<RequestFilter, RequestHandler>(filter, requestHandler));
	}

	public void dataReceived(List<Byte> data) {
	}

	public void setTransportLayer(TransportLayer transportLayer) {
		this.transportLayer = transportLayer;
	}
}
