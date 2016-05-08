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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic;

import net.sf.jdnp3.dnp3.service.outstation.handler.generic.InternalIndicatorWriteRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

public class InternalIndicatorWriter implements InternalIndicatorWriteRequestHandler {
	private InternalStatusProvider internalStatusProvider;

	public InternalIndicatorWriter(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public void doWriteIndicatorBit(long index, boolean value) {
		switch ((int) index) {
		case 0:
			internalStatusProvider.setBroadcast(value);
			break;
		case 1:
			internalStatusProvider.setClass1Events(value);
			break;
		case 2:
			internalStatusProvider.setClass2Events(value);
			break;
		case 3:
			internalStatusProvider.setClass3Events(value);
			break;
		case 4:
			internalStatusProvider.setNeedTime(value);
			break;
		case 5:
			internalStatusProvider.setLocalControl(value);
			break;
		case 6:
			internalStatusProvider.setDeviceTrouble(value);
			break;
		case 7:
			internalStatusProvider.setDeviceRestart(value);
			break;
		case 8:
			internalStatusProvider.setNoFunctionCodeSupport(value);
			break;
		case 9:
			internalStatusProvider.setObjectUnknown(value);
			break;
		case 10:
			internalStatusProvider.setParameterError(value);
			break;
		case 11:
			internalStatusProvider.setEventBufferOverflow(value);
			break;
		case 12:
			internalStatusProvider.setAlreadyExecuting(value);
			break;
		case 13:
			internalStatusProvider.setConfigurationCorrupt(value);
			break;

		default:
			break;
		}
	}
}