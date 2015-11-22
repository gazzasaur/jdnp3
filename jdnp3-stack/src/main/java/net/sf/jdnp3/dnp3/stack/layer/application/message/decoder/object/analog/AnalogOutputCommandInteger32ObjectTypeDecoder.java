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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.analog;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_COMMAND_INT32;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputCommandObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class AnalogOutputCommandInteger32ObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(ANALOG_OUTPUT_COMMAND_INT32);
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		if (!this.canDecode(decoderContext)) {
			throw new IllegalArgumentException("Unable to decode data.");
		}
		
		AnalogOutputCommandObjectInstance command = new AnalogOutputCommandObjectInstance();
		command.setIndex(decoderContext.getCurrentIndex());
		
		long value = DataUtils.getInteger(0, 4, data);
		long statusCode = DataUtils.getInteger(4, 1, data);
		
		command.setValue(value);
		for (StatusCode status : StatusCode.values()) {
			if (status.getCode() == statusCode) {
				command.setStatusCode(status);
				break;
			}
		}
		
		DataUtils.trim(5, data);
		return command;
	}
}