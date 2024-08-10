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
package net.sf.jdnp3.dnp3.stack.utils;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.reverse;
import static org.apache.commons.lang3.ArrayUtils.toObject;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class DataUtils {
	public static void addInteger(long value, int octetCount, List<Byte> data) {
		byte[] arrayValue = ByteBuffer.allocate(8).putLong(value).array();
		ArrayUtils.reverse(arrayValue);
		data.addAll(asList(toObject(arrayValue)).subList(0, octetCount));
	}

	public static long getUnsignedInteger(int index, int octetCount, List<Byte> data) {
		byte[] rawBuffer = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < octetCount; ++i) {
			rawBuffer[i] = data.get(index + i);
		}
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}

	// FIXME Critical: This is being used for unsigned integers in some places.
	public static long getInteger(int index, int octetCount, List<Byte> data) {
		byte[] rawBuffer = { 0, 0, 0, 0, 0, 0, 0, 0 };
		if (data.get(octetCount - 1).byteValue() < 0) {
			for (int i = 0; i < rawBuffer.length; ++i) {
				rawBuffer[i] = (byte) 0xFF;
			}
		}
		for (int i = 0; i < octetCount; ++i) {
			rawBuffer[i] = data.get(index + i);
		}
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getLong();
	}
	
	public static void addFloat(float value, List<Byte> data) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		byteBuffer.putFloat(value);
		reverse(byteBuffer.array());
		data.addAll(asList(toObject(byteBuffer.array())));
	}

	public static double getFloat(int index, List<Byte> data) {
		byte[] rawBuffer = { 0, 0, 0, 0 };
		for (int i = 0; i < rawBuffer.length; ++i) {
			rawBuffer[i] = data.get(index + i);
		}
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getFloat();
	}

	public static void addDouble(double value, List<Byte> data) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		byteBuffer.putDouble(value);
		reverse(byteBuffer.array());
		data.addAll(asList(toObject(byteBuffer.array())));
	}
	
	public static double getDouble(int index, List<Byte> data) {
		byte[] rawBuffer = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < rawBuffer.length; ++i) {
			rawBuffer[i] = data.get(index + i);
		}
		ArrayUtils.reverse(rawBuffer);
		return ByteBuffer.wrap(rawBuffer).getDouble();
	}

	public static void trim(long count, List<Byte> data) {
		if (data.size() < count) {
			data.clear();
		} else if (count > 0) {
			for (int i = 0; i < count; ++i) {
				data.remove(0);
			}
		}
	}
	
	public static byte bitSetToByte(BitSet bitSet) {
		byte[] byteArray = bitSet.toByteArray();
		if (byteArray.length > 0) {
			return byteArray[0];
		}
		return 0;
	}
	
	public static String toString(List<Byte> data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Byte dataByte : data) {
			stringBuilder.append(String.format("%02X", dataByte));
		}
		return stringBuilder.toString();
	}
}
