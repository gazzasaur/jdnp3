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
package net.sf.jdnp3.dnp3.stack.layer.application.service;

public interface InternalStatusProvider {
	public boolean isBroadcast();
	public void setBroadcast(boolean value);
	
	public boolean isClass1Events();
	public void setClass1Events(boolean value);

	public boolean isClass2Events();
	public void setClass2Events(boolean value);
	
	public boolean isClass3Events();
	public void setClass3Events(boolean value);
	
	public boolean isNeedTime();
	public void setNeedTime(boolean value);
	
	public boolean isLocalControl();
	public void setLocalControl(boolean value);
	
	public boolean isDeviceTrouble();
	public void setDeviceTrouble(boolean value);
	
	public boolean isDeviceRestart();
	public void setDeviceRestart(boolean value);
	
	public boolean isNoFunctionCodeSupport();
	public void setNoFunctionCodeSupport(boolean value);
	
	public boolean isObjectUnknown();
	public void setObjectUnknown(boolean value);
	
	public boolean isParameterError();
	public void setParameterError(boolean value);
	
	public boolean isEventBufferOverflow();
	public void setEventBufferOverflow(boolean value);
	
	public boolean isAlreadyExecuting();
	public void setAlreadyExecuting(boolean value);
	
	public boolean isConfigurationCorrupt();
	public void setConfigurationCorrupt(boolean value);

	public boolean isUnsolicitedEnabled();
	public void setUnsolicitedEnabled(boolean value);

	public boolean isEnabled();
	public void setEnabled(boolean value);
}