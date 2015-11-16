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

import static java.lang.String.format;

import net.sf.jdnp3.dnp3.service.outstation.handler.generic.InternalIndicatorWriteRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

public class InternalIndicatorWriter implements InternalIndicatorWriteRequestHandler {
	private InternalStatusProvider internalStatusProvider;

	public InternalIndicatorWriter(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public void doWriteIndicatorBit(long index, boolean value) {
		if (index != 7) {
			throw new IllegalArgumentException(format("Cannot write to IIN bit %d.  Only IIN index 7 may be written to.", index));
		}
		internalStatusProvider.setDeviceRestart(value);
	}
}