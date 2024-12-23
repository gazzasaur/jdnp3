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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT16;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT16_DELTA;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT16_DELTA_NO_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT16_NO_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT32;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT32_DELTA;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT32_DELTA_NO_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_INT32_NO_FLAGS;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterStaticObjectInstance;

public class CounterStaticTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterStaticTypeRationaliserHelper.class);
	private List<ObjectType> validObjectTypes = Arrays.asList(COUNTER_STATIC_INT32, COUNTER_STATIC_INT16, COUNTER_STATIC_INT32_DELTA, COUNTER_STATIC_INT16_DELTA, COUNTER_STATIC_INT32_NO_FLAGS, COUNTER_STATIC_INT16_NO_FLAGS, COUNTER_STATIC_INT32_DELTA_NO_FLAGS, COUNTER_STATIC_INT16_DELTA_NO_FLAGS);
	
	public void rationalise(ObjectInstance objectInstance) {
		CounterStaticObjectInstance specificInstance = (CounterStaticObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			LOGGER.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		boolean otherFlags = !specificInstance.isOnline() ||
				specificInstance.isRestart() ||
				specificInstance.isCommunicationsLost() ||
				specificInstance.isRemoteForced() ||
				specificInstance.isLocalForced() ||
				specificInstance.isRollover() ||
				specificInstance.isDiscontinuity();
		if (specificInstance.getRequestedType().getGroup() != COUNTER_STATIC_GROUP || specificInstance.getRequestedType().getVariation() == 0) {
			objectInstance.setRequestedType(COUNTER_STATIC_INT32);
		}
		
		if (specificInstance.getRequestedType().equals(COUNTER_STATIC_INT32_NO_FLAGS) && otherFlags) {
			LOGGER.warn(format(COUNTER_STATIC_INT32_NO_FLAGS.toString() + " format requested but flags are required."));
			specificInstance.setRequestedType(COUNTER_STATIC_INT32);
		}
		if (specificInstance.getRequestedType().equals(COUNTER_STATIC_INT16_NO_FLAGS) && otherFlags) {
			LOGGER.warn(format(COUNTER_STATIC_INT16_NO_FLAGS.toString() + " format requested but flags are required."));
			specificInstance.setRequestedType(COUNTER_STATIC_INT16);
		}
		if (specificInstance.getRequestedType().equals(COUNTER_STATIC_INT32_DELTA_NO_FLAGS) && otherFlags) {
			LOGGER.warn(format(COUNTER_STATIC_INT32_DELTA_NO_FLAGS.toString() + " format requested but flags are required."));
			specificInstance.setRequestedType(COUNTER_STATIC_INT32_DELTA_NO_FLAGS);
		}
		if (specificInstance.getRequestedType().equals(COUNTER_STATIC_INT16_DELTA_NO_FLAGS) && otherFlags) {
			LOGGER.warn(format(COUNTER_STATIC_INT16_DELTA_NO_FLAGS.toString() + " format requested but flags are required."));
			specificInstance.setRequestedType(COUNTER_STATIC_INT16_DELTA_NO_FLAGS);
		}
	}
}
