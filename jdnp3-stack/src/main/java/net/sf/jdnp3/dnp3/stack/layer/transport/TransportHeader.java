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

public class TransportHeader {
	private boolean firstSegment = false;
	private boolean finalSegment = false;
	private int sequenceNumber = 0;
	
	public boolean isFirstSegment() {
		return firstSegment;
	}
	
	public void setFirstSegment(boolean firstSegment) {
		this.firstSegment = firstSegment;
	}

	public boolean isFinalSegment() {
		return finalSegment;
	}

	public void setFinalSegment(boolean finalSegment) {
		this.finalSegment = finalSegment;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
