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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;

public class ObjectFragmentPackerResult {
	private boolean atCapacity = false;
	private ObjectFragment objectFragment = null;

	public boolean isAtCapacity() {
		return atCapacity;
	}

	public void setAtCapacity(boolean atCapacity) {
		this.atCapacity = atCapacity;
	}
	
	public boolean hasObjectFragment() {
		return objectFragment != null;
	}

	public ObjectFragment getObjectFragment() {
		if (objectFragment == null) {
			throw new IllegalStateException("No ObjectFragment has been created.");
		}
		return objectFragment;
	}

	public void setObjectFragment(ObjectFragment objectFragment) {
		this.objectFragment = objectFragment;
	}
}
