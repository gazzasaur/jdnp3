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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object.core;

public enum StatusCode {
	SUCCESS(0),
	TIMEOUT(1),
	NO_SELECT(2),
	FORMAT_ERROR(3),
	NOT_SUPPORTED(4),
	ALREADY_ACTIVE(5),
	HARDWARE_ERROR(6),
	LOCAL(7),
	TOO_MANY_OBJS(8),
	NOT_AUTHORIZED(9),
	AUTOMATION_INHIBIT(10),
	PROCESSING_LIMITED(11),
	OUT_OF_RANGE(12),
	NON_PARTICIPATING(126);
	
	private int code;

	private StatusCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
