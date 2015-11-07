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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary;

public enum OperationType {
	NUL(0, false),
	PULSE_ON(1, true),
	PULSE_OFF(2, false),
	LATCH_ON(3, true),
	LATCH_OFF(4, false);
	
	private int code;
	private boolean active;

	private OperationType(int code, boolean active) {
		this.code = code;
		this.active = active;
	}

	public int getCode() {
		return code;
	}

	public boolean isActive() {
		return active;
	}
}
