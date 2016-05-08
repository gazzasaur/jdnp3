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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT32_DELTA;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterStaticObjectInstance;

public class CounterInteger32DeltaObjectTypeEncoder implements ObjectTypeEncoder {
	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return objectType.equals(COUNTER_STATIC_INT32_DELTA);
	}

	public void encode(ObjectFragmentEncoderContext context, ObjectInstance objectInstance, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType())) {
			throw new IllegalArgumentException(format("Cannot encode the given value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}

		CounterStaticObjectInstance specificInstance = (CounterStaticObjectInstance) objectInstance;
		data.add(CounterFlagsEncoder.encode(specificInstance));
		addInteger((long) specificInstance.getValue(), 4, data);
	}
}
