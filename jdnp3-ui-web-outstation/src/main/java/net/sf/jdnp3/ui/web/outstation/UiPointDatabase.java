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

import net.sf.jdnp3.ui.web.outstation.database.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

@ManagedBean
@RequestScoped
public class UiPointDatabase {
	public List<UiPoint> getBinaryInputDataPoints() {
		List<BinaryInputDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
		return convert(binaryDataPoints);
	}

	public List<UiPoint> getAnalogInputDataPoints() {
		List<AnalogInputDataPoint> analogDataPoints = DatabaseManagerProvider.getDatabaseManager().getAnalogDataPoints();
		return convert(analogDataPoints);
	}

	private List<UiPoint> convert(List<? extends DataPoint> dataPoints) {
		List<UiPoint> points = new ArrayList<>();
		for (DataPoint dataPoint : dataPoints) {
			UiPoint point = new UiPoint();
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			points.add(point);
		}
		return points;
	}
}
