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
package net.sf.jdnp3.ui.web.outstation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

@ManagedBean
@RequestScoped
public class PointDatabase {
	public List<String> getPoints() {
		int index = 0;
		List<String> names = new ArrayList<>();
		List<BinaryDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
		for (BinaryDataPoint binaryDataPoint : binaryDataPoints) {
			names.add("p" + index);
			++index;
		}
		return names;
	}
}
