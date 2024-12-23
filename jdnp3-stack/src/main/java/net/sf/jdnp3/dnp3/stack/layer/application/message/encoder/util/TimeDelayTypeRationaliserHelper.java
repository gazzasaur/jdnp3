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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.TIME_DELAY_COARSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.TIME_DELAY_FINE;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.TimeDelayObjectInstance;

public class TimeDelayTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeDelayTypeRationaliserHelper.class);
	private List<ObjectType> validObjectTypes = Arrays.asList(TIME_DELAY_FINE, TIME_DELAY_COARSE);
	
	public void rationalise(ObjectInstance objectInstance) {
		TimeDelayObjectInstance specificInstance = (TimeDelayObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			LOGGER.warn(format("Unknown object type '%s' for class '%s', setting to TimeDelayFine.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(TIME_DELAY_FINE);
		}
		if (specificInstance.getRequestedType().equals(ObjectTypeConstants.TIME_DELAY_FINE) && specificInstance.getTimestamp() > 0xFFFF) {
			LOGGER.warn(format("TimeDelayFine was requested but a delay of %d is required.  Using time delay coarse instead.", specificInstance.getTimestamp()));
			specificInstance.setRequestedType(ObjectTypeConstants.TIME_DELAY_COARSE);
		}
	}
}
