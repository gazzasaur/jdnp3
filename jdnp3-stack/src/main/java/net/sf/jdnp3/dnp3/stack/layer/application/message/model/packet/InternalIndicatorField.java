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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

public class InternalIndicatorField {
	private boolean broadcast = false;
	private boolean class1Events = false;
	private boolean class2Events = false;
	private boolean class3Events = false;
	private boolean needTime = false;
	private boolean localControl = false;
	private boolean deviceTrouble = false;
	private boolean deviceRestart = false;
	private boolean noFunctionCodeSupport = false;
	private boolean objectUnknown = false;
	private boolean parameterError = false;
	private boolean eventBufferOverflow = false;
	private boolean alreadyExecuting = false;
	private boolean configurationCorrupt = false;
	
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

	public boolean isNeedTime() {
		return needTime;
	}

	public void setNeedTime(boolean needTime) {
		this.needTime = needTime;
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

	public boolean isNoFunctionCodeSupport() {
		return noFunctionCodeSupport;
	}

	public void setNoFunctionCodeSupport(boolean noFunctionCodeSupport) {
		this.noFunctionCodeSupport = noFunctionCodeSupport;
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

	public boolean isEventBufferOverflow() {
		return eventBufferOverflow;
	}

	public void setEventBufferOverflow(boolean eventBufferOverflow) {
		this.eventBufferOverflow = eventBufferOverflow;
	}

	public boolean isAlreadyExecuting() {
		return alreadyExecuting;
	}

	public void setAlreadyExecuting(boolean alreadyExecuting) {
		this.alreadyExecuting = alreadyExecuting;
	}

	public boolean isConfigurationCorrupt() {
		return configurationCorrupt;
	}

	public void setConfigurationCorrupt(boolean configurationCorrupt) {
		this.configurationCorrupt = configurationCorrupt;
	}
}
