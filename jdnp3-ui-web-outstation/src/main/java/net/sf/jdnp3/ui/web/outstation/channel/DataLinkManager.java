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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.SimpleSynchronisedTransportBinding;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;

public class DataLinkManager {
	private String dataLinkName;
	private DataLinkLayer dataLinkLayer;
	private Map<TransportBindingItem, OutstationDevice> transportBindings = new HashMap<>();

	// TODO Should probably be a properties object.
	private boolean ignoreFcb = false;
	
	public DataLinkManager(String dataLinkName) {
		this.dataLinkName = dataLinkName;
	}

	public void bind(int address, OutstationDevice outstationDevice) {
		SimpleSynchronisedTransportBinding transportBinding = new SimpleSynchronisedTransportBinding();
		TransportBindingItem dataLinkBinding = new TransportBindingItem(this, transportBinding);
		outstationDevice.addTransportBinding(dataLinkBinding);
		dataLinkBinding.bindOutstation(address, outstationDevice);
		dataLinkBinding.bindDataLink(dataLinkLayer, ignoreFcb);
		transportBindings.put(dataLinkBinding, outstationDevice);
	}
	
	public void start() {
		dataLinkLayer.start();
	}
	
	public void stop() {
		dataLinkLayer.stop();
	}

	public void setIgnoreFcb(boolean ignoreFcb) {
		this.ignoreFcb = ignoreFcb;
	}
	
	public boolean isRunning() {
		return dataLinkLayer.isRunning();
	}

	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
	}
	
	public int getBindingCount() {
		return transportBindings.size();
	}
	
	public void removeBinding(TransportBindingItem transportBindingItem) {
		transportBindings.remove(transportBindingItem);
	}
	
	public void close() {
		dataLinkLayer.close();
		List<Entry<TransportBindingItem, OutstationDevice>> bindings = new ArrayList<>(transportBindings.entrySet());
		for (Entry<TransportBindingItem, OutstationDevice> binding : bindings) {
			transportBindings.remove(binding.getKey());
			binding.getKey().unbind();
		}
	}
	
	public void unbind(OutstationDevice outstation) {
		List<Entry<TransportBindingItem, OutstationDevice>> entries = new ArrayList<>(transportBindings.entrySet());
		for (Entry<TransportBindingItem, OutstationDevice> binding : entries) {
			if (binding.getValue() == outstation) {
				transportBindings.remove(binding.getKey());
				binding.getKey().unbind();
			}
		}
	}
	
	public void unbind(int address, OutstationDevice outstation) {
		List<Entry<TransportBindingItem, OutstationDevice>> entries = new ArrayList<>(transportBindings.entrySet());
		for (Entry<TransportBindingItem, OutstationDevice> binding : entries) {
			if (binding.getValue() == outstation && binding.getKey().getAddress() == address) {
				transportBindings.remove(binding.getKey());
				binding.getKey().unbind();
			}
		}
	}
	
	public List<OutstationBinding> getBindings(OutstationDevice outstation) {
		List<OutstationBinding> outstationBindings = new ArrayList<>();
		for (Entry<TransportBindingItem, OutstationDevice> binding : transportBindings.entrySet()) {
			if (binding.getValue() == outstation) {
				OutstationBinding outstationBinding = new OutstationBinding();
				outstationBinding.setAddress(binding.getKey().getAddress());
				outstationBinding.setDataLinkName(dataLinkName);
				outstationBindings.add(outstationBinding);
			}
		}
		return outstationBindings;
	}
}
