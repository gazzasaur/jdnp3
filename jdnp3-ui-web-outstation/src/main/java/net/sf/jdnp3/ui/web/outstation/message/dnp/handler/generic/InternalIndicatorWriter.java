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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic;

import net.sf.jdnp3.dnp3.service.outstation.handler.generic.InternalIndicatorWriteRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

public class InternalIndicatorWriter implements InternalIndicatorWriteRequestHandler {
	private InternalStatusProvider internalStatusProvider;

	public InternalIndicatorWriter(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public void doWriteIndicatorBit(long index, boolean value) {
		if (index != 7) {
			// FIXME IMPL Move this to the adaptor and use a defined exception (parameter error).
			throw new IllegalArgumentException("Only IIN index 7 may be written to.");
		}
		internalStatusProvider.setDeviceRestart(value);
	}
}