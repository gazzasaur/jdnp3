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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.InternalIndicatorWriteRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.InternalIndicatorsDataPoint;

import org.apache.commons.beanutils.BeanUtils;

public class InternalIndicatorWriter implements InternalIndicatorWriteRequestHandler {
	public List<AnalogInputStaticObjectInstance> doReadStatics(long startIndex, long stopIndex) {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = DatabaseManagerProvider.getDatabaseManager().getAnalogInputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, dataPoints.get((int) i));
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<AnalogInputStaticObjectInstance> doReadStatics() {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = DatabaseManagerProvider.getDatabaseManager().getAnalogInputDataPoints();

		for (AnalogInputDataPoint analogDataPoint : dataPoints) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, analogDataPoint);
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public void doWriteIndicatorBit(long index, boolean value) {
		if (index != 7) {
			// FIXME IMPL Move this to the adaptor and use a defined exception (parameter error).
			throw new IllegalArgumentException("Only IIN index 7 may be written to.");
		}
		InternalIndicatorsDataPoint dataPoint = DatabaseManagerProvider.getDatabaseManager().getInternalIndicatorsDataPoint();
		dataPoint.setDeviceRestart(value);
		DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(dataPoint);
	}
}