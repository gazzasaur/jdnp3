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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;

public class ObjectFragmentHeader {
	private ObjectType objectType = ANY;
	private QualifierField qualifierField = new QualifierField();
	private PrefixType prefixType = new NoPrefixType();
	private Range range = new NoRange();
	
	public ObjectType getObjectType() {
		return objectType;
	}
	
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public QualifierField getQualifierField() {
		return qualifierField;
	}

	public void setQualifierField(QualifierField qualifierField) {
		this.qualifierField = qualifierField;
	}

	public PrefixType getPrefixType() {
		return prefixType;
	}

	public void setPrefixType(PrefixType prefixType) {
		this.prefixType = prefixType;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}
}
