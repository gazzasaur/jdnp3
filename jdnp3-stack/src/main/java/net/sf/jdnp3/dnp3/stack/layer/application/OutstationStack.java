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

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayerImpl;

public class OutstationStack {
	private DataLinkLayer dataLinkLayer = null;
	private TransportLayer transportLayer = new TransportLayerImpl();
	private OutstationApplicationLayer outstationApplicationLayer = new OutstationApplicationLayerImpl();
	
	public OutstationStack() {
//		outstationApplicationLayer
//		transportLayer.setApplicationLayer(outstationApplicationLayer);
		
	}
	
	public DataLinkLayer getDataLinkLayer() {
		return dataLinkLayer;
	}

	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
	}
}
