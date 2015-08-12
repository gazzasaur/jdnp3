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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GetterAndSetterTester {
	private List<ObjectFactory> objectFactories = new ArrayList<>();
	
	public void addObjectFactory(ObjectFactory objectFactory) {
		objectFactories.add(objectFactory);
	}
	
	public <E> boolean testSubject(E subject, List<String> properties, Class<E> clazz) {
		boolean passed = true;
		for (String property : properties) {
			passed = passed && tryTestProperty(subject, clazz, property);
		}
		return passed;
	}

	private <E> boolean tryTestProperty(E subject, Class<E> clazz, String property) {
		try {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(property, clazz);
			ObjectFactory objectFactory = this.getObjectFactory(property, propertyDescriptor.getPropertyType());
			Object randomInstance = objectFactory.createRandomInstance();
			propertyDescriptor.getWriteMethod().invoke(subject, randomInstance);
			Object object = propertyDescriptor.getReadMethod().invoke(subject);
			if (!objectFactory.areEqual(randomInstance, object)) {
				System.out.println(String.format("Property %s: Expecting %s\nGot %s", property, ReflectionToStringBuilder.toString(randomInstance), ReflectionToStringBuilder.toString(object)));
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private ObjectFactory getObjectFactory(String property, Class<?> clazz) {
		for (ObjectFactory objectFactory : objectFactories) {
			if (objectFactory.canHandle(clazz)) {
				return objectFactory;
			}
		}
		throw new IllegalArgumentException(String.format("Cannot find an object factory for property '%s' of type '%s'.", property, clazz));
	}
}
