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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util;

public class EncoderUtils {
	public static int calculateOctetCount(long value) {
		if (value < 0) {
			throw new IndexOutOfBoundsException("A negative value is not supported.");
		} else if (value < 256) {
			return 1;
		} else if (value < 65536) {
			return 2;
		} else if (value < 4294967296L) {
			return 4;
		}
		throw new IndexOutOfBoundsException("The give value cannot fit into a 32 bit field.");
	}
}
