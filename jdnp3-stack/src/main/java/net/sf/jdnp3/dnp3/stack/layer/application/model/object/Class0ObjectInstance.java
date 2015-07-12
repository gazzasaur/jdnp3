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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CLASS_0;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public class Class0ObjectInstance implements ObjectInstance {
	private ObjectType requestedType = CLASS_0;
	
	public long getIndex() {
		return 0;
	}

	public ObjectType getRequestedType() {
		return requestedType;
	}

	public void setRequestedType(ObjectType requestedType) {
		this.requestedType = requestedType;
	}
}
