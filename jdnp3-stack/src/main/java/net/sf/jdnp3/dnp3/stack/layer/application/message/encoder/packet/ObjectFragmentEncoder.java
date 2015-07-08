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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range.RangeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;

public class ObjectFragmentEncoder {
	private RangeEncoder rangeEncoder = new RangeEncoder();
	private QualifierEncoder qualifierEncoder = new QualifierEncoder();
	private ObjectFieldEncoder objectFieldEncoder = new ObjectFieldEncoder();
	
	// FIXME IMPL If object prefix cannot fit in the OPC do I override, throw exception.  Or do I always calculate it.
	// FIXME IMPL If the range cannot fit in the RSC do I override, throw exception.  Or do I always calculate it.
	public void encode(ObjectFragment objectFragment, List<Byte> data) {
		RangeSpecifierCode rangeSpecifierCode = rangeEncoder.calculateRangeSpecifierCode(objectFragment.getObjectFragmentHeader().getRange());
		objectFragment.getObjectFragmentHeader().getQualifierField().setRangeSpecifierCode(rangeSpecifierCode);
		
		data.add((byte) objectFragment.getObjectFragmentHeader().getObjectType().getGroup());
		data.add((byte) objectFragment.getObjectFragmentHeader().getObjectType().getVariation());
		qualifierEncoder.encode(objectFragment.getObjectFragmentHeader().getQualifierField(), data);
		rangeEncoder.encode(objectFragment.getObjectFragmentHeader().getRange(), data);
		
		if (rangeSpecifierCode != objectFragment.getObjectFragmentHeader().getQualifierField().getRangeSpecifierCode()) {
			throw new IllegalArgumentException(String.format("The calculated RangeSpecifierCode %s does not match the declared range specifier code %s.", rangeSpecifierCode.getCode(), objectFragment.getObjectFragmentHeader().getQualifierField().getRangeSpecifierCode().getCode()));
		}

		for (ObjectField objectField : objectFragment.getObjectFields()) {
			objectFieldEncoder.encode(objectFragment.getObjectFragmentHeader().getQualifierField().getObjectPrefixCode(), objectField, data);
		}
	}
}
