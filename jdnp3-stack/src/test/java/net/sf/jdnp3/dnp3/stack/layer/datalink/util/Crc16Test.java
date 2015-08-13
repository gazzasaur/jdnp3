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
package net.sf.jdnp3.dnp3.stack.layer.datalink.util;

import static java.util.Arrays.asList;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16.computeCrc;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class Crc16Test {
	@Test
	public void testConstructor() {
		new Crc16();
	}
	
	@Test
	public void testCrcGeneration() throws Exception {
		assertThat(computeCrc(new ArrayList<Byte>()), is(0xFFFF));
		assertThat(computeCrc(asList(toObject(decodeHex("b00b1e55".toCharArray())))), is(0x22BB));
	}
}
