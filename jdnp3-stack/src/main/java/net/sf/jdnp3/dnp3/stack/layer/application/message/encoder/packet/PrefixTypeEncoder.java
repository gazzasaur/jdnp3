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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger;

import java.util.Deque;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class PrefixTypeEncoder {
	public static void encode(PrefixType prefixType, ObjectInstance objectInstance, Deque<Byte> data) {
		if (prefixType instanceof NoPrefixType) {
		} else if (prefixType instanceof IndexPrefixType) {
			addInteger(objectInstance.getIndex(), prefixType.getOctetCount(), data);
		} else {
			throw new IllegalArgumentException("Cannot encode index of type " + prefixType.getClass());
		}
	}
}
