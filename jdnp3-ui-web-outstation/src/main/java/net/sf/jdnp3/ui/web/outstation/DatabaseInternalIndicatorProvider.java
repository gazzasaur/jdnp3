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

public class DatabaseInternalIndicatorProvider implements InternalStatusProvider {
	public boolean isBroadcast() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isBroadcast();
	}

	public boolean isClass1Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass1Events();
	}

	public boolean isClass2Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass2Events();
	}

	public boolean isClass3Events() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isClass3Events();
	}

	public boolean isNeedTime() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isNeedTime();
	}

	public boolean isLocalControl() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isLocalControl();
	}

	public boolean isDeviceTrouble() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isDeviceTrouble();
	}

	public boolean isDeviceRestart() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isDeviceRestart();
	}

	public boolean isNoFunctionCodeSupport() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isNoFunctionCodeSupport();
	}

	public boolean isObjectUnknown() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isObjectUnknown();
	}

	public boolean isParameterError() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isParameterError();
	}

	public boolean isEventBufferOverflow() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isEventBufferOverflow();
	}

	public boolean isAlreadyExecuting() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isAlreadyExecuting();
	}

	public boolean isConfigurationCorrupt() {
		return DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint().isConfigurationCorrupt();
	}
}
