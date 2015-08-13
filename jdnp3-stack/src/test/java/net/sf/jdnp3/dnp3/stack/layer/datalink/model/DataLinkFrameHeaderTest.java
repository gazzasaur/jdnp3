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
package net.sf.jdnp3.dnp3.stack.layer.datalink.model;

import static net.sf.jdnp3.test.util.gast.MasterGast.testSubject;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class DataLinkFrameHeaderTest {
	private List<String> PROPERTIES = Arrays.asList("source", "destination", "length", "checkSum", "primary", "direction", "functionCode");
	
	@Test
	public void testConstants() {
		assertThat(new DataLinkFrameHeader().getStart(), is(0x6405));
	}
	
	@Test
	public void testGettersAndSetters() {
		testSubject(new DataLinkFrameHeader(), PROPERTIES, DataLinkFrameHeader.class);
	}
}