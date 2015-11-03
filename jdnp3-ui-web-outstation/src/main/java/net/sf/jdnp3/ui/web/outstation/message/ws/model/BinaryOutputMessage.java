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
package net.sf.jdnp3.ui.web.outstation.message.ws.model;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CROB_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.StatusCode.SUCCESS;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.OperationType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.StatusCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.TripCloseCode;

public class BinaryOutputMessage implements Message {
	private String type = "binaryOutputPoint";
	private long index = 0;
	private int eventClass = 1;
	private int commandEventClass = 1;
	private ObjectType eventType = ANY;
	private ObjectType staticType = ANY;
	private ObjectType commandEventType = CROB_EVENT_ANY;
	
	private String name = "";
	private boolean active = false;
	private boolean online = true;
	private boolean restart = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean communicationsLost = false;
	
	private boolean autoUpdateOnSuccess = true;
	
	private long operatedCount = 0;
	private StatusCode statusCode = SUCCESS;
	
	private long count = 0;
	private long onTime = 0;
	private long offTime = 0;
	private OperationType operationType = OperationType.NUL;
	private TripCloseCode tripCloseCode = TripCloseCode.NUL;
	
	public String getType() {
		return type;
	}
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public boolean isLocalForced() {
		return localForced;
	}

	public void setLocalForced(boolean localForced) {
		this.localForced = localForced;
	}

	public boolean isRemoteForced() {
		return remoteForced;
	}

	public void setRemoteForced(boolean remoteForced) {
		this.remoteForced = remoteForced;
	}

	public boolean isCommunicationsLost() {
		return communicationsLost;
	}

	public void setCommunicationsLost(boolean communicationsLost) {
		this.communicationsLost = communicationsLost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEventClass() {
		return eventClass;
	}

	public void setEventClass(int eventClass) {
		this.eventClass = eventClass;
	}

	public ObjectType getEventType() {
		return eventType;
	}

	public void setEventType(ObjectType eventType) {
		this.eventType = eventType;
	}

	public ObjectType getStaticType() {
		return staticType;
	}

	public void setStaticType(ObjectType staticType) {
		this.staticType = staticType;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
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

	public long getOperatedCount() {
		return operatedCount;
	}

	public void setOperatedCount(long operatedCount) {
		this.operatedCount = operatedCount;
	}

	public ObjectType getCommandEventType() {
		return commandEventType;
	}

	public void setCommandEventType(ObjectType commandEventType) {
		this.commandEventType = commandEventType;
	}

	public boolean isAutoUpdateOnSuccess() {
		return autoUpdateOnSuccess;
	}

	public void setAutoUpdateOnSuccess(boolean autoUpdateOnSuccess) {
		this.autoUpdateOnSuccess = autoUpdateOnSuccess;
	}

	public int getCommandEventClass() {
		return commandEventClass;
	}

	public void setCommandEventClass(int commandEventClass) {
		this.commandEventClass = commandEventClass;
	}
}
