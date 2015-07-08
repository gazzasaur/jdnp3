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

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;

public class IndexRangeObjectInstanceListDecoder implements ObjectInstanceListDecoder {
	public void decode(ObjectFragment objectFragment, ObjectFunctionDecoder functionDecoder, ObjectTypeDecoder objectTypeDecoder, List<Byte> data) {
		IndexRange range = (IndexRange) objectFragment.getObjectFragmentHeader().getRange();
		System.out.println("Start " + range.getStartIndex() + ", Stop: " + range.getStopIndex());
		for (long i = range.getStartIndex(); i <= range.getStopIndex(); ++i) {
			System.out.println("Index " + i);
			ObjectField objectField = new ObjectField();
			objectField.setPrefix(i);
			functionDecoder.decode(range.getStartIndex(), range.getStopIndex(), objectField, objectTypeDecoder, data);
		}
	}
}
