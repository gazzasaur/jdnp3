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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range.RangeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class ObjectFragmentHeaderEncoder {
	private RangeEncoder rangeEncoder = new RangeEncoder();
	private QualifierEncoder qualifierEncoder = new QualifierEncoder();
	
	public void encode(ObjectType objectType, QualifierField qualifierField, Range range, List<Byte> data) {
		DataUtils.addInteger8(objectType.getGroup(), data);
		DataUtils.addInteger8(objectType.getVariation(), data);
		qualifierEncoder.encode(qualifierField, data);
		rangeEncoder.encode(range, qualifierField.getRangeSpecifierCode().getOctetCount(), data);
	}
}
