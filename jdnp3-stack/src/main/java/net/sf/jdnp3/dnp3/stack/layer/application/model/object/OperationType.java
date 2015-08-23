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

public enum OperationType {
	NUL(0),
	PULSE_ON(1),
	PULSE_OFF(2),
	LATCH_ON(3),
	LATCH_OFF(4);
	
	private int code;

	private OperationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
