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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
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

		String searchTerm = ((SearchRequestMessage) message).getSearchTerm();

		if (searchTerm == null || searchTerm.length() < 1) {
			messanger.sendMessage(new SearchResultMessage());
			return;
		}
		List<String> searchTerms = Arrays.asList(searchTerm.split(" "));
		if (searchTerms.size() > 3) {
			searchTerms = searchTerms.subList(0, 3);
		}

		List<String> searchableTerms = searchTerms;
		executorService.submit(() -> {
			List<SearchResultItem> results = new ArrayList<SearchResultItem>();

			for (SiteDeviceList site : DeviceProvider.gettDeviceList().getSiteDeviceLists()) {
				for (String deviceName : site.getDevices()) {
					OutstationDevice device = DeviceProvider.getDevice(site.getSite(), deviceName);

					for (AnalogInputDataPoint dataPoint : device.getDatabaseManager().getAnalogInputDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String,String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("analogInputPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}

					for (AnalogOutputDataPoint dataPoint : device.getDatabaseManager().getAnalogOutputDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String,String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("analogOutputPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}

					for (BinaryInputDataPoint dataPoint : device.getDatabaseManager().getBinaryInputDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String,String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("binaryInputPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}

					for (BinaryOutputDataPoint dataPoint : device.getDatabaseManager().getBinaryOutputDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String, String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("binaryOutputPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}

					for (CounterDataPoint dataPoint : device.getDatabaseManager().getCounterDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String, String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("counterPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}

					for (DoubleBitBinaryInputDataPoint dataPoint : device.getDatabaseManager().getDoubleBitBinaryInputDataPoints()) {
						Optional<Boolean> searchTermsMet = searchableTerms.stream().map(term -> {
							if (deviceName.toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							if (dataPoint.getName().toLowerCase().contains(term.toLowerCase())) {
								return true;
							}
							for (Map.Entry<String, String> tag : dataPoint.getTags().entrySet()) {
								if (tag.getKey().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
								if (tag.getValue().toLowerCase().contains(term.toLowerCase())) {
									return true;
								}
							}
							return false;
						}).filter(v -> !v).findAny();

						if (!searchTermsMet.isPresent()) {
							SearchResultItem result = new SearchResultItem();
							result.setSite(site.getSite());
							result.setDevice(deviceName);
							result.setPointIndex(dataPoint.getIndex());
							result.setPointName(dataPoint.getName());
							result.setPointType("doubleBitBinaryInputPoint");
							result.setAdditionalInformation(String.join(", ", dataPoint.getTags().entrySet().stream().map(es -> es.getKey() + ": " + es.getValue()).collect(Collectors.toList())));
							results.add(result);
						}

						if (results.size() >= 10) {
							SearchResultMessage resultMessage = new SearchResultMessage();
							resultMessage.setSearchTerm(searchTerm);
							resultMessage.setResults(results);
							messanger.sendMessage(resultMessage);
							return;
						}
					}
				}
			}

			SearchResultMessage resultMessage = new SearchResultMessage();
			resultMessage.setSearchTerm(searchTerm);
			resultMessage.setResults(results);
			messanger.sendMessage(resultMessage);
			return;
		});
	}
}
