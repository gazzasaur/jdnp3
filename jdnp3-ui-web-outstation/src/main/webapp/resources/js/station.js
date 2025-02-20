var jdnp3 = jdnp3 || {};
jdnp3.station = jdnp3.station || {};

jdnp3.station.siteDeviceListings = [];

jdnp3.station.SetStationsMessageHandler = function() {
}

jdnp3.station.SearchResultMessageHandler = function() {
}

jdnp3.station.updateDeviceListing = function(site, devices) {
	site.devices.forEach(function(device) {
		device.dirty = true;
	});
	
	devices.forEach(function(device) {
		var j = 0;
		for (j = 0; j < site.devices.length; ++j) {
			if (device === site.devices[j].device) {
				site.devices[j].dirty = false;
				return;
			} else if (site.devices[j].device > device) {
				break;
			}
		}
		
		var deviceComponent = document.createElement('a');
		deviceComponent.setAttribute('style', 'display: block;');
		deviceComponent.target = '_blank';
		deviceComponent.title = device;
		deviceComponent.href = '/device.jsf?stationCode=' + site.site + '&deviceCode=' + device;
		deviceComponent.appendChild(document.createTextNode(device));
		var additionalDeviceItem = {
			'dirty': false,
			'device': device,
			'component': deviceComponent
		};
		site.devices.splice(j, 0, additionalDeviceItem);
		site.component.insertBefore(additionalDeviceItem.component, site.component.children[j]);
	});
	
	var deviceListing = [];
	site.devices.forEach(function(device) {
		if (device.dirty) {
			site.component.removeChild(siteData.component);
		} else {
			deviceListing.push(device);
		}
	});
	site.devices = deviceListing;
}

jdnp3.station.updateSearchResults = function(searchResult) {
	var searchResultsPanel = document.getElementById('searchResults');
	searchResultsPanel.innerHTML = '';

	for (var result of searchResult['results']) {
		var siteDiv = document.createElement('div');
		siteDiv.appendChild(document.createTextNode(result['site'] + ' - ' + result['device']))
		var pointDiv = document.createElement('div');
		pointDiv.appendChild(document.createTextNode(result['pointName'] + ', ' + result['pointType'].charAt(0).toUpperCase() + result['pointType'].slice(1) + ' at Index ' + result['pointIndex']))
		var tagsDiv = document.createElement('div');
		tagsDiv.appendChild(document.createTextNode(result['additionalInformation']));

		var resultDiv = document.createElement('a');
		resultDiv.appendChild(siteDiv);
		resultDiv.appendChild(pointDiv);
		resultDiv.appendChild(tagsDiv);
		resultDiv.style.color = 'inherit';
		resultDiv.style.textDecoration = 'none';
		resultDiv.style.display = 'block';
		resultDiv.style.cursor = 'pointer';
		resultDiv.style.borderLeft = '1px solid black';
		resultDiv.style.borderRight = '1px solid black';
		resultDiv.style.borderBottom = '1px solid black';
		resultDiv.onmouseleave = (event) => event.target.style.backgroundColor = '';
		resultDiv.onmouseenter = (event) => event.target.style.backgroundColor = 'lightgray';

		// FIXME Add this in when the UX feels better
		// resultDiv.href = '/device.jsf?stationCode=' + result['site'] + '&deviceCode=' + result['device'] + '&filter=' + searchResult['searchTerm'];
		resultDiv.target = '_blank';
		resultDiv.href = '/device.jsf?stationCode=' + result['site'] + '&deviceCode=' + result['device'];

		searchResultsPanel.appendChild(resultDiv);
	}
}

jdnp3.station.SearchResultMessageHandler.prototype.processMessage = function(searchResult) {
	jdnp3.station.updateSearchResults(searchResult);
}

jdnp3.station.SetStationsMessageHandler.prototype.processMessage = function(stationMessage) {
	var mainContainer = document.getElementById('device-selection');
	
	jdnp3.station.siteDeviceListings.forEach(function(siteData) {
		siteData.dirty = true;
	});
	
	stationMessage.siteDeviceLists.forEach(function(siteDeviceList) {
		var i = 0;
		for (i = 0; i < jdnp3.station.siteDeviceListings.length; ++i) {
			if (siteDeviceList.site === jdnp3.station.siteDeviceListings[i].site) {
				jdnp3.station.siteDeviceListings[i].dirty = false;
				jdnp3.station.updateDeviceListing(jdnp3.station.siteDeviceListings[i], siteDeviceList.devices);
				return;
			} else if (jdnp3.station.siteDeviceListings[i].site > siteDeviceList.site) {
				console.log(jdnp3.station.siteDeviceListings[i].site);
				console.log(siteDeviceList.site);
				break;
			}
		}
		
		var siteContainer = document.createElement('div');
		siteContainer.className = 'site-selection-container';
		var siteName = document.createElement('div');
		siteName.className = 'site-selection-name';
		siteName.appendChild(document.createTextNode(siteDeviceList.site + ' (' + siteDeviceList.devices.length + ')'));
		siteContainer.appendChild(siteName);
		
		var siteComponent = document.createElement('div');
		siteComponent.className = 'hidden';
		siteContainer.appendChild(siteComponent);
		siteName.onclick = function(event) {
			if (siteComponent.className === 'hidden') {
				siteComponent.className = '';
			} else {
				siteComponent.className = 'hidden';
			}
		};
		
		var additionalItem = {
			'site': siteDeviceList.site,
			'dirty': false,
			'devices': [],
			'deviceMap': {},
			'component': siteComponent,
			'container': siteContainer
		};
		jdnp3.station.siteDeviceListings.splice(i, 0, additionalItem);
		mainContainer.insertBefore(additionalItem.container, mainContainer.children[i]);
		jdnp3.station.updateDeviceListing(jdnp3.station.siteDeviceListings[i], siteDeviceList.devices);
	});
	
	var listing = [];
	jdnp3.station.siteDeviceListings.forEach(function(siteData) {
		if (siteData.dirty) {
			mainContainer.removeChild(siteData.component);
		} else {
			listing.push(siteData);
		}
	});
	jdnp3.station.siteDeviceListings = listing;
}

jdnp3.station.Station = function(location) {
	this.messageHandlers = {};
	this.messageHandlers['siteList'] = new jdnp3.station.SetStationsMessageHandler();
	this.messageHandlers['searchResult'] = new jdnp3.station.SearchResultMessageHandler();
	
	var connectionListener = {};
	connectionListener.connected = function() {
		var element = document.getElementById('statusText');
		element.innerHTML = "Connected";
		element.setAttribute('style', 'color: green;');
		updateListings();
	};
	connectionListener.disconnected = function() {
		var element = document.getElementById('statusText');
		element.innerHTML = "Disconnected";
		element.setAttribute('style', 'color: red;');
	};
	
	this.messanger = new jdnp3.message.Messanger(location);
	this.messanger.setConnectionListener(connectionListener);
	this.messanger.setMessageReceiver(this);
	this.messanger.connect();
	
	var thisObject = this;
	
	var updateListings = function() {
		try {
			thisObject.messanger.sendMessage({'type': 'listDevices'});
		} catch (exception) {
			console.log('WARN: Site listing not available.');
		}
	}
	
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		updateListings();
	}, 60000, true);
}

jdnp3.station.Station.prototype.search = function(keyword) {
	var thisObject = this;

	var performSearch = function() {
		try {
			thisObject.messanger.sendMessage({'type': 'searchRequest', 'searchTerm': keyword});
		} catch (exception) {
			console.log('WARN: Unable to search.');
		}
	}

	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		performSearch();
	}, 10, false);
}

jdnp3.station.Station.prototype.globalAutoTriggerEvent = function(enable) {
	var thisObject = this;

	var performOperation = function() {
		try {
			thisObject.messanger.sendMessage({'type': 'globalAutoEvent', 'enable': enable});
		} catch (exception) {
			console.log('WARN: Unable to update flag.');
		}
	}

	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		performOperation();
	}, 10, false);
}

jdnp3.station.Station.prototype.messageReceived = function(message) {
	if (message.data) {
		messageData = message.data.replace(/([\s\[{.]"value"\s*:\s*)NaN/, '$1"NaN"');
		messageData = messageData.replace(/([\s\[{.]"value"\s*:\s*)Infinity/, '$1"Infinity"');
		messageData = messageData.replace(/([\s\[{.]"value"\s*:\s*)-Infinity/, '$1"-Infinity"');
		messageObject = JSON.parse(messageData);
		if (messageObject.type in this.messageHandlers) {
			this.messageHandlers[messageObject.type].processMessage(messageObject);
		} else {
			console.log('No hander for message type ' + messageObject.type + ' could be found.');
		}
	}
}
