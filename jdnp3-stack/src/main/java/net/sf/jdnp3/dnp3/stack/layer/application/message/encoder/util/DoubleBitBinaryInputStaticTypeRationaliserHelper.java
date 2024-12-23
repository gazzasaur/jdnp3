/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_0;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.DOUBLE_BIT_BINARY_INPUT_STATIC_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.DOUBLE_BIT_BINARY_INPUT_STATIC_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.DOUBLE_BIT_BINARY_INPUT_STATIC_PACKED;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.DoubleBitBinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants;

public class DoubleBitBinaryInputStaticTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(DoubleBitBinaryInputStaticTypeRationaliserHelper.class);
	private List<ObjectType> validObjectTypes = Arrays.asList(ANY, CLASS_0, DOUBLE_BIT_BINARY_INPUT_STATIC_ANY, DOUBLE_BIT_BINARY_INPUT_STATIC_FLAGS, DOUBLE_BIT_BINARY_INPUT_STATIC_PACKED);
	
	public void rationalise(ObjectInstance objectInstance) {
		DoubleBitBinaryInputStaticObjectInstance specificInstance = (DoubleBitBinaryInputStaticObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			LOGGER.warn("Unknown object type '{}' for class '{}', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass());
			objectInstance.setRequestedType(ANY);
		}
		if (!specificInstance.isOnline()
				|| specificInstance.isRestart()
				|| specificInstance.isLocalForced()
				|| specificInstance.isRemoteForced()
				|| specificInstance.isChatterFilter()
				|| specificInstance.isCommunicationsLost()
				|| specificInstance.getRequestedType().equals(DOUBLE_BIT_BINARY_INPUT_STATIC_FLAGS)) {
			if (specificInstance.getRequestedType().equals(ObjectTypeConstants.DOUBLE_BIT_BINARY_INPUT_STATIC_PACKED)) {
				LOGGER.warn("Packed format requested but flags are required.");
			}
			objectInstance.setRequestedType(DOUBLE_BIT_BINARY_INPUT_STATIC_FLAGS);
		} else {
			objectInstance.setRequestedType(DOUBLE_BIT_BINARY_INPUT_STATIC_PACKED);
		}
	}
}
