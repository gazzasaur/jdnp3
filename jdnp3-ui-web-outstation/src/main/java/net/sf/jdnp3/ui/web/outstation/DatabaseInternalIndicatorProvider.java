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
package net.sf.jdnp3.ui.web.outstation;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.InternalIndicatorsDataPoint;

public class DatabaseInternalIndicatorProvider implements InternalStatusProvider {
	public boolean isBroadcast() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isBroadcast();
	}
	
	public synchronized void setBroadcast(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setBroadcast(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isClass1Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass1Events();
	}
	
	public synchronized void setClass1Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setClass1Events(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isClass2Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass2Events();
	}
	
	public synchronized void setClass2Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setClass2Events(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isClass3Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass3Events();
	}
	
	public synchronized void setClass3Events(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setClass3Events(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isNeedTime() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isNeedTime();
	}
	
	public synchronized void setNeedTime(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setNeedTime(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isLocalControl() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isLocalControl();
	}
	
	public synchronized void setLocalControl(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setLocalControl(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isDeviceTrouble() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isDeviceTrouble();
	}
	
	public synchronized void setDeviceTrouble(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setDeviceTrouble(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isDeviceRestart() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isDeviceRestart();
	}
	
	public synchronized void setDeviceRestart(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setDeviceRestart(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isNoFunctionCodeSupport() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isNoFunctionCodeSupport();
	}
	
	public synchronized void setNoFunctionCodeSupport(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setNoFunctionCodeSupport(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isObjectUnknown() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isObjectUnknown();
	}
	
	public synchronized void setObjectUnknown(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setObjectUnknown(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isParameterError() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isParameterError();
	}
	
	public synchronized void setParameterError(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setParameterError(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isEventBufferOverflow() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isEventBufferOverflow();
	}
	
	public synchronized void setEventBufferOverflow(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setEventBufferOverflow(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isAlreadyExecuting() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isAlreadyExecuting();
	}
	
	public synchronized void setAlreadyExecuting(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setAlreadyExecuting(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}

	public boolean isConfigurationCorrupt() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isConfigurationCorrupt();
	}
	
	public synchronized void setConfigurationCorrupt(boolean value) {
		InternalIndicatorsDataPoint internalIndicatorsDataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		internalIndicatorsDataPoint.setConfigurationCorrupt(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(internalIndicatorsDataPoint);
	}
}
