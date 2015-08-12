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
package net.sf.jdnp3.test.util.gast;

import org.apache.commons.lang3.RandomUtils;

public class DoubleObjectFactory implements ObjectFactory {

	public boolean canHandle(Class<?> clazz) {
		return clazz.equals(Double.class) || clazz.equals(double.class);
	}

	public Object createRandomInstance() {
		return RandomUtils.nextDouble(-1.0 * Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public boolean areEqual(Object expected, Object actual) {
		return expected.equals(actual);
	}

}
