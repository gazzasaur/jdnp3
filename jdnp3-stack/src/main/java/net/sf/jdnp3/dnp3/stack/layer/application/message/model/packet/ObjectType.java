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

public class ObjectType {
	private int group = 0;
	private int variation = 0;

	public ObjectType() {
	}

	public ObjectType(int group, int variation) {
		this.group = group;
		this.variation = variation;
	}
	
	public int getGroup() {
		return group;
	}
	
	public int getVariation() {
		return variation;
	}

	public int hashCode() {
		return Integer.valueOf(group).hashCode() + Integer.valueOf(variation).hashCode();
	}
	
	public boolean equals(Object object) {
		if (object instanceof ObjectType) {
			ObjectType other = (ObjectType)object;
			return group == other.group && variation == other.variation;
		}
		return false;
	}
	
	public String toString() {
		return String.format("Group: %s, Variation: %s", group, variation);
	}
}
