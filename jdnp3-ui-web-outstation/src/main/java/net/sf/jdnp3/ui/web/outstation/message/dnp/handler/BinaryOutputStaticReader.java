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

import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryOutputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;

import org.apache.commons.beanutils.BeanUtils;

public class BinaryOutputStaticReader implements BinaryOutputStaticReadRequestHandler {
	private DatabaseManager databaseManager;

	public BinaryOutputStaticReader(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<BinaryOutputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			BinaryOutputStaticObjectInstance staticObjectInstance = new BinaryOutputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(staticObjectInstance, dataPoints.get((int) i));
				points.add(staticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<BinaryOutputStaticObjectInstance> readStatics() {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (BinaryOutputDataPoint dataPoint : dataPoints) {
			BinaryOutputStaticObjectInstance staticObjectInstance = new BinaryOutputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(staticObjectInstance, dataPoint);
				points.add(staticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}
	
	public List<BinaryOutputStaticObjectInstance> readStatic(long index) {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		if (index < dataPoints.size()) {
			BinaryOutputStaticObjectInstance staticObjectInstance = new BinaryOutputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(staticObjectInstance, dataPoints.get((int) index));
				points.add(staticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public Class<BinaryOutputStaticObjectInstance> getObjectInstanceClass() {
		return BinaryOutputStaticObjectInstance.class;
	}
}