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

import static java.util.Arrays.asList;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CUSTOM;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.trim;
import static org.apache.commons.lang3.ArrayUtils.toObject;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class ByteDataObjectTypeDecoder {
	private List<Byte> expectedData;
	private List<Byte> responseData;
	private FunctionCode functionCode;
	
	public ByteDataObjectTypeDecoder(FunctionCode functionCode, String expectedData, String responseData) {
		this.functionCode = functionCode;
		this.expectedData = new ArrayList<>(asList(toObject(parseHexBinary(expectedData))));
		this.responseData = new ArrayList<>(asList(toObject(parseHexBinary(responseData))));
	}

	public boolean canDecode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		return functionCode.equals(decoderContext.getFunctionCode()) && (data.size() >= expectedData.size()) && expectedData.equals(data.subList(0, expectedData.size()));
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		if (!this.canDecode(decoderContext, data)) {
			throw new IllegalArgumentException("Cannot decode data.");
		}
		
		trim(expectedData.size(), data);
		
		ByteDataObjectInstance objectInstance = new ByteDataObjectInstance();
		objectInstance.setData(new ArrayList<>(responseData));
		objectInstance.setRequestedType(CUSTOM);
		return objectInstance;
	}
}
