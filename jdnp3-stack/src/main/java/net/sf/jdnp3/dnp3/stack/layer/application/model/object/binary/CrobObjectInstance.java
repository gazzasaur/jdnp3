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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class CrobObjectInstance implements ObjectInstance {
	private long index;
	private long count;
	private long onTime;
	private long offTime;
	private boolean clear;
	private boolean queue;
	private StatusCode statusCode;
	private OperationType operationType;
	private TripCloseCode tripCloseCode;
	private ObjectType requestedType = ANY;

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public ObjectType getRequestedType() {
		return requestedType;
	}

	public void setRequestedType(ObjectType requestedType) {
		this.requestedType = requestedType;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getOnTime() {
		return onTime;
	}

	public void setOnTime(long onTime) {
		this.onTime = onTime;
	}

	public long getOffTime() {
		return offTime;
	}

	public void setOffTime(long offTime) {
		this.offTime = offTime;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public TripCloseCode getTripCloseCode() {
		return tripCloseCode;
	}

	public void setTripCloseCode(TripCloseCode tripCloseCode) {
		this.tripCloseCode = tripCloseCode;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean isQueue() {
		return queue;
	}

	public void setQueue(boolean queue) {
		this.queue = queue;
	}
}
