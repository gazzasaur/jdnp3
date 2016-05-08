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
package net.sf.jdnp3.test.util.gast;

import org.apache.commons.lang3.RandomUtils;

public class EnumeratorObjectFactory implements ObjectFactory {

	public boolean canHandle(Class<?> clazz) {
		return clazz.isEnum();
	}

	public Object createRandomInstance(Class<?> clazz) {
		return clazz.getEnumConstants()[RandomUtils.nextInt(0, clazz.getEnumConstants().length)];
		
	}

	public boolean areEqual(Object expected, Object actual) {
		return expected.equals(actual);
	}
}
