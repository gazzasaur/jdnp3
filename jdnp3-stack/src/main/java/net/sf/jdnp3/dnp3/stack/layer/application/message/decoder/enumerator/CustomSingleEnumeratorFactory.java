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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;

public class CustomSingleEnumeratorFactory implements ItemEnumeratorFactory {
	private ObjectType objectType;
	private FunctionCode functionCode;
	private Class<? extends Range> rangeClass;
	private Class<? extends PrefixType> prefixTypeClass;
	
	public CustomSingleEnumeratorFactory(ObjectType objectType, FunctionCode functionCode, Class<? extends Range> rangeClass, Class<? extends PrefixType> prefixTypeClass) {
		this.objectType = objectType;
		this.rangeClass = rangeClass;
		this.functionCode = functionCode;
		this.prefixTypeClass = prefixTypeClass;
	}
	
	public boolean hasEnumerator(ObjectFragmentDecoderContext decoderContext, ObjectFragment objectFragment) {
		return (functionCode.equals(decoderContext.getFunctionCode()) &&
				objectType.equals(decoderContext.getObjectType()) &&
				objectFragment.getObjectFragmentHeader().getRange().getClass().equals(rangeClass) &&
				objectFragment.getObjectFragmentHeader().getPrefixType().getClass().equals(prefixTypeClass));
	}
	
	public ItemEnumerator createEnumerator(ObjectFragmentDecoderContext decoderContext, ObjectFragment objectFragment) {
		if (!this.hasEnumerator(decoderContext, objectFragment)) {
			throw new IllegalArgumentException(String.format("No enumerator exist for the prefix type %s and range type %s.", decoderContext.getClass(), objectFragment.getClass()));
		}
		return new CustomSingleEnumerator();
	}
}
