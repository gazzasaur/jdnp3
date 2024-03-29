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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class ObjectFragment {
	private ObjectFragmentHeader objectFragmentHeader = new ObjectFragmentHeader();
	private List<ObjectInstance> objectInstances = new ArrayList<>();
	
	public ObjectFragmentHeader getObjectFragmentHeader() {
		return objectFragmentHeader;
	}
	
	public void setObjectFragmentHeader(ObjectFragmentHeader objectFragmentHeader) {
		this.objectFragmentHeader = objectFragmentHeader;
	}

	public List<ObjectInstance> getObjectInstances() {
		return unmodifiableList(objectInstances);
	}

	public void addObjectInstance(ObjectInstance objectInstance) {
		objectInstances.add(objectInstance);
	}
}
