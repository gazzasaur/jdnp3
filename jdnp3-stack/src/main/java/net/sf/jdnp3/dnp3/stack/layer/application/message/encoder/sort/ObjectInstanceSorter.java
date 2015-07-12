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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.StaticObjectInstance;

public class ObjectInstanceSorter {
	public void sort(List<ObjectInstance> objectInstances) {
		Comparator<ObjectInstance> objectInstanceComparator = new Comparator<ObjectInstance>() {
			public int compare(ObjectInstance o1, ObjectInstance o2) {
				if (o1 instanceof EventObjectInstance && o2 instanceof StaticObjectInstance) {
					return -1;
				} else if (o1 instanceof StaticObjectInstance && o2 instanceof EventObjectInstance) {
					return 1;
				} else if (o1 instanceof EventObjectInstance && o2 instanceof EventObjectInstance) {
					EventObjectInstance eo1 = (EventObjectInstance) o1;
					EventObjectInstance eo2 = (EventObjectInstance) o2;
					if (eo1.getTimestamp() != eo2.getTimestamp()) {
						return Long.compare(eo1.getTimestamp(), eo2.getTimestamp());
					}
				}
				if (o1.getRequestedType().getGroup() != o2.getRequestedType().getGroup()) {
					return Integer.compare(o1.getRequestedType().getGroup(), o2.getRequestedType().getGroup());
				} else if (o1.getIndex() != o2.getIndex()) {
					return Long.compare(o1.getIndex(), o2.getIndex());
				} else {
					return Integer.compare(o1.getRequestedType().getVariation(), o2.getRequestedType().getVariation());
				}
			}
		};
		Collections.sort(objectInstances, objectInstanceComparator);
	}
}
