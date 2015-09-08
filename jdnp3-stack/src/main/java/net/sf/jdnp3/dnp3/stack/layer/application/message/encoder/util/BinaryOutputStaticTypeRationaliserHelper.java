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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_OUTPUT_STATIC_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_OUTPUT_STATIC_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_OUTPUT_STATIC_PACKED;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CLASS_0;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryOutputStaticTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<ObjectType> validObjectTypes = Arrays.asList(ANY, CLASS_0, BINARY_OUTPUT_STATIC_ANY, BINARY_OUTPUT_STATIC_FLAGS, BINARY_OUTPUT_STATIC_PACKED);
	
	public void rationalise(ObjectInstance objectInstance) {
		BinaryOutputStaticObjectInstance specificInstance = (BinaryOutputStaticObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			logger.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		if (!specificInstance.isOnline()
				|| specificInstance.isRestart()
				|| specificInstance.isLocalForced()
				|| specificInstance.isRemoteForced()
				|| specificInstance.isCommunicationsLost()
				|| specificInstance.getRequestedType().equals(BINARY_OUTPUT_STATIC_FLAGS)) {
			if (specificInstance.getRequestedType().equals(BINARY_OUTPUT_STATIC_PACKED)) {
				logger.warn(format("Packed format requested but flags are required."));
			}
			objectInstance.setRequestedType(BINARY_OUTPUT_STATIC_FLAGS);
		} else {
			objectInstance.setRequestedType(BINARY_OUTPUT_STATIC_PACKED);
		}
	}
}
