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
package net.sf.jdnp3.dnp3.service.outstation.core;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class ByteDataOutstationApplicationRequestHandler implements OutstationApplicationRequestHandler {
	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		return (request.getObjectInstances().size() > 0) && (request.getObjectInstances().get(0) instanceof ByteDataObjectInstance);
	}

	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		for (ObjectInstance objectInstance : request.getObjectInstances()) {
			response.add(objectInstance);
		}
	}
}
