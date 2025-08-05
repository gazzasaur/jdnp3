/**
 * Copyright 2025 Graeme Farquharson
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

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_0;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.AssignClassRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class AssignClassObjectRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private int group;
	private Class<E> clazz;
	private AssignClassObjectRequestHandler serviceRequestHandler = null;
	
	public AssignClassObjectRequestAdaptor() {
	}

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == FunctionCode.ASSIGN_CLASS && request.getObjectFragmentHeader().getObjectType().getGroup() == CLASS_0.getGroup()) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (!this.canHandle(functionCode, request)) {
			throw new RuntimeException("Cannot perform assign class operation.");
		}
		
		long eventClass = request.getObjectFragmentHeader().getObjectType().getVariation() - 1;
		if (eventClass < 0 || eventClass > 3) {
			throw new RuntimeException("Illegal class code received: " + eventClass);
		}
		
		if (serviceRequestHandler != null) {
			serviceRequestHandler.assignClasses(eventClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof AssignClassObjectRequestHandler) {
			AssignClassObjectRequestHandler assignClassRequestHandler = (AssignClassObjectRequestHandler) requestHandler;
			this.serviceRequestHandler = (AssignClassRequestHandler<E>) assignClassRequestHandler;
		}
	}
}
