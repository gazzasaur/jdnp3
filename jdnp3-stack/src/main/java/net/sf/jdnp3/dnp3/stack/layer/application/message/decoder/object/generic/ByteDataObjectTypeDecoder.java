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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.trim;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class ByteDataObjectTypeDecoder implements ObjectTypeDecoder {
	private ObjectType objectType;
	private List<Byte> expectedData;
	
	public ByteDataObjectTypeDecoder(ObjectType objectType, List<Byte> expectedData) {
		this.objectType = objectType;
		this.expectedData = new ArrayList<>(expectedData);
	}
	
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(objectType);
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		List<Byte> actualData = new ArrayList<>(data.subList(0, expectedData.size()));
		trim(expectedData.size(), data);
		
		if (actualData.equals(expectedData)) {
			ByteDataObjectInstance objectInstance = new ByteDataObjectInstance();
			objectInstance.setData(expectedData);
			return objectInstance;
		}
		throw new IllegalArgumentException(format("Actual data %s does not match expeced data %s.", actualData, expectedData));
	}
}
