/**
 * Copyright 2016 Graeme Farquharson
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

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range.RangeSpecifierCodeCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCodeCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;

public class QualifierFieldCalculator {
	public static QualifierField calculate(PrefixType prefixType, Range range) {
		QualifierField qualifierField = new QualifierField();
		ObjectPrefixCode minOpc = ObjectPrefixCodeCalculator.calculate(prefixType);
		RangeSpecifierCode minRsc = RangeSpecifierCodeCalculator.calculateRangeSpecifierCode(range, 0);
		int minOctetCount = Math.max(minOpc.getOctetCount(), minRsc.getOctetCount());
		qualifierField.setObjectPrefixCode(ObjectPrefixCodeCalculator.calculate(prefixType, minOctetCount));
		qualifierField.setRangeSpecifierCode(RangeSpecifierCodeCalculator.calculateRangeSpecifierCode(range, minOctetCount));
		return qualifierField;
	}
}
