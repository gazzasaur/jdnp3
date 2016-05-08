/**
 * Copyright 2016 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.analog.AnalogOutputOperateRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputCommandObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;

public class AnalogOutputCommandOperator implements AnalogOutputOperateRequestHandler {
	private DatabaseManager databaseManager;

	public AnalogOutputCommandOperator(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public AnalogOutputCommandObjectInstance doDirectOperate(AnalogOutputCommandObjectInstance objectInstance) {
		List<AnalogOutputDataPoint> analogOutputDataPoints = databaseManager.getAnalogOutputDataPoints();
		if (analogOutputDataPoints.size() > objectInstance.getIndex()) {
			AnalogOutputDataPoint analogOutputDataPoint = analogOutputDataPoints.get((int) objectInstance.getIndex());
			objectInstance.setStatusCode(analogOutputDataPoint.getStatusCode());
			
			analogOutputDataPoint.setOperatedCount(analogOutputDataPoint.getOperatedCount() + 1);
			
			if (objectInstance.getStatusCode().equals(StatusCode.SUCCESS) && analogOutputDataPoint.isAutoUpdateOnSuccess()) {
				analogOutputDataPoint.setValue(objectInstance.getValue());
			}
			databaseManager.setAnalogOutputDataPoint(analogOutputDataPoint);
		}
		return objectInstance;
	}
}
