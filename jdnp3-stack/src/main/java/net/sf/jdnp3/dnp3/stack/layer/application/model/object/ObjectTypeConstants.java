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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public class ObjectTypeConstants {
	public static ObjectType BINARY_INPUT_STATIC_ANY = new ObjectType() {{
		this.setGroup(1);
		this.setVariation(0);
	}};

	public static ObjectType BINARY_INPUT_STATIC_PACKED = new ObjectType() {{
		this.setGroup(1);
		this.setVariation(1);
	}};

	public static ObjectType BINARY_INPUT_STATIC_FLAGS = new ObjectType() {{
		this.setGroup(1);
		this.setVariation(2);
	}};

	public static ObjectType BINARY_INPUT_EVENT_ANY = new ObjectType() {{
		this.setGroup(2);
		this.setVariation(0);
	}};

	public static ObjectType BINARY_INPUT_EVENT_WITHOUT_TIME = new ObjectType() {{
		this.setGroup(2);
		this.setVariation(1);
	}};

	public static ObjectType BINARY_INPUT_EVENT_ABSOLUTE_TIME = new ObjectType() {{
		this.setGroup(2);
		this.setVariation(2);
	}};

	public static ObjectType BINARY_INPUT_EVENT_RELATIVE_TIME = new ObjectType() {{
		this.setGroup(2);
		this.setVariation(3);
	}};

	public static ObjectType CLASS_0 = new ObjectType() {{
		this.setGroup(60);
		this.setVariation(1);
	}};

	public static ObjectType CLASS_1 = new ObjectType() {{
		this.setGroup(60);
		this.setVariation(2);
	}};

	public static ObjectType CLASS_2 = new ObjectType() {{
		this.setGroup(60);
		this.setVariation(3);
	}};

	public static ObjectType CLASS_3 = new ObjectType() {{
		this.setGroup(60);
		this.setVariation(4);
	}};

	public static ObjectType INTERNAL_INDICATIONS_PACKED = new ObjectType() {{
		this.setGroup(80);
		this.setVariation(1);
	}};
}
