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
package net.sf.jdnp3.ui.web.outstation.channel;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.ApplicationTransportBindingAdaptor;
import net.sf.jdnp3.dnp3.stack.layer.transport.DataLinkTransportBindingAdaptor;
import net.sf.jdnp3.dnp3.stack.layer.transport.SimpleSynchronisedTransportBinding;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportBinding;

public class DataLinkManager {
	private DataLinkLayer dataLinkLayer;
	private List<TransportBinding> transportBindings = new ArrayList<>();
	
	public void bind(int address, Outstation outstation) {
		SimpleSynchronisedTransportBinding transportBinding = new SimpleSynchronisedTransportBinding();
		DataLinkTransportBindingAdaptor dataLinkBinding = new DataLinkTransportBindingAdaptor(transportBinding);
		ApplicationTransportBindingAdaptor applicationBinding = new ApplicationTransportBindingAdaptor(transportBinding);
		dataLinkLayer.addDataLinkLayerListener(dataLinkBinding);
		outstation.setApplicationTransport(applicationBinding);
		transportBinding.setApplicationLayer(address, outstation.getApplicationLayer());
		transportBinding.setDataLinkLayer(dataLinkLayer);
		transportBindings.add(transportBinding);
	}

	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
	}
	
	public int getBindingCount() {
		return transportBindings.size();
	}

	public int getConnectionCount() {
		return this.dataLinkLayer.getConnectionCount();
	}
}
