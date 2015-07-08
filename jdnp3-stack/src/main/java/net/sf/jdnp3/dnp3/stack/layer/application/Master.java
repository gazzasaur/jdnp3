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
package net.sf.jdnp3.dnp3.stack.layer.application;

import java.util.List;

public class Master {
	public void requestPoint(int index) {
	}
	
	public void requestStaticObjects(int startIndex, int count) {
	}
	
	public void requestAllPoints() {
	}

	public void requestPoints(int count) {
	}

	public void requestPoints(List<Integer> indicies) {
	}
	
//	public void addHandler() { Create filter for group, group and variation, index as well as aggregation filter.
//	}
}
