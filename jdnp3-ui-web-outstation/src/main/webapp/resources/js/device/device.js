var jdnp3 = jdnp3 || {};
jdnp3.device = jdnp3.device || {};

jdnp3.device.Device = function(location) {
	this.messageHandlers = {};
	this.messageHandlers['internalIndicators'] = new jdnp3.iin.SetInternalIndicatorsMessageHandler();
	this.messageHandlers['binaryInputPoint'] = new jdnp3.binaryinput.SetBinaryInputMessageHandler();
	this.messageHandlers['binaryOutputPoint'] = new jdnp3.binaryoutput.SetBinaryOutputMessageHandler();
	this.messageHandlers['analogInputPoint'] = new jdnp3.analoginput.SetAnalogInputMessageHandler();
	this.messageHandlers['analogOutputPoint'] = new jdnp3.analogoutput.SetAnalogOutputMessageHandler();
	
	this.messanger = new jdnp3.message.Messanger(location);
	this.messanger.setMessageReceiver(this);
	this.messanger.connect();
}

jdnp3.device.Device.prototype.messageReceived = function(message) {
	if (message.data) {
		messageObject = jQuery.parseJSON(message.data);
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

jdnp3.device.Device.prototype.requestEvent = function(type, dataPoint) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		var data = {
			'type': type,
			'site': dataPoint.site,
			'device': dataPoint.device,
			'index': dataPoint.index
		};
		device.messanger.sendMessage(data);
	}, 0);
}
