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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.RESPONSE;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;

public class ObjectFragmentPackerContext {
	private long freeSpace = 0;
	private long timeReference = 0;
	private FunctionCode functionCode = RESPONSE;

	public long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	public FunctionCode getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(FunctionCode functionCode) {
		this.functionCode = functionCode;
	}

	public long getTimeReference() {
		return timeReference;
	}

	public void setTimeReference(long timeReference) {
		this.timeReference = timeReference;
	}
}