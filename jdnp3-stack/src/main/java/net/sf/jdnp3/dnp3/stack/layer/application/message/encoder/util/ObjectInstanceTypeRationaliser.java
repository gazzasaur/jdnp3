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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.DoubleBitBinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.DoubleBitBinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.TimeDelayObjectInstance;

// FIXME IMPL Update this to avoid hard coding the helpers.
public class ObjectInstanceTypeRationaliser {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectInstanceTypeRationaliser.class);
	
	private Map<Class<? extends ObjectInstance>, ObjectInstanceTypeRationaliserHelper> rationaliserHelpers = new HashMap<Class<? extends ObjectInstance>, ObjectInstanceTypeRationaliserHelper>() {{
		this.put(BinaryInputStaticObjectInstance.class, new BinaryInputStaticTypeRationaliserHelper());
		this.put(BinaryInputEventObjectInstance.class, new BinaryInputEventTypeRationaliserHelper());
		this.put(DoubleBitBinaryInputStaticObjectInstance.class, new DoubleBitBinaryInputStaticTypeRationaliserHelper());
		this.put(DoubleBitBinaryInputEventObjectInstance.class, new DoubleBitBinaryInputEventTypeRationaliserHelper());
		this.put(BinaryOutputStaticObjectInstance.class, new BinaryOutputStaticTypeRationaliserHelper());
		this.put(AnalogInputStaticObjectInstance.class, new AnalogInputStaticTypeRationaliserHelper());
		this.put(AnalogInputEventObjectInstance.class, new AnalogInputEventTypeRationaliserHelper());
		this.put(AnalogOutputStaticObjectInstance.class, new AnalogOutputStaticTypeRationaliserHelper());
		this.put(CounterStaticObjectInstance.class, new CounterStaticTypeRationaliserHelper());
		this.put(TimeDelayObjectInstance.class, new TimeDelayTypeRationaliserHelper());
	}};
	
	public void rationaliseType(ObjectInstance objectInstance) {
		ObjectInstanceTypeRationaliserHelper rationaliserHelper = rationaliserHelpers.get(objectInstance.getClass());
		if (rationaliserHelper == null) {
			LOGGER.info("No Rationaliser found for type: " + objectInstance.getClass());
		} else {
			rationaliserHelper.rationalise(objectInstance);
		}
	}
}
