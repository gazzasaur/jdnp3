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
package net.sf.jdnp3.dnp3.stack.layer.transport;

import java.util.ArrayDeque;
import java.util.Deque;

public class TransportSegment {
	private TransportHeader transportHeader = new TransportHeader();
	private Deque<Byte> data = new ArrayDeque<>();
	
	public TransportHeader getTransportHeader() {
		return transportHeader;
	}
	
	public void setTransportHeader(TransportHeader transportHeader) {
		this.transportHeader = transportHeader;
	}

	public Deque<Byte> getData() {
		return data;
	}

	public void setData(Deque<Byte> data) {
		this.data = data;
	}
}
