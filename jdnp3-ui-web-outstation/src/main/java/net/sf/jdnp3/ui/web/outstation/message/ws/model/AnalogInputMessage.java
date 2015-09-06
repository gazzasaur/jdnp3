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
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public class AnalogInputMessage implements Message {
	private String type = "analogInputPoint";
	private long index = 0;
	private int eventClass = 1;
	private ObjectType eventType = ANY;
	private ObjectType staticType = ANY;
	
	private String name = "";
	private String value = "0";
	private boolean online = true;
	private boolean restart = false;
	private boolean overRange = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean referenceError = false;
	private boolean communicationsLost = false;
	
	public String getType() {
		return type;
	}
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
