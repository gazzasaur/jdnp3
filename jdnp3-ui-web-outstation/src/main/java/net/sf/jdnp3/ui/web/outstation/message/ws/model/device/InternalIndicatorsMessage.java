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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.device;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;


public class InternalIndicatorsMessage implements DeviceMessage {
	private String type = "internalIndicators";
	private String site = "";
	private String device = "";
	private boolean enabled = true;
	private boolean readonly = false;
	private boolean unsolicitedEnabled = false;
	
	private boolean needTime = false;
	private boolean broadcast = false;
	private boolean class1Events = false;
	private boolean class2Events = false;
	private boolean class3Events = false;
	private boolean localControl = false;
	private boolean deviceTrouble = false;
	private boolean deviceRestart = false;
	private boolean objectUnknown = false;
	private boolean parameterError = false;
	private boolean alreadyExecuting = false;
	private boolean eventBufferOverflow = false;
	private boolean noFunctionCodeSupport = false;
	private boolean configurationCorrupt = false;
	
	private Map<String, String> tags = new HashMap<>();

	public String getType() {
		return type;
	}
	
	public boolean isNeedTime() {
		return needTime;
	}

	public void setNeedTime(boolean needTime) {
		this.needTime = needTime;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public boolean isClass1Events() {
		return class1Events;
	}

	public void setClass1Events(boolean class1Events) {
		this.class1Events = class1Events;
	}

	public boolean isClass2Events() {
		return class2Events;
	}

	public void setClass2Events(boolean class2Events) {
		this.class2Events = class2Events;
	}

	public boolean isClass3Events() {
		return class3Events;
	}

	public void setClass3Events(boolean class3Events) {
		this.class3Events = class3Events;
	}

	public boolean isLocalControl() {
		return localControl;
	}

	public void setLocalControl(boolean localControl) {
		this.localControl = localControl;
	}

	public boolean isDeviceTrouble() {
		return deviceTrouble;
	}

	public void setDeviceTrouble(boolean deviceTrouble) {
		this.deviceTrouble = deviceTrouble;
	}

	public boolean isDeviceRestart() {
		return deviceRestart;
	}

	public void setDeviceRestart(boolean deviceRestart) {
		this.deviceRestart = deviceRestart;
	}

	public boolean isObjectUnknown() {
		return objectUnknown;
	}

	public void setObjectUnknown(boolean objectUnknown) {
		this.objectUnknown = objectUnknown;
	}

	public boolean isParameterError() {
		return parameterError;
	}

	public void setParameterError(boolean parameterError) {
		this.parameterError = parameterError;
	}

	public boolean isAlreadyExecuting() {
		return alreadyExecuting;
	}

	public void setAlreadyExecuting(boolean alreadyExecuting) {
		this.alreadyExecuting = alreadyExecuting;
	}

	public boolean isEventBufferOverflow() {
		return eventBufferOverflow;
	}

	public void setEventBufferOverflow(boolean eventBufferOverflow) {
		this.eventBufferOverflow = eventBufferOverflow;
	}

	public boolean isNoFunctionCodeSupport() {
		return noFunctionCodeSupport;
	}

	public void setNoFunctionCodeSupport(boolean noFunctionCodeSupport) {
		this.noFunctionCodeSupport = noFunctionCodeSupport;
	}

	public boolean isConfigurationCorrupt() {
		return configurationCorrupt;
	}

	public void setConfigurationCorrupt(boolean configurationCorrupt) {
		this.configurationCorrupt = configurationCorrupt;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isUnsolicitedEnabled() {
		return unsolicitedEnabled;
	}

	public void setUnsolicitedEnabled(boolean unsolicitedEnabled) {
		this.unsolicitedEnabled = unsolicitedEnabled;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
