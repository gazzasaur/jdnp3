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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.BinaryInputObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class0ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class1ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class2ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class3ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.InternalIndicatorBitObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;

public class ObjectFragmentDataDecoder {
	@SuppressWarnings("serial")
	private Map<RangeSpecifierCode, ObjectInstanceListDecoder> listDecoders = new HashMap<RangeSpecifierCode, ObjectInstanceListDecoder>() {{
		this.put(RangeSpecifierCode.ONE_OCTET_INDEX, new IndexRangeObjectInstanceListDecoder());
		this.put(RangeSpecifierCode.TWO_OCTET_INDEX, new IndexRangeObjectInstanceListDecoder());
		this.put(RangeSpecifierCode.FOUR_OCTET_INDEX, new IndexRangeObjectInstanceListDecoder());
		this.put(RangeSpecifierCode.NO_RANGE, new NoRangeObjectInstanceListDecoder());
	}};
	
	@SuppressWarnings("serial")
	private Map<FunctionCode, ObjectFunctionDecoder> functionDecoders = new HashMap<FunctionCode, ObjectFunctionDecoder>() {{
		this.put(FunctionCode.READ, new NullObjectFunctionDecoder());
		this.put(FunctionCode.WRITE, new ObjectInstanceFunctionDecoder());
		this.put(FunctionCode.ENABLE_UNSOLICITED, new NullObjectFunctionDecoder());
		this.put(FunctionCode.DISABLE_UNSOLICITED, new NullObjectFunctionDecoder());
	}};

	@SuppressWarnings("serial")
	private Map<ObjectType, ObjectTypeDecoder> objectDecoders = new HashMap<ObjectType, ObjectTypeDecoder>() {{
		this.put(ObjectTypeConstants.BINARY_INPUT_STATIC_ANY, new BinaryInputObjectTypeDecoder());
		
		this.put(ObjectTypeConstants.CLASS_0, new Class0ObjectTypeDecoder());
		this.put(ObjectTypeConstants.CLASS_1, new Class1ObjectTypeDecoder());
		this.put(ObjectTypeConstants.CLASS_2, new Class2ObjectTypeDecoder());
		this.put(ObjectTypeConstants.CLASS_3, new Class3ObjectTypeDecoder());

		this.put(ObjectTypeConstants.INTERNAL_INDICATIONS_PACKED, new InternalIndicatorBitObjectTypeDecoder());
	}};
	
	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		ObjectInstanceListDecoder listDecoder = listDecoders.get(objectFragment.getObjectFragmentHeader().getQualifierField().getRangeSpecifierCode());
		ObjectFunctionDecoder functionDecoder = functionDecoders.get(functionCode);
		ObjectTypeDecoder objectTypeDecoder = objectDecoders.get(objectFragment.getObjectFragmentHeader().getObjectType());
		nullCheck(functionDecoder, "ObjectFunctionDecoder", "FunctionCode", format("0x%02X", functionCode.getCode()));
		nullCheck(listDecoder, "IndexDecoder", "ObjectPrefixCode", format("0x%02X", objectFragment.getObjectFragmentHeader().getQualifierField().getObjectPrefixCode().getCode()));
		nullCheck(objectTypeDecoder, "ObjectTypeDecoder", "ObjectType", objectFragment.getObjectFragmentHeader().getObjectType().toString());
		listDecoder.decode(objectFragment, functionDecoder, objectTypeDecoder, data);
		for (ObjectField objectField : objectFragment.getObjectFields()) {
			objectField.getObjectInstance().setRequestedType(objectFragment.getObjectFragmentHeader().getObjectType());
		}
	}

	private void nullCheck(Object object, String type, String code, String value) {
		if (object == null) {
			throw new IllegalArgumentException(format("No %s was found for the %s: %s", type, code, value));
		}
	}
}
