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
package net.sf.jdnp3.dnp3.stack.layer.datalink.model;

public enum FunctionCode {
	// Secondary to Primary
	ACK(false, 0),
	NACK(false, 1),
	LINK_STATUS(false, 11),
	NOT_SUPPORTED(false, 15),

	// Primary to Secondary
	RESET_LINK_STATUS(true, 0),
	TEST_LINK_STATES(true, 2),
	CONFIRMED_USER_DATA(true, 3),
	UNCONFIRMED_USER_DATA(true, 4),
	REQUEST_LINK_STATUS(true, 9);
	
	private final int code;
	private final boolean primary;
	
	private FunctionCode(boolean primary, int code) {
		this.primary = primary;
		this.code = code;
	}

	public boolean isPrimary() {
		return primary;
	}

	public int getCode() {
		return code;
	}
}
