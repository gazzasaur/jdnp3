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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class MultiDataLinkInterceptor implements DataLinkInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataLinkInterceptor.class);
	
	private ExecutorService executorService;
	private List<ChannelId> connectedChannels = new LinkedList<ChannelId>();
	private List<DataLinkInterceptor> listeners = new LinkedList<DataLinkInterceptor>();
	
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void connected(ChannelId channelId) {
		synchronized (listeners) {
			connectedChannels.add(channelId);
		}
	}

	public void disconnected(ChannelId channelId) {
		synchronized (listeners) {
			while (connectedChannels.remove(channelId)) {}
		}
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
		executorService.execute(() -> {
			List<DataLinkInterceptor> listenersCopy;
			synchronized (listeners) {
				if (!connectedChannels.contains(messageProperties.getChannelId())) {
					LOGGER.warn("Dropping payload as the channel has been disconnected.");
				}
				listenersCopy = new ArrayList<>(listeners);
			}
			
			for (DataLinkInterceptor dataLinkInterceptor : listenersCopy) {
				try {
					dataLinkInterceptor.receiveData(messageProperties, frame);
				} catch (Exception e) {
					LOGGER.error("Error caught from datalink interceptor.  Moving on.", e);
					// FIXME IMPL I have been requested not to remove this here.  This could be dangerous, but it make sense. This should be an option if anything.
					// listeners.remove(dataLinkListener);
				}
			}	
		});
	}
}
