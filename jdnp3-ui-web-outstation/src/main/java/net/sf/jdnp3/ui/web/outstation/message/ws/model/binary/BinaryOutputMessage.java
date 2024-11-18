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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_COMMAND_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode.SUCCESS;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.OperationType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.TripCloseCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;

public class BinaryOutputMessage implements DeviceMessage {
	private String type = "binaryOutputPoInteger";
	private long index = 0;
	private String site = "";
	private String device = "";
	
	private Integer eventClass = null;
	private Integer commandEventClass = null;
	private ObjectType eventType = null;
	private ObjectType staticType = null;
	private Boolean triggerEventOnChange = null;
	private ObjectType commandEventType = null;
	
	private String name = null;
	private Boolean active = null;
	private Boolean online = null;
	private Boolean restart = null;
	private Boolean localForced = null;
	private Boolean remoteForced = null;
	private Boolean communicationsLost = null;
	
	private Boolean autoUpdateOnSuccess = null;
	
	private Long operatedCount = null;
	private StatusCode statusCode = null;
	
	private Long count = null;
	private Long onTime = null;
	private Long offTime = null;
	private OperationType operationType = null;
	private TripCloseCode tripCloseCode = null;
	
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
	
	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getOnTime() {
		return onTime;
	}

	public void setOnTime(Long onTime) {
		this.onTime = onTime;
	}

	public Long getOffTime() {
		return offTime;
	}

	public void setOffTime(Long offTime) {
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

	public Long getOperatedCount() {
		return operatedCount;
	}

	public void setOperatedCount(Long operatedCount) {
		this.operatedCount = operatedCount;
	}

	public ObjectType getCommandEventType() {
		return commandEventType;
	}

	public void setCommandEventType(ObjectType commandEventType) {
		this.commandEventType = commandEventType;
	}

	public Boolean isAutoUpdateOnSuccess() {
		return autoUpdateOnSuccess;
	}

	public void setAutoUpdateOnSuccess(Boolean autoUpdateOnSuccess) {
		this.autoUpdateOnSuccess = autoUpdateOnSuccess;
	}

	public Integer getCommandEventClass() {
		return commandEventClass;
	}

	public void setCommandEventClass(Integer commandEventClass) {
		this.commandEventClass = commandEventClass;
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
