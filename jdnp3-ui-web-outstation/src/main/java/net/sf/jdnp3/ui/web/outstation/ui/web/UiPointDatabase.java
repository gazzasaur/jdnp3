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
package net.sf.jdnp3.ui.web.outstation.ui.web;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@RequestScoped
public class UiPointDatabase {
	private Logger logger = LoggerFactory.getLogger(UiPointDatabase.class);
	
	public List<UiPoint> getBinaryInputDataPoints(String stationCode, String deviceCode) {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return new ArrayList<>();
		}
		
		if (stationCode != null && deviceCode != null && !stationCode.isEmpty() && !deviceCode.isEmpty()) {
			try {
				List<BinaryInputDataPoint> dataPoints = DeviceProvider.getDevice(stationCode, deviceCode).getDatabaseManager().getBinaryInputDataPoints();
				return convert(dataPoints);
			} catch (Exception e) {
				logger.error("Failed to fetch data points.", e);
			}
		}
		logger.warn(format("Failed to fetch station details for station %s device %s.", stationCode, deviceCode));
		return new ArrayList<>();
	}

	public List<UiPoint> getBinaryOutputDataPoints(String stationCode, String deviceCode) {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return new ArrayList<>();
		}

		if (stationCode != null && deviceCode != null && !stationCode.isEmpty() && !deviceCode.isEmpty()) {
			try {
				List<BinaryOutputDataPoint> dataPoints = DeviceProvider.getDevice(stationCode, deviceCode).getDatabaseManager().getBinaryOutputDataPoints();
				return convert(dataPoints);
			} catch (Exception e) {
				logger.error("Failed to fetch data points.", e);
			}
		}
		logger.warn(format("Failed to fetch station details for station %s device %s.", stationCode, deviceCode));
		return new ArrayList<>();
	}

	public List<UiPoint> getAnalogInputDataPoints(String stationCode, String deviceCode) {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return new ArrayList<>();
		}

		if (stationCode != null && deviceCode != null && !stationCode.isEmpty() && !deviceCode.isEmpty()) {
			try {
				List<AnalogInputDataPoint> dataPoints = DeviceProvider.getDevice(stationCode, deviceCode).getDatabaseManager().getAnalogInputDataPoints();
				return convert(dataPoints);
			} catch (Exception e) {
				logger.error("Failed to fetch data points.", e);
			}
		}
		logger.warn(format("Failed to fetch station details for station %s device %s.", stationCode, deviceCode));
		return new ArrayList<>();
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
