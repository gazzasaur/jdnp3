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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.handler.time.TimeAndDateRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.TimeAndDateObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

public class TimeAndDateHandler implements TimeAndDateRequestHandler {
	private Logger logger = LoggerFactory.getLogger(TimeAndDateHandler.class);
	
	private InternalStatusProvider internalStatusProvider;
	
	public TimeAndDateHandler(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public List<TimeAndDateObjectInstance> doReadTime(long count) {
		TimeAndDateObjectInstance timeAndDateObjectInstance = new TimeAndDateObjectInstance();
		timeAndDateObjectInstance.setTimestamp(new Date().getTime());
		return Arrays.asList(timeAndDateObjectInstance);
	}

	public void doWriteTime(TimeAndDateObjectInstance timeAndDateObjectInstance) {
		logger.info("Write Time: " + new Date(timeAndDateObjectInstance.getTimestamp()));
		internalStatusProvider.setNeedTime(false);
	}
}
