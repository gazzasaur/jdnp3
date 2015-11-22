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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.OperationType.NUL;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryOutputOperateRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputCrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;

public class CrobOperator implements BinaryOutputOperateRequestHandler {
	private DatabaseManager databaseManager;

	public CrobOperator(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public BinaryOutputCrobObjectInstance doDirectOperate(BinaryOutputCrobObjectInstance crobObjectInstance) {
		List<BinaryOutputDataPoint> binaryOutputDataPoints = databaseManager.getBinaryOutputDataPoints();
		if (binaryOutputDataPoints.size() > crobObjectInstance.getIndex()) {
			BinaryOutputDataPoint binaryOutputDataPoint = binaryOutputDataPoints.get((int) crobObjectInstance.getIndex());
			crobObjectInstance.setStatusCode(binaryOutputDataPoint.getStatusCode());
			
			binaryOutputDataPoint.setOperatedCount(binaryOutputDataPoint.getOperatedCount() + 1);
			
			binaryOutputDataPoint.setCount(crobObjectInstance.getCount());
			binaryOutputDataPoint.setOnTime(crobObjectInstance.getOnTime());
			binaryOutputDataPoint.setOffTime(crobObjectInstance.getOffTime());
			binaryOutputDataPoint.setOperationType(crobObjectInstance.getOperationType());
			binaryOutputDataPoint.setTripCloseCode(crobObjectInstance.getTripCloseCode());
			
			if (crobObjectInstance.getStatusCode().equals(StatusCode.SUCCESS) && !crobObjectInstance.getOperationType().equals(NUL) && binaryOutputDataPoint.isAutoUpdateOnSuccess()) {
				binaryOutputDataPoint.setActive(crobObjectInstance.getOperationType().isActive());
			}
			databaseManager.setBinaryOutputDataPoint(binaryOutputDataPoint);
		}
		return crobObjectInstance;
	}
}
