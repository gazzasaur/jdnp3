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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.range.RangeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class ObjectFragmentDecoder {
	private RangeDecoder rangeDecoder = new RangeDecoder();
	private QualifierDecoder qualifierDecoder = new QualifierDecoder();
	private ObjectFragmentDataDecoder objectFragmentDataDecoder = new ObjectFragmentDataDecoder();
	
	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		objectFragment.getObjectFragmentHeader().getObjectType().setGroup((int) DataUtils.getInteger8(0, data));
		objectFragment.getObjectFragmentHeader().getObjectType().setVariation((int) DataUtils.getInteger8(1, data));
		DataUtils.trim(2, data);
		
		qualifierDecoder.decode(objectFragment.getObjectFragmentHeader().getQualifierField(), data);
		rangeDecoder.decode(objectFragment.getObjectFragmentHeader(), data);
		
		objectFragmentDataDecoder.decode(functionCode, objectFragment, data);
	}
}
