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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.counter;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;

public class CounterMessage implements DeviceMessage {
	private String type = "counterPoint";
	private long index = 0;
	private String site = "";
	private String device = "";
	
	private int eventClass = 1;
	private ObjectType eventType = ANY;
	private ObjectType staticType = ANY;
	private boolean triggerEventOnChange = false;
	
	private String name = "";
	private long value = 0;
	private boolean online = true;
	private boolean restart = false;
	private boolean rollover = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean discontinuity = false;
	private boolean communicationsLost = false;
	
	private Map<String, String> tags = new HashMap<>();

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

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public boolean isRollover() {
		return rollover;
	}

	public void setRollover(boolean rollover) {
		this.rollover = rollover;
	}

	public boolean isDiscontinuity() {
		return discontinuity;
	}

	public void setDiscontinuity(boolean discontinuity) {
		this.discontinuity = discontinuity;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public boolean isTriggerEventOnChange() {
		return triggerEventOnChange;
	}

	public void setTriggerEventOnChange(boolean triggerEventOnChange) {
		this.triggerEventOnChange = triggerEventOnChange;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
