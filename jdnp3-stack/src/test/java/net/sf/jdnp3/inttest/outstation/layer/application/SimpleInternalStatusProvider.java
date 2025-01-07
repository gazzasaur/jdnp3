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
package net.sf.jdnp3.inttest.outstation.layer.application;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

public class SimpleInternalStatusProvider implements InternalStatusProvider {
	public void setParameterError(boolean value) {
	}

	public void setObjectUnknown(boolean value) {
	}

	public void setNoFunctionCodeSupport(boolean value) {
	}

	public void setNeedTime(boolean value) {
	}

	public void setLocalControl(boolean value) {
	}

	public void setEventBufferOverflow(boolean value) {
	}

	public void setDeviceTrouble(boolean value) {
	}

	public void setDeviceRestart(boolean value) {
	}

	public void setConfigurationCorrupt(boolean value) {
	}

	public void setClass3Events(boolean value) {
	}

	public void setClass2Events(boolean value) {
	}

	public void setClass1Events(boolean value) {
	}

	public void setBroadcast(boolean value) {
	}

	public void setAlreadyExecuting(boolean value) {
	}

	public void setUnsolicitedEnabled(boolean value) {
	}

	public boolean isParameterError() {
		return false;
	}

	public boolean isObjectUnknown() {
		return false;
	}

	public boolean isNoFunctionCodeSupport() {
		return false;
	}

	public boolean isNeedTime() {
		return false;
	}

	public boolean isLocalControl() {
		return false;
	}

	public boolean isEventBufferOverflow() {
		return false;
	}

	public boolean isDeviceTrouble() {
		return false;
	}

	public boolean isDeviceRestart() {
		return false;
	}

	public boolean isConfigurationCorrupt() {
		return false;
	}

	public boolean isClass3Events() {
		return false;
	}

	public boolean isClass2Events() {
		return false;
	}

	public boolean isClass1Events() {
		return false;
	}

	public boolean isBroadcast() {
		return false;
	}

	public boolean isAlreadyExecuting() {
		return false;
	}

	public boolean isUnsolicitedEnabled() {
		return false;
	}
}
