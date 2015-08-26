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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object;

import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range.RangeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragmentHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;

public class ObjectFragmentHeaderEncoder {
	private RangeEncoder rangeEncoder = new RangeEncoder();
	private QualifierEncoder qualifierEncoder = new QualifierEncoder();
	
	public void encode(ObjectFragmentHeader header, List<Byte> data) {
		Range range = header.getRange();
		ObjectType objectType = header.getObjectType();
		QualifierField qualifierField = header.getQualifierField();
		
		addInteger(objectType.getGroup(), 1, data);
		addInteger(objectType.getVariation(), 1, data);
		qualifierEncoder.encode(qualifierField, data);
		rangeEncoder.encode(range, qualifierField.getRangeSpecifierCode().getOctetCount(), data);
	}
}
