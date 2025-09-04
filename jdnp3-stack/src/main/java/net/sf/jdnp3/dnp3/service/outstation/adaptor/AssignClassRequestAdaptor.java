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

public class AssignClassRequestAdaptor<E extends StaticObjectInstance> implements OutstationRequestHandlerAdaptor {
	private int group;
	private Class<E> clazz;
	private AssignClassRequestHandler<E> serviceRequestHandler = null;
	
	public AssignClassRequestAdaptor(int group, Class<E> clazz) {
		this.group = group;
		this.clazz = clazz;
	}

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == FunctionCode.ASSIGN_CLASS && request.getObjectFragmentHeader().getObjectType().getGroup() == group) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (!this.canHandle(functionCode, request)) {
			throw new RuntimeException("Cannot perform assign class operation.");
		}
		
		if (serviceRequestHandler != null) {
			Range range = request.getObjectFragmentHeader().getRange();
			PrefixType prefixType = request.getObjectFragmentHeader().getPrefixType();
			
			if (range instanceof NoRange) {
				serviceRequestHandler.assignClasses();
			} else if (range instanceof IndexRange) {
				IndexRange indexRange = (IndexRange) range;
				serviceRequestHandler.assignClasses(indexRange.getStartIndex(), indexRange.getStopIndex());
			} else if (prefixType instanceof IndexPrefixType) {
				for (ObjectInstance objectInstance : request.getObjectInstances()) {
					serviceRequestHandler.assignClass(objectInstance.getIndex());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof AssignClassRequestHandler<?>) {
			AssignClassRequestHandler<?> assignClassRequestHandler = (AssignClassRequestHandler<?>) requestHandler;
			if (assignClassRequestHandler.getObjectInstanceClass().equals(clazz)) {
				this.serviceRequestHandler = (AssignClassRequestHandler<E>) assignClassRequestHandler;
			}
		}
	}
}
