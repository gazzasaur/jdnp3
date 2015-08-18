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
package net.sf.jdnp3.dnp3.stack.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mockit.integration.junit4.JMockit;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class DataUtilsTest {
	private Random random = new Random();
	
	@Test
	public void testConstructor() {
		new DataUtils();
	}
	
	@Test
	public void testAddInteger_Happy() {
		performAddIntegerTest_Happy(0, 0x28674C394637283BL);
		performAddIntegerTest_Happy(1, 0x28674C394637283BL, 0x3B);
		performAddIntegerTest_Happy(2, 0x06BAD5F19CF59A47L, 0x47, 0x9A);
		performAddIntegerTest_Happy(3, 0xF71A96B8B52AD86EL, 0x6E, 0xD8, 0x2A);
		performAddIntegerTest_Happy(8, 0xF36A68A3E79C9B4FL, 0x4F, 0x9B, 0x9C, 0xE7, 0xA3, 0x68, 0x6A, 0xF3);
	}

	@Test
	public void testAddInteger_Sad() {
		performAddIntegerTest_Sad(-1);
		performAddIntegerTest_Sad(9);
		performAddIntegerTest_Sad(random.nextInt(1000) + 9);
		performAddIntegerTest_Sad(-1*(random.nextInt(1000) + 1));
	}

	@Test
	public void testGetInteger_Happy() {
		performGetIntegerTest_Happy(0, 1, 0x3B, 0x3B);
		performGetIntegerTest_Happy(1, 1, 0x47L, 0x19, 0x47, 0x9A);
		performGetIntegerTest_Happy(0, 2, 0xF76AL, 0x6A, 0xF7, 0x2A);
		performGetIntegerTest_Happy(2, 8, 0xF36A68A3E79C9B4FL, 0x52, 0x34, 0x4F, 0x9B, 0x9C, 0xE7, 0xA3, 0x68, 0x6A, 0xF3, 0x8D);
		performGetIntegerTest_Happy(random.nextInt(10000) - 5000, 0, 0, generateRandomList());
	}

	@Test
	public void testGetInteger_Sad() {
		performGetIntegerTest_Sad(-1, 1, generateRandomList());
		performGetIntegerTest_Sad(-2, 2, generateRandomList());
		performGetIntegerTest_Sad(-1*random.nextInt(10000) - 1, random.nextInt(10) + 1, generateRandomList());
		performGetIntegerTest_Sad(2, 2, 0x14, 0x83, 0x12);
		performGetIntegerTest_Sad(4, 1, 0x14, 0x83, 0x12);
	}

	@Test
	public void testTrim() {
		performTrimTest(0, integerArrayToByteList(), integerArrayToByteList());
		performTrimTest(1, integerArrayToByteList(), integerArrayToByteList());
		performTrimTest(3, integerArrayToByteList(), integerArrayToByteList());
		performTrimTest(0, integerArrayToByteList(0x6A, 0x2B), integerArrayToByteList(0x6A, 0x2B));
		performTrimTest(1, integerArrayToByteList(0x6A, 0x2B), integerArrayToByteList(0x2B));
		performTrimTest(3, integerArrayToByteList(0x6A, 0x2B), integerArrayToByteList());
		performTrimTest(0, integerArrayToByteList(0x78, 0xAC, 0xF4, 0x19), integerArrayToByteList(0x78, 0xAC, 0xF4, 0x19));
		performTrimTest(1, integerArrayToByteList(0x78, 0xAC, 0xF4, 0x19), integerArrayToByteList(0xAC, 0xF4, 0x19));
		performTrimTest(3, integerArrayToByteList(0x78, 0xAC, 0xF4, 0x19), integerArrayToByteList(0x19));
	}

	private void performAddIntegerTest_Happy(int octetCount, long value, Integer... expectedBytes) {
		List<Byte> data = new ArrayList<>();
		DataUtils.addInteger(value, octetCount, data);
		assertThat(data, is(integerArrayToByteList(expectedBytes)));
	}

	private void performGetIntegerTest_Happy(int index, int octetCount, long expectedValue, Integer... actualBytes) {
		List<Byte> data = integerArrayToByteList(actualBytes);
		long actualValue = DataUtils.getInteger(index, octetCount, data);
		assertThat(actualValue, is(expectedValue));
	}

	private void performAddIntegerTest_Sad(int octetCount) {
		long value = random.nextLong();
		List<Byte> data = new ArrayList<>();
		try {
			DataUtils.addInteger(value, octetCount, data);
			fail();
		} catch (Exception e) {
			assertThat(data.size(), is(0));
		}
	}

	private void performGetIntegerTest_Sad(int index, int octetCount, Integer... integers) {
		List<Byte> data = integerArrayToByteList(integers);
		try {
			DataUtils.getInteger(index, octetCount, data);
			Assert.fail();
		} catch (Exception e) {
		}
	}

	private void performTrimTest(int trimCount, List<Byte> data, List<Byte> expectedData) {
		DataUtils.trim(trimCount, data);
		assertThat(data, CoreMatchers.is(expectedData));
	}

	private List<Byte> integerArrayToByteList(Integer... integers) {
		List<Byte> bytes = new ArrayList<>();
		for (Integer integer : integers) {
			bytes.add(integer.byteValue());
		}
		return bytes;
	}
	
	private Integer[] generateRandomList() {
		int size = random.nextInt(10);
		Integer[] bytes = new Integer[size];
		for (int i = 0; i < size; ++i) {
			bytes[i] = random.nextInt(255);
		}
		return bytes;
	}
}
