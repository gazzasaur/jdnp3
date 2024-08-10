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
package net.sf.jdnp3.ui.web.outstation.database.point.analog;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_COMMAND_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode.SUCCESS;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;

public class AnalogOutputDataPoint implements DataPoint {
	private long index = -1;
	private String name = "";
	private double value = 0;
	
	private boolean online = true;
	private boolean restart = false;
	private boolean overRange = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean referenceError = false;
	private boolean communicationsLost = false;
	
	private long operatedCount = 0;
	private StatusCode statusCode = SUCCESS;
	private boolean autoUpdateOnSuccess = true;
	
	private ObjectType staticType = ANALOG_OUTPUT_STATIC_ANY;
	private ObjectType eventType = ANALOG_OUTPUT_EVENT_ANY;
	private ObjectType commandEventType = ANALOG_OUTPUT_COMMAND_EVENT_ANY;
	private int eventClass = 1;
	private int commandEventClass = 1;
	
	private Map<String, String> tags = new HashMap<>();
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public ObjectType getStaticType() {
		return staticType;
	}

	public void setStaticType(ObjectType staticType) {
		this.staticType = staticType;
	}

	public ObjectType getEventType() {
		return eventType;
	}

	public void setEventType(ObjectType eventType) {
		this.eventType = eventType;
	}

	public int getEventClass() {
		return eventClass;
	}

	public void setEventClass(int eventClass) {
		this.eventClass = eventClass;
	}

	public boolean isOverRange() {
		return overRange;
	}

	public void setOverRange(boolean overRange) {
		this.overRange = overRange;
	}

	public boolean isReferenceError() {
		return referenceError;
	}

	public void setReferenceError(boolean referenceError) {
		this.referenceError = referenceError;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getOperatedCount() {
		return operatedCount;
	}

	public void setOperatedCount(long operatedCount) {
		this.operatedCount = operatedCount;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isAutoUpdateOnSuccess() {
		return autoUpdateOnSuccess;
	}

	public void setAutoUpdateOnSuccess(boolean autoUpdateOnSuccess) {
		this.autoUpdateOnSuccess = autoUpdateOnSuccess;
	}

	public ObjectType getCommandEventType() {
		return commandEventType;
	}

	public void setCommandEventType(ObjectType commandEventType) {
		this.commandEventType = commandEventType;
	}

	public int getCommandEventClass() {
		return commandEventClass;
	}

	public void setCommandEventClass(int commandEventClass) {
		this.commandEventClass = commandEventClass;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
