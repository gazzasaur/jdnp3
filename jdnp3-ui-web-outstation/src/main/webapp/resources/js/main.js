var scheduler = jdnp3.schedule.getDefaultScheduler();

var webSocket;
var webSocketMessageQueue = [];

var MESSAGE_HANDLER_REGISTRY = {};
MESSAGE_HANDLER_REGISTRY.heartbeat = function() {};
MESSAGE_HANDLER_REGISTRY.internalIndicators = jdnp3.iin.setInternalIndicators;
MESSAGE_HANDLER_REGISTRY.binaryInputPoint = jdnp3.binary.setBinaryInput;
MESSAGE_HANDLER_REGISTRY.binaryOutputPoint = jdnp3.binary.setBinaryOutput;
MESSAGE_HANDLER_REGISTRY.analogInputPoint = jdnp3.analog.setAnalog;

var ATTRIBUTE_CHANGE_HANDLER_REGISTRY = {};
ATTRIBUTE_CHANGE_HANDLER_REGISTRY.ii = jdnp3.iin.getInternalIndicators;
ATTRIBUTE_CHANGE_HANDLER_REGISTRY.bi = jdnp3.binary.getBinaryInput;
ATTRIBUTE_CHANGE_HANDLER_REGISTRY.bo = jdnp3.binary.getBinaryOutput;
ATTRIBUTE_CHANGE_HANDLER_REGISTRY.ai = jdnp3.analog.getAnalog;

var EVENT_MESSAGE_REGISTRY = {};
EVENT_MESSAGE_REGISTRY.bi = 'binaryInputEvent';
EVENT_MESSAGE_REGISTRY.ai = 'analogInputEvent';

$(document).ready(function() {
	var location = document.location.toString().replace(/\bhttp/,'ws').replace(/\/\/.*/,'//') + window.location.host + '/secure/ws/general';
	webSocket = new WebSocket(location);
	
	webSocket.onopen = function() {
		console.log('WebSocket connection open.');
	};
	webSocket.onclose = function() {
		console.log('WebSocket connection closed.');
	};
	webSocket.onerror = function(error) {
		console.log('WebSocket error detected: ' + error);
	};
	webSocket.onmessage = function(e) {
		scheduler.addTask(function() {
			if (e.data) {
				message = jQuery.parseJSON(e.data);
				if (message.type in MESSAGE_HANDLER_REGISTRY) {
					MESSAGE_HANDLER_REGISTRY[message.type](message);
				} else {
					console.log('WARN: No handler found for ' + message.type)
				}
			}
		}, 0);
	};
	scheduler.addTask(function() {
		var data = {
				'type': 'heartbeat'
		};
		webSocket.send(JSON.stringify(data));
	}, 10000, true);
});

getDataPointIndex = function(id) {
	var regexArray = /[a-z0-9]+-(\d+)/g.exec(id);
	if (!regexArray || regexArray.length != 2) {
		throw "Cannot extract an index value from the id " + id;
	}
	return parseInt(regexArray[1]);
}

requestChangeSingleAttributeValue = function(type, attribute, value) {
	scheduler.addTask(function() {
		var data = {
			type: type,
			attribute: attribute,
			value: value
		};
		webSocket.send(JSON.stringify(data));
	}, 0);
}

requestChangeAttributeValue = function(id, attribute, value) {
	scheduler.addTask(function() {
		var objectId = /([a-z]+)-(\d+)/g.exec(id);
		if (objectId && objectId[1] in ATTRIBUTE_CHANGE_HANDLER_REGISTRY) {
			var data = ATTRIBUTE_CHANGE_HANDLER_REGISTRY[objectId[1]](id);
			if (data.hasOwnProperty(attribute)) {
				data[attribute] = value;
				webSocket.send(JSON.stringify(data));
			}
		} else {
			console.log('WARN: Cannot change attribute for ' + objectId);
		}
	}, 0);
}

requestEvent = function(id) {
	scheduler.addTask(function() {
		var objectId = /([a-z]+)-(\d+)/g.exec(id);
		if (objectId && objectId[1] in EVENT_MESSAGE_REGISTRY) {
			var data = {
				'type': EVENT_MESSAGE_REGISTRY[objectId[1]],
				'index': parseInt(objectId[2])
			};
			webSocket.send(JSON.stringify(data));
		} else {
			console.log('WARN: Cannot create an event for the id ' + id);
		}
	}, 0);
}