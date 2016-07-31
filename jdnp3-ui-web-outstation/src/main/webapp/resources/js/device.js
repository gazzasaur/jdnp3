var jdnp3 = jdnp3 || {};
jdnp3.device = jdnp3.device || {};

jdnp3.device.Device = function(location) {
	this.messageHandlers = {};
	this.messageHandlers['internalIndicators'] = new jdnp3.iin.SetInternalIndicatorsMessageHandler();
	this.messageHandlers['binaryInputPoint'] = new jdnp3.binaryinput.SetBinaryInputMessageHandler();
	this.messageHandlers['binaryOutputPoint'] = new jdnp3.binaryoutput.SetBinaryOutputMessageHandler();
	this.messageHandlers['analogInputPoint'] = new jdnp3.analoginput.SetAnalogInputMessageHandler();
	this.messageHandlers['analogOutputPoint'] = new jdnp3.analogoutput.SetAnalogOutputMessageHandler();
	this.messageHandlers['counterPoint'] = new jdnp3.counter.SetCounterMessageHandler();
	this.messageHandlers['bindings'] = new jdnp3.bindings.SetBindingsMessageHandler();
	
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
}

jdnp3.device.Device.prototype.messageReceived = function(message) {
	if (message.data) {
		messageData = message.data.replace(/([\s\[{.]"value"\s*:\s*)NaN/, '$1"NaN"');
		messageData = messageData.replace(/([\s\[{.]"value"\s*:\s*)Infinity/, '$1"Infinity"');
		messageData = messageData.replace(/([\s\[{.]"value"\s*:\s*)-Infinity/, '$1"-Infinity"');
		messageObject = JSON.parse(messageData);
		
		if (!this.site && messageObject.site) {
			this.site = messageObject.site;
			this.device = messageObject.device;
			document.title = this.site + ' - ' + this.device;
		}
		if (messageObject.type in this.messageHandlers) {
			this.messageHandlers[messageObject.type].processMessage(messageObject);
		} else {
			console.log('No hander for message type ' + messageObject.type + ' could be found.');
		}
	}
}

jdnp3.device.Device.prototype.requestChangeAttributeValue = function(dataPoint, attribute, value) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		if (dataPoint.hasOwnProperty(attribute)) {
			dataPoint[attribute] = value;
			device.messanger.sendMessage(dataPoint);
		} else {
			console.log('WARN: Cannot change attribute ' + attribute + ' in ' + dataPoint);
		}
	}, 0);
}

jdnp3.device.Device.prototype.requestChangeSingleAttributeValue = function(type, attribute, value) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		var data = {
			'type': type,
			'site': device.site,
			'device': device.device,
			'attribute': attribute,
			'value': value
		};
		device.messanger.sendMessage(data);
	}, 0);
}

jdnp3.device.Device.prototype.requestEvent = function(type, dataPoint, timestamp) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		var data = {
			'type': type,
			'site': dataPoint.site,
			'device': dataPoint.device,
			'index': dataPoint.index
		};
		if (timestamp) {
			data['timestamp'] = timestamp;
		}
		device.messanger.sendMessage(data);
	}, 0);
}

jdnp3.device.Device.prototype.unbind = function(site, deviceName, dataLinkName, address) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		var data = {
			'type': 'unbindDevice',
			'site': site,
			'device': deviceName,
			'address': address,
			'dataLinkName': dataLinkName
		};
		device.messanger.sendMessage(data);
	}, 0);
}
