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
package net.sf.jdnp3.dnp3.stack.layer.datalink.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class MultiDataLinkInterceptor implements DataLinkInterceptor {
	private Logger logger = LoggerFactory.getLogger(MultiDataLinkInterceptor.class);
	
	private ExecutorService executorService;
	private List<DataLinkInterceptor> listeners = new ArrayList<DataLinkInterceptor>();
	
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	public void addDataLinkListener(DataLinkInterceptor dataLinkInterceptor) {
		synchronized (listeners) {
			listeners.add(dataLinkInterceptor);
		}
	}

	public void removeDataLinkListener(DataLinkInterceptor dataLinkInterceptor) {
		synchronized (listeners) {
			listeners.remove(dataLinkInterceptor);
		}
	}

	public void receiveData(MessageProperties messageProperties, DataLinkFrame frame) {
		if (executorService == null) {
			throw new IllegalStateException("No ExecutorService has been defined.");
		}
		
		List<DataLinkInterceptor> listenersCopy;
		synchronized (listeners) {
			listenersCopy = new ArrayList<>(listeners);
		}
		
		for (DataLinkInterceptor dataLinkInterceptor : listenersCopy) {
			executorService.execute(
				new Runnable() {
					public void run() {
						try {
							dataLinkInterceptor.receiveData(messageProperties, frame);
						} catch (Exception e) {
							logger.error("Error caught from datalink interceptor.  Moving on.", e);
							// FIXME IMPL I have been requested not to remove this here.  This could be dangerous, but it make sense. This should be an option if anything.
							// listeners.remove(dataLinkListener);
						}
					}
				}
			);
		}
	}
}
