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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.StaticReadRequestHandler;
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

public class StaticReadRequestAdaptor<E extends StaticObjectInstance> implements OutstationRequestHandlerAdaptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticReadRequestAdaptor.class);
	
	private int group;
	private Class<E> clazz;
	private StaticReadRequestHandler<E> serviceRequestHandler = null;
	
	public StaticReadRequestAdaptor(int group, Class<E> clazz) {
		this.group = group;
		this.clazz = clazz;
	}

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == FunctionCode.READ && request.getObjectFragmentHeader().getObjectType().getGroup() == group) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			List<E> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			PrefixType prefixType = request.getObjectFragmentHeader().getPrefixType();
			
			if (range instanceof NoRange) {
				result = serviceRequestHandler.readStatics();
			} else if (range instanceof IndexRange) {
				IndexRange indexRange = (IndexRange) range;
				result = serviceRequestHandler.readStatics(indexRange.getStartIndex(), indexRange.getStopIndex());
			} else if (prefixType instanceof IndexPrefixType) {
				result = new ArrayList<E>();
				for (ObjectInstance objectInstance : request.getObjectInstances()) {
					result.addAll(serviceRequestHandler.readStatic(objectInstance.getIndex()));
				}
			}
			
			if (result == null) {
				LOGGER.warn("Cannot perform a read request on the static input qith a prefix type of {} and a range of {}.", prefixType.getClass(), range.getClass());
			} else {
				for (E objectInstance : result) {
					objectInstance.setRequestedType(request.getObjectFragmentHeader().getObjectType());
					response.add(objectInstance);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof StaticReadRequestHandler<?>) {
			StaticReadRequestHandler<?> staticReadRequestHandler = (StaticReadRequestHandler<?>) requestHandler;
			if (staticReadRequestHandler.getObjectInstanceClass().equals(clazz)) {
				this.serviceRequestHandler = (StaticReadRequestHandler<E>) staticReadRequestHandler;
			}
		}
	}
}
