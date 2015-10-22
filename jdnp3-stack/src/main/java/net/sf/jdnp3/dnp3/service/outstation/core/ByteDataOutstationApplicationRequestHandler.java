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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class ByteDataOutstationApplicationRequestHandler implements OutstationApplicationRequestHandler {
	private ObjectType objectType;
	private FunctionCode functionCode;
	private List<Byte> returnData = new ArrayList<Byte>();
	
	public ByteDataOutstationApplicationRequestHandler(ObjectType objectType, FunctionCode functionCode, List<Byte> returnData) {
		this.objectType = objectType;
		this.functionCode = functionCode;
		this.returnData = returnData;
	}
	
	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		return this.functionCode.equals(functionCode) &&
				objectType.equals(request.getObjectFragmentHeader().getObjectType());
	}

	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		ByteDataObjectInstance objectInstance = new ByteDataObjectInstance();
		objectInstance.setRequestedType(objectType);
		objectInstance.setData(returnData);
		response.add(objectInstance);
	}
}
