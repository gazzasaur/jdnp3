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
package net.sf.jdnp3.dnp3.service.outstation.adaptor;

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_COMMAND_GROUP;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.analog.AnalogOutputOperateRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputCommandObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class AnalogOutputOperateRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private AnalogOutputOperateRequestHandler serviceRequestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		// FIXME Operate currently ignores select
		if ((functionCode == DIRECT_OPERATE || functionCode == OPERATE) && request.getObjectFragmentHeader().getObjectType().getGroup() == ANALOG_OUTPUT_COMMAND_GROUP) {
			return true;
		}
		return false;
	}

	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			for (ObjectInstance objectInstance : request.getObjectInstances()) {
				response.add(serviceRequestHandler.doDirectOperate((AnalogOutputCommandObjectInstance) objectInstance));
			}
		}
	}

	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof AnalogOutputOperateRequestHandler) {
			this.serviceRequestHandler = (AnalogOutputOperateRequestHandler) requestHandler;
		}
	}
}
