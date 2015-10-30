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

import java.util.List;

public class MasterGast {
	private static GetterAndSetterTester getterAndSetterTester = new GetterAndSetterTester() {{
		this.addObjectFactory(new LongObjectFactory());
		this.addObjectFactory(new DoubleObjectFactory());
		this.addObjectFactory(new BooleanObjectFactory());
		this.addObjectFactory(new IntegerObjectFactory());
		this.addObjectFactory(new EnumeratorObjectFactory());
	}};
	
	public static <E> boolean testSubject(E subject, List<String> properties, Class<E> clazz) {
		return getterAndSetterTester.testSubject(subject, properties, clazz);
	}
	
	public static GetterAndSetterTester createGast(ObjectFactory... factories) {
		GetterAndSetterTester tester = new GetterAndSetterTester();
		tester.addObjectFactory(new LongObjectFactory());
		tester.addObjectFactory(new DoubleObjectFactory());
		tester.addObjectFactory(new BooleanObjectFactory());
		tester.addObjectFactory(new IntegerObjectFactory());
		tester.addObjectFactory(new EnumeratorObjectFactory());
		
		for (ObjectFactory objectFactory : factories) {
			tester.addObjectFactory(objectFactory);
		}
		return tester;
	}
}
