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
	ACK(0),
	NACK(1),
	LINK_STATUS(11),
	NOT_SUPPORTED(15),

	// Primary to Secondary
	RESET_LINK_STATUS(0),
	TEST_LINK(2),
	CONFIRMED_USER_DATA(3),
	UNCONFIRMED_USER_DATA(4),
	REQUEST_LINK_STATUS(9);
	
	private final int code;
	
	private FunctionCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
