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

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.toObject;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class DataUtils {
	public static void addInteger8(long value, List<Byte> data) {
		byte[] arrayValue = ByteBuffer.allocate(8).putLong((int) value).array();
		ArrayUtils.reverse(arrayValue);
		data.addAll(asList(toObject(arrayValue)).subList(0, 1));
	}

	public static void addInteger16(long value, List<Byte> data) {
		byte[] arrayValue = ByteBuffer.allocate(8).putLong((int) value).array();
		ArrayUtils.reverse(arrayValue);
		data.addAll(asList(toObject(arrayValue)).subList(0, 2));
	}

	public static void addInteger32(long value, List<Byte> data) {
		byte[] arrayValue = ByteBuffer.allocate(8).putLong((int) value).array();
		ArrayUtils.reverse(arrayValue);
		data.addAll(asList(toObject(arrayValue)).subList(0, 4));
	}

	public static void addInteger(long value, int octetCount, List<Byte> data) {
		byte[] arrayValue = ByteBuffer.allocate(8).putLong(value).array();
		ArrayUtils.reverse(arrayValue);
		data.addAll(asList(toObject(arrayValue)).subList(0, octetCount));
	}
	
	public static long getInteger8(int index, List<Byte> data) {
		byte[] rawBuffer = { data.get(index), 0, 0, 0, 0, 0, 0, 0 };
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}
	
	public static long getInteger16(int index, List<Byte> data) {
		byte[] rawBuffer = { data.get(index), data.get(index + 1), 0, 0, 0, 0, 0, 0 };
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}
	
	public static long getInteger32(int index, List<Byte> data) {
		byte[] rawBuffer = { data.get(index), data.get(index + 1), data.get(index + 2), data.get(index + 3), 0, 0, 0, 0 };
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}

	public static long getInteger(int index, int octetCount, List<Byte> data) {
		byte[] rawBuffer = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < octetCount; ++i) {
			rawBuffer[i] = data.get(index + i);
		}
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}

	public static void trim(long count, List<Byte> data) {
		for (int i = 0; i < count; ++i) {
			data.remove(0);
		}
	}
}
