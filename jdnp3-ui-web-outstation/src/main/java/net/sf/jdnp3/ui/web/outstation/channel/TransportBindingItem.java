/**
 * Copyright 2016 Graeme Farquharson
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

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.StatefulDataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.ApplicationTransportBindingAdaptor;
import net.sf.jdnp3.dnp3.stack.layer.transport.DataLinkTransportBindingAdaptor;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportBinding;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;

public class TransportBindingItem {
	private int address = 0;
	private DataLinkLayer dataLinkLayer = null;
	private OutstationDevice outstationDevice = null;
	
	private TransportBinding transportBinding = null;
	private DataLinkInterceptor dataLinkInterceptor = null;
	private DataLinkTransportBindingAdaptor dataLinkTransportBindingAdaptor = null;
	private ApplicationTransportBindingAdaptor applicationTransportBindingAdaptor = null;
	private DataLinkManager dataLinkManager;
	
	public TransportBindingItem(DataLinkManager dataLinkManager, TransportBinding transportBinding) {
		this.dataLinkManager = dataLinkManager;
		this.transportBinding = transportBinding;
	}
	
	public void bindOutstation(int address, OutstationDevice outstation) {
		if (outstationDevice != null) {
			throw new IllegalStateException("An outstation is already bound.");
		}
		this.address = address;
		outstationDevice = outstation;
		applicationTransportBindingAdaptor = new ApplicationTransportBindingAdaptor(transportBinding);
		outstationDevice.getOutstation().addApplicationTransport(applicationTransportBindingAdaptor);
		transportBinding.setApplicationLayer(address, outstationDevice.getOutstation().getApplicationLayer());
	}
	
	public void bindDataLink(DataLinkLayer dataLinkLayer) {
		if (this.dataLinkLayer != null) {
			throw new IllegalStateException("A datalink is already bound.");
		}
		this.dataLinkLayer = dataLinkLayer;
		dataLinkTransportBindingAdaptor = new DataLinkTransportBindingAdaptor(transportBinding);
		dataLinkInterceptor = new StatefulDataLinkInterceptor(dataLinkLayer, this.address, dataLinkTransportBindingAdaptor);
		dataLinkLayer.addDataLinkLayerListener(dataLinkInterceptor);
		transportBinding.setDataLinkLayer(dataLinkLayer);
	}
	
	public void unbind() {
		dataLinkManager.removeBinding(this);
		if (outstationDevice != null) {
			outstationDevice.getOutstation().removeApplicationTransport(applicationTransportBindingAdaptor);
			outstationDevice = null;
		}
		if (dataLinkLayer != null) {
			dataLinkLayer.removeDataLinkLayerListener(dataLinkInterceptor);
			dataLinkLayer = null;
		}
	}

	public int getAddress() {
		return address;
	}
}