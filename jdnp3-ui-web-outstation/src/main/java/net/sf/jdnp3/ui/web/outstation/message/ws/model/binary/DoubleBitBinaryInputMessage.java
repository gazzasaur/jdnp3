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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.binary;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;

public class DoubleBitBinaryInputMessage implements DeviceMessage {
	private String type = "DoubleBitBinaryInputPoInteger";
	private long index = 0;
	private String site = "";
	private String device = "";
	
	private Integer eventClass = null;
	private ObjectType eventType = null;
	private ObjectType staticType = null;
	private Boolean triggerEventOnChange = null;
	
	private String name = null;
	private Double value = null;
	private Boolean online = null;
	private Boolean restart = null;
	private Boolean localForced = null;
	private Boolean remoteForced = null;
	private Boolean chatterFilter = null;
	private Boolean communicationsLost = null;
	
	private Map<String, String> tags = null;

	public String getType() {
		return type;
	}
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Boolean isChatterFilter() {
		return chatterFilter;
	}

	public void setChatterFilter(Boolean chatterFilter) {
		this.chatterFilter = chatterFilter;
	}

	public Boolean isOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Boolean isRestart() {
		return restart;
	}

	public void setRestart(Boolean restart) {
		this.restart = restart;
	}

	public Boolean isLocalForced() {
		return localForced;
	}

	public void setLocalForced(Boolean localForced) {
		this.localForced = localForced;
	}

	public Boolean isRemoteForced() {
		return remoteForced;
	}

	public void setRemoteForced(Boolean remoteForced) {
		this.remoteForced = remoteForced;
	}

	public Boolean isCommunicationsLost() {
		return communicationsLost;
	}

	public void setCommunicationsLost(Boolean communicationsLost) {
		this.communicationsLost = communicationsLost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEventClass() {
		return eventClass;
	}

	public void setEventClass(Integer eventClass) {
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

	public Boolean isTriggerEventOnChange() {
		return triggerEventOnChange;
	}

	public void setTriggerEventOnChange(Boolean triggerEventOnChange) {
		this.triggerEventOnChange = triggerEventOnChange;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
