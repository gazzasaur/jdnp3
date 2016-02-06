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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object.core;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public class ObjectTypeConstants {
	public static ObjectType ANY = new ObjectType(-1, -1);
	public static ObjectType CUSTOM = new ObjectType(-100, -100);
	
	public static int BINARY_INPUT_STATIC_GROUP = 1;
	public static ObjectType BINARY_INPUT_STATIC_ANY = new ObjectType(BINARY_INPUT_STATIC_GROUP, 0);
	public static ObjectType BINARY_INPUT_STATIC_PACKED = new ObjectType(BINARY_INPUT_STATIC_GROUP, 1);
	public static ObjectType BINARY_INPUT_STATIC_FLAGS = new ObjectType(BINARY_INPUT_STATIC_GROUP, 2);
	
	public static int BINARY_INPUT_EVENT_GROUP = 2;
	public static ObjectType BINARY_INPUT_EVENT_ANY = new ObjectType(BINARY_INPUT_EVENT_GROUP, 0);
	public static ObjectType BINARY_INPUT_EVENT_WITHOUT_TIME = new ObjectType(BINARY_INPUT_EVENT_GROUP, 1);
	public static ObjectType BINARY_INPUT_EVENT_ABSOLUTE_TIME = new ObjectType(BINARY_INPUT_EVENT_GROUP, 2);
	public static ObjectType BINARY_INPUT_EVENT_RELATIVE_TIME = new ObjectType(BINARY_INPUT_EVENT_GROUP, 3);
	
	public static int BINARY_OUTPUT_STATIC_GROUP = 10;
	public static ObjectType BINARY_OUTPUT_STATIC_ANY = new ObjectType(10, 0);
	public static ObjectType BINARY_OUTPUT_STATIC_PACKED = new ObjectType(10, 1);
	public static ObjectType BINARY_OUTPUT_STATIC_FLAGS = new ObjectType(10, 2);

	public static int BINARY_OUTPUT_EVENT_GROUP = 11;
	public static ObjectType BINARY_OUTPUT_EVENT_ANY = new ObjectType(11, 0);
	public static ObjectType BINARY_OUTPUT_EVENT_WITHOUT_TIME = new ObjectType(11, 1);
	public static ObjectType BINARY_OUTPUT_EVENT_ABSOLUTE_TIME = new ObjectType(11, 2);

	public static ObjectType BINARY_OUTPUT_COMMAND_CROB = new ObjectType(12, 1);

	public static ObjectType BINARY_OUTPUT_COMMAND_EVENT_ANY = new ObjectType(13, 0);
	public static ObjectType BINARY_OUTPUT_COMMAND_EVENT_WITHOUT_TIME = new ObjectType(13, 1);
	public static ObjectType BINARY_OUTPUT_COMMAND_EVENT_ABSOLUTE_TIME = new ObjectType(13, 2);
	
	public static int COUNTER_STATIC_GROUP = 20;
	public static ObjectType COUNTER_STATIC_ANY = new ObjectType(COUNTER_STATIC_GROUP, 0);
	public static ObjectType COUNTER_STATIC_INT32 = new ObjectType(COUNTER_STATIC_GROUP, 1);
	public static ObjectType COUNTER_STATIC_INT16 = new ObjectType(COUNTER_STATIC_GROUP, 2);
	public static ObjectType COUNTER_STATIC_INT32_DELTA = new ObjectType(COUNTER_STATIC_GROUP, 3);
	public static ObjectType COUNTER_STATIC_INT16_DELTA = new ObjectType(COUNTER_STATIC_GROUP, 4);
	public static ObjectType COUNTER_STATIC_INT32_NO_FLAGS = new ObjectType(COUNTER_STATIC_GROUP, 5);
	public static ObjectType COUNTER_STATIC_INT16_NO_FLAGS = new ObjectType(COUNTER_STATIC_GROUP, 6);
	public static ObjectType COUNTER_STATIC_INT32_DELTA_NO_FLAGS = new ObjectType(COUNTER_STATIC_GROUP, 7);
	public static ObjectType COUNTER_STATIC_INT16_DELTA_NO_FLAGS = new ObjectType(COUNTER_STATIC_GROUP, 8);

	public static int COUNTER_EVENT_GROUP = 22;
	public static ObjectType COUNTER_EVENT_ANY = new ObjectType(COUNTER_EVENT_GROUP, 0);
	public static ObjectType COUNTER_EVENT_INT32_FLAGS = new ObjectType(COUNTER_EVENT_GROUP, 1);
	public static ObjectType COUNTER_EVENT_INT16_FLAGS = new ObjectType(COUNTER_EVENT_GROUP, 2);
	public static ObjectType COUNTER_EVENT_INT32_FLAGS_DELTA = new ObjectType(COUNTER_EVENT_GROUP, 3);
	public static ObjectType COUNTER_EVENT_INT16_FLAGS_DELTA = new ObjectType(COUNTER_EVENT_GROUP, 4);
	public static ObjectType COUNTER_EVENT_INT32_FLAGS_TIME = new ObjectType(COUNTER_EVENT_GROUP, 5);
	public static ObjectType COUNTER_EVENT_INT16_FLAGS_TIME = new ObjectType(COUNTER_EVENT_GROUP, 6);
	public static ObjectType COUNTER_EVENT_INT32_FLAGS_DELTA_TIME = new ObjectType(COUNTER_EVENT_GROUP, 7);
	public static ObjectType COUNTER_EVENT_INT16_FLAGS_DELTA_TIME = new ObjectType(COUNTER_EVENT_GROUP, 8);

	public static ObjectType ANALOG_INPUT_STATIC_ANY = new ObjectType(30, 0);
	public static ObjectType ANALOG_INPUT_STATIC_INT32 = new ObjectType(30, 1);
	public static ObjectType ANALOG_INPUT_STATIC_INT16 = new ObjectType(30, 2);
	public static ObjectType ANALOG_INPUT_STATIC_INT32_NO_FLAGS = new ObjectType(30, 3);
	public static ObjectType ANALOG_INPUT_STATIC_INT16_NO_FLAGS = new ObjectType(30, 4);
	public static ObjectType ANALOG_INPUT_STATIC_FLOAT32 = new ObjectType(30, 5);
	public static ObjectType ANALOG_INPUT_STATIC_FLOAT64 = new ObjectType(30, 6);

	public static ObjectType ANALOG_INPUT_EVENT_ANY = new ObjectType(32, 0);
	public static ObjectType ANALOG_INPUT_EVENT_INT32_WITHOUT_TIME = new ObjectType(32, 1);
	public static ObjectType ANALOG_INPUT_EVENT_INT16_WITHOUT_TIME = new ObjectType(32, 2);
	public static ObjectType ANALOG_INPUT_EVENT_INT32_ABSOLUTE_TIME = new ObjectType(32, 3);
	public static ObjectType ANALOG_INPUT_EVENT_INT16_ABSOLUTE_TIME = new ObjectType(32, 4);
	public static ObjectType ANALOG_INPUT_EVENT_FLOAT32_WITHOUT_TIME = new ObjectType(32, 5);
	public static ObjectType ANALOG_INPUT_EVENT_FLOAT64_WITHOUT_TIME = new ObjectType(32, 6);
	public static ObjectType ANALOG_INPUT_EVENT_FLOAT32_ABSOLUTE_TIME = new ObjectType(32, 7);
	public static ObjectType ANALOG_INPUT_EVENT_FLOAT64_ABSOLUTE_TIME = new ObjectType(32, 8);
	
	public static int ANALOG_OUTPUT_STATIC_GROUP = 40;
	public static ObjectType ANALOG_OUTPUT_STATIC_ANY = new ObjectType(40, 0);
	public static ObjectType ANALOG_OUTPUT_STATIC_INT32 = new ObjectType(40, 1);
	public static ObjectType ANALOG_OUTPUT_STATIC_INT16 = new ObjectType(40, 2);
	public static ObjectType ANALOG_OUTPUT_STATIC_FLOAT32 = new ObjectType(40, 3);
	public static ObjectType ANALOG_OUTPUT_STATIC_FLOAT64 = new ObjectType(40, 4);
	
	public static int ANALOG_OUTPUT_COMMAND_GROUP = 41;
	public static ObjectType ANALOG_OUTPUT_COMMAND_INT32 = new ObjectType(41, 1);
	public static ObjectType ANALOG_OUTPUT_COMMAND_INT16 = new ObjectType(41, 2);
	public static ObjectType ANALOG_OUTPUT_COMMAND_FLOAT32 = new ObjectType(41, 3);
	public static ObjectType ANALOG_OUTPUT_COMMAND_FLOAT64 = new ObjectType(41, 4);
	
	public static int ANALOG_OUTPUT_EVENT_GROUP = 42;
	public static ObjectType ANALOG_OUTPUT_EVENT_ANY = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 0);
	public static ObjectType ANALOG_OUTPUT_EVENT_INT32_WITHOUT_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 2);
	public static ObjectType ANALOG_OUTPUT_EVENT_INT16_WITHOUT_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 2);
	public static ObjectType ANALOG_OUTPUT_EVENT_INT32_WITH_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 3);
	public static ObjectType ANALOG_OUTPUT_EVENT_INT16_WITH_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 4);
	public static ObjectType ANALOG_OUTPUT_EVENT_FLOAT32_WITHOUT_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 5);
	public static ObjectType ANALOG_OUTPUT_EVENT_FLOAT64_WITHOUT_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 6);
	public static ObjectType ANALOG_OUTPUT_EVENT_FLOAT32_WITH_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 7);
	public static ObjectType ANALOG_OUTPUT_EVENT_FLOAT64_WITH_TIME = new ObjectType(ANALOG_OUTPUT_EVENT_GROUP, 8);
	
	public static final ObjectType ANALOG_OUTPUT_COMMAND_EVENT_ANY = new ObjectType(43, 0);
	
	public static ObjectType TIME_AND_DATE_ABSOLUTE_TIME = new ObjectType(50, 1);

	public static ObjectType SYNCHRONISED_CTO = new ObjectType(51, 1);
	public static ObjectType UNSYNCHRONISED_CTO = new ObjectType(51, 2);
	
	public static ObjectType TIME_DELAY_COARSE = new ObjectType(52, 1);
	public static ObjectType TIME_DELAY_FINE = new ObjectType(52, 2);

	public static ObjectType CLASS_0 = new ObjectType(60, 1);
	public static ObjectType CLASS_1 = new ObjectType(60, 2);
	public static ObjectType CLASS_2 = new ObjectType(60, 3);
	public static ObjectType CLASS_3 = new ObjectType(60, 4);

	public static ObjectType FILE_IDENTIFIER = new ObjectType(70, 1);

	public static ObjectType INTERNAL_INDICATIONS_PACKED = new ObjectType(80, 1);
}
