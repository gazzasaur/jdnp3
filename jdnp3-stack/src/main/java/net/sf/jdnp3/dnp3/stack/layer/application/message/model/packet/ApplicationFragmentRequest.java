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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFragmentRequest {
	private ApplicationFragmentRequestHeader header = new ApplicationFragmentRequestHeader();
	private List<ObjectFragment> objectFragments = new ArrayList<>();
	
	public ApplicationFragmentRequestHeader getHeader() {
		return header;
	}
	
	public void setHeader(ApplicationFragmentRequestHeader header) {
		this.header = header;
	}
	
	public List<ObjectFragment> getObjectFragments() {
		return unmodifiableList(objectFragments);
	}
	
	public void addObjectFragment(ObjectFragment objectFragment) {
		objectFragments.add(objectFragment);
	}
}
