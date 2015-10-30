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
package net.sf.jdnp3.dnp3.stack.message;

import static java.util.Arrays.asList;

import java.util.List;

import mockit.integration.junit4.JMockit;
import net.sf.jdnp3.test.util.gast.GetterAndSetterTester;
import net.sf.jdnp3.test.util.gast.MasterGast;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class MessagePropertiesTest {
	private static final List<String> PROPERTIES = asList("sourceAddress", "destinationAddress", "master", "timeReceived", "channelId");
	private GetterAndSetterTester gast = MasterGast.createGast(new ChannelIdObjectFactory());
	
	@Test
	public void testGettersAndSetters() {
		gast.testSubject(new MessageProperties(), PROPERTIES, MessageProperties.class);
	}
}
