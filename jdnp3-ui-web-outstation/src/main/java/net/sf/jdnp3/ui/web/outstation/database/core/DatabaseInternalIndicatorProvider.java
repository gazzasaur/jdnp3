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
package net.sf.jdnp3.ui.web.outstation.database.core;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;

public class DatabaseInternalIndicatorProvider implements InternalStatusProvider {
	private DatabaseManager databaseManager;

	public DatabaseInternalIndicatorProvider(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public boolean isBroadcast() {
		return databaseManager.getInternalIndicatorsDataPoint().isBroadcast();
	}
	
	public synchronized void setBroadcast(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isBroadcast() != value) {
			internalIndicatorsDataPoint.setBroadcast(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isClass1Events() {
		return databaseManager.getInternalIndicatorsDataPoint().isClass1Events();
	}
	
	public synchronized void setClass1Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isClass1Events() != value) {
			internalIndicatorsDataPoint.setClass1Events(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isClass2Events() {
		return databaseManager.getInternalIndicatorsDataPoint().isClass2Events();
	}
	
	public synchronized void setClass2Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isClass2Events() != value) {
			internalIndicatorsDataPoint.setClass2Events(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isClass3Events() {
		return databaseManager.getInternalIndicatorsDataPoint().isClass3Events();
	}
	
	public synchronized void setClass3Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isClass3Events() != value) {
			internalIndicatorsDataPoint.setClass3Events(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isNeedTime() {
		return databaseManager.getInternalIndicatorsDataPoint().isNeedTime();
	}
	
	public synchronized void setNeedTime(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isNeedTime() != value) {
			internalIndicatorsDataPoint.setNeedTime(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isLocalControl() {
		return databaseManager.getInternalIndicatorsDataPoint().isLocalControl();
	}
	
	public synchronized void setLocalControl(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isLocalControl() != value) {
			internalIndicatorsDataPoint.setLocalControl(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isDeviceTrouble() {
		return databaseManager.getInternalIndicatorsDataPoint().isDeviceTrouble();
	}
	
	public synchronized void setDeviceTrouble(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isDeviceTrouble() != value) {
			internalIndicatorsDataPoint.setDeviceTrouble(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isDeviceRestart() {
		return databaseManager.getInternalIndicatorsDataPoint().isDeviceRestart();
	}
	
	public synchronized void setDeviceRestart(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isDeviceRestart() != value) {
			internalIndicatorsDataPoint.setDeviceRestart(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isNoFunctionCodeSupport() {
		return databaseManager.getInternalIndicatorsDataPoint().isNoFunctionCodeSupport();
	}
	
	public synchronized void setNoFunctionCodeSupport(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isNoFunctionCodeSupport() != value) {
			internalIndicatorsDataPoint.setNoFunctionCodeSupport(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isObjectUnknown() {
		return databaseManager.getInternalIndicatorsDataPoint().isObjectUnknown();
	}
	
	public synchronized void setObjectUnknown(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isObjectUnknown() != value) {
			internalIndicatorsDataPoint.setObjectUnknown(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isParameterError() {
		return databaseManager.getInternalIndicatorsDataPoint().isParameterError();
	}
	
	public synchronized void setParameterError(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isParameterError() != value) {
			internalIndicatorsDataPoint.setParameterError(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isEventBufferOverflow() {
		return databaseManager.getInternalIndicatorsDataPoint().isEventBufferOverflow();
	}
	
	public synchronized void setEventBufferOverflow(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isEventBufferOverflow() != value) {
			internalIndicatorsDataPoint.setEventBufferOverflow(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isAlreadyExecuting() {
		return databaseManager.getInternalIndicatorsDataPoint().isAlreadyExecuting();
	}
	
	public synchronized void setAlreadyExecuting(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isAlreadyExecuting() != value) {
			internalIndicatorsDataPoint.setAlreadyExecuting(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isConfigurationCorrupt() {
		return databaseManager.getInternalIndicatorsDataPoint().isConfigurationCorrupt();
	}
	
	public synchronized void setConfigurationCorrupt(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly()) {
			return;
		}
		if (internalIndicatorsDataPoint.isConfigurationCorrupt() != value) {
			internalIndicatorsDataPoint.setConfigurationCorrupt(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isEnabled() {
		return databaseManager.getInternalIndicatorsDataPoint().isEnabled();
	}
	
	public synchronized void setEnabled(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isEnabled() != value) {
			internalIndicatorsDataPoint.setEnabled(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isReadonly() {
		return databaseManager.getInternalIndicatorsDataPoint().isReadonly();
	}
	
	public synchronized void setReadonly(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isReadonly() != value) {
			internalIndicatorsDataPoint.setReadonly(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public boolean isUnsolicitedEnabled() {
		return databaseManager.getInternalIndicatorsDataPoint().isUnsolicitedEnabled();
	}

	public void setUnsolicitedEnabled(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.isUnsolicitedEnabled() != value) {
			internalIndicatorsDataPoint.setUnsolicitedEnabled(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}

	public long getTimestampOffset() {
		return databaseManager.getInternalIndicatorsDataPoint().getTimestampOffset();
	}

	public synchronized void setTimestampOffset(long value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
		if (internalIndicatorsDataPoint.getTimestampOffset() != value) {
			internalIndicatorsDataPoint.setTimestampOffset(value);
			databaseManager.setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
		}
	}
}
