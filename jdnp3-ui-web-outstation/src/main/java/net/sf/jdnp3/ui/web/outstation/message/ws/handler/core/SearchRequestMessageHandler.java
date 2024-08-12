/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.SearchResultItem;
import net.sf.jdnp3.ui.web.outstation.main.SiteDeviceList;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SearchRequestMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SearchResultMessage;

public class SearchRequestMessageHandler implements MessageHandler {
	// Onlt allow for a single search thread.
	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public boolean canHandle(Message message) {
		return message instanceof SearchRequestMessage;
	}

	// FIXME This is expensive for something that can be precomputed or cached and should definitely blast noisy clients.
	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}

		var searchTerm = ((SearchRequestMessage) message).getSearchTerm();

		if (searchTerm == null || searchTerm.length() < 1) {
			messanger.sendMessage(new SearchResultMessage());
			return;
		}
		var searchTerms = Arrays.asList(searchTerm.split(" "));
		if (searchTerms.size() > 3) {
			searchTerms = searchTerms.subList(0, 3);
		}

		var searchableTerms = searchTerms;
		executorService.submit(() -> {
			var results = new ArrayList<SearchResultItem>();

			for (SiteDeviceList site : DeviceProvider.gettDeviceList().getSiteDeviceLists()) {
				for (var deviceName : site.getDevices()) {
					var device = DeviceProvider.getDevice(site.getSite(), deviceName);
					for (var dataPoint : device.getDatabaseManager().getAnalogInputDataPoints()) {
						var searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.contains(term)) {
								return true;
							}
							if (dataPoint.getName().contains(term)) {
								return true;
							}
							for (var tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().contains(term)) {
									return true;
								}
								if (tag.getValue().contains(term)) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							var result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType(dataPoint.getClass().getSimpleName());
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).toList()));
							results.add(result);
						}

						if (results.size() >= 10) {
							var resultMessage = new SearchResultMessage();
							messanger.sendMessage(resultMessage);
							return;
						}
					}
				}
			}

			var resultMessage = new SearchResultMessage();
			messanger.sendMessage(resultMessage);
			return;
		});
	}
}
