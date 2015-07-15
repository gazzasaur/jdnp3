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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.sort;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectInstanceTypeRationaliser {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("serial")
	private Map<Class<? extends ObjectInstance>, ObjectInstanceTypeRationaliserHelper> rationaliserHelpers = new HashMap<Class<? extends ObjectInstance>, ObjectInstanceTypeRationaliserHelper>() {{
		this.put(BinaryInputStaticObjectInstance.class, new BinaryInputStaticTypeRationaliserHelper());
		this.put(BinaryInputEventObjectInstance.class, new BinaryInputEventTypeRationaliserHelper());
	}};
	
	public void rationaliseType(ObjectInstance objectInstance) {
		ObjectInstanceTypeRationaliserHelper rationaliserHelper = rationaliserHelpers.get(objectInstance.getClass());
		if (rationaliserHelper == null) {
			logger.warn("No Rationaliser found for type: " + objectInstance.getClass());
		}
		rationaliserHelper.rationalise(objectInstance);
	}
}
