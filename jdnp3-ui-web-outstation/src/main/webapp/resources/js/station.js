var jdnp3 = jdnp3 || {};
jdnp3.station = jdnp3.station || {};

jdnp3.station.SetStationsMessageHandler = function() {
}

jdnp3.station.SetStationsMessageHandler.prototype.processMessage = function(stationMessage) {
	var mainContainer = document.getElementById('device-selection');
	mainContainer.innerHTML = '';
	
	var mainList = document.createElement('ul');
	mainContainer.appendChild(mainList);
	stationMessage.siteDeviceLists.forEach(function(siteDeviceList) {
		var siteListItem = document.createElement('li');
		var siteListText = document.createElement('span');
		siteListText.appendChild(document.createTextNode(siteDeviceList.site));
		siteListItem.appendChild(siteListText);
		mainList.appendChild(siteListItem);
		
		var deviceList = document.createElement('ul');
		siteListItem.appendChild(deviceList);
		siteDeviceList.devices.forEach(function(deviceName) {
			var deviceListItem = document.createElement('li');
			var deviceLink = document.createElement('a');
			var link = 'device.jsf?stationCode=' + encodeURIComponent(siteDeviceList.site) + '&deviceCode=' + encodeURIComponent(deviceName);
			deviceLink.setAttribute('href', link);
			deviceLink.setAttribute('target', '_blank');
			deviceLink.appendChild(document.createTextNode(deviceName));
			deviceListItem.appendChild(deviceLink);
			deviceList.appendChild(deviceListItem);
		});
	});
}

jdnp3.station.Station = function(location) {
	this.messageHandlers = {};
	this.messageHandlers['siteList'] = new jdnp3.station.SetStationsMessageHandler();
	
	var connectionListener = {};
	connectionListener.connected = function() {
		var element = document.getElementById('statusText');
		element.innerHTML = "Connected";
		element.setAttribute('style', 'color: green;');
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
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		try {
			thisObject.messanger.sendMessage({'type': 'listDevices'});
		} catch (exception) {
			console.log('WARN: Site listing not available.');
		}
	}, 5000, true);
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
