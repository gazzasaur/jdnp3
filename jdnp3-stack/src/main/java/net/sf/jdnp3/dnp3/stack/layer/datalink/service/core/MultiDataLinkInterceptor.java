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

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class MultiDataLinkInterceptor implements DataLinkInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataLinkInterceptor.class);
	
	private ExecutorService executorService;
	private List<ChannelId> connectedChannels = new ArrayList<>();
	private List<DataLinkInterceptor> listeners = new ArrayList<DataLinkInterceptor>();
	private LinkedList<Pair<MessageProperties, DataLinkFrame>> frameQueue = new LinkedList<>();
	
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

	// TODO May need to pass this upstream.
	public void connected(ChannelId channelId) {
		synchronized (frameQueue) {
			connectedChannels.add(channelId);
		}
	}
	
	// TODO May need to pass this upstream.
	public void disconnected(ChannelId channelId) {
		synchronized (frameQueue) {
			connectedChannels.remove(channelId);
		}
	}

	public void receiveData(MessageProperties messageProperties, DataLinkFrame frame) {
		if (executorService == null) {
			throw new IllegalStateException("No ExecutorService has been defined.");
		}

		synchronized (frameQueue) {
			boolean threadRunning = !frameQueue.isEmpty();
			frameQueue.offerFirst(Pair.of(messageProperties, frame));

			if (threadRunning) {
				return;
			}
			executorService.execute(() -> this.startThread());
		}
	}

	private void startThread() {
		long processStartTime = System.currentTimeMillis();

		boolean channelClosed = false;
		Pair<MessageProperties, DataLinkFrame> requestData;

		synchronized (frameQueue) {
			requestData = frameQueue.peekLast();
			if (requestData == null) {
				return;
			}
			channelClosed = !connectedChannels.contains(requestData.getLeft().getChannelId());
		}

		if (!channelClosed) {
			List<DataLinkInterceptor> listenersCopy;
			synchronized (listeners) {
				listenersCopy = new ArrayList<>(listeners);
			}

			for (DataLinkInterceptor dataLinkInterceptor : listenersCopy) {
				try {
					dataLinkInterceptor.receiveData(requestData.getLeft(), requestData.getRight());
				} catch (Exception e) {
					LOGGER.error("Error caught from datalink interceptor.  Moving on.", e);
					// FIXME IMPL I have been requested not to remove this here.  This could be dangerous, but it make sense. This should be an option if anything.
					// listeners.remove(dataLinkListener);
				}
			}
		}
		synchronized (frameQueue) {
			frameQueue.pollLast();
			if (!frameQueue.isEmpty()) {
				executorService.execute(() -> startThread());
			}
		}
	}
}
