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
package net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class MultiDataLinkListener implements DataLinkListener {
	private Logger logger = LoggerFactory.getLogger(MultiDataLinkListener.class);
	
	private ExecutorService executorService;
	private List<DataLinkListener> listeners = new ArrayList<DataLinkListener>();
	
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	public void addDataLinkListener(DataLinkListener dataLinkListener) {
		synchronized (listeners) {
			listeners.add(dataLinkListener);
		}
	}

	public void removeDataLinkListener(DataLinkListener dataLinkListener) {
		synchronized (listeners) {
			listeners.remove(dataLinkListener);
		}
	}

	public void receiveData(MessageProperties messageProperties, List<Byte> data) {
		if (executorService == null) {
			throw new IllegalStateException("No ExecutorService has been defined.");
		}
		
		List<DataLinkListener> listenersCopy;
		synchronized (listeners) {
			listenersCopy = new ArrayList<>(listeners);
		}
		
		for (DataLinkListener dataLinkListener : listenersCopy) {
			executorService.execute(
				new Runnable() {
					public void run() {
						try {
							dataLinkListener.receiveData(messageProperties, data);
						} catch (Exception e) {
							logger.error("Error caught from datalink listener.  Evicting member.", e);
							listeners.remove(dataLinkListener);
						}
					}
				}
			);
		}
	}
}
