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

import net.sf.jdnp3.ui.web.outstation.database.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

@ManagedBean
@RequestScoped
public class UiPointDatabase {
	public List<UiPoint> getBinaryPoints() {
		List<UiPoint> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			UiPoint point = new UiPoint();
			point.setIndex(binaryDataPoint.getIndex());
			point.setName(binaryDataPoint.getName());
			points.add(point);
		}
		return points;
	}
}
