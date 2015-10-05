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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.RESPONSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.ANY;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public class ObjectFragmentDecoderContext {
	private ObjectType objectType = ANY;
	private long commonTimeOfOccurrance = 0;
	private FunctionCode functionCode = RESPONSE;
	
	private long startIndex = 0;
	private long currentIndex = 0;
	private boolean lastItem = false;
	
	public ObjectType getObjectType() {
		return objectType;
	}
	
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public long getCommonTimeOfOccurrance() {
		return commonTimeOfOccurrance;
	}

	public void setCommonTimeOfOccurrance(long commonTimeOfOccurrance) {
		this.commonTimeOfOccurrance = commonTimeOfOccurrance;
	}

	public FunctionCode getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(FunctionCode functionCode) {
		this.functionCode = functionCode;
	}

	public long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}

	public long getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(long currentIndex) {
		this.currentIndex = currentIndex;
	}

	public boolean isLastItem() {
		return lastItem;
	}

	public void setLastItem(boolean lastItem) {
		this.lastItem = lastItem;
	}
}
