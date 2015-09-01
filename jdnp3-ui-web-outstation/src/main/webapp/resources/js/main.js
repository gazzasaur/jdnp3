var scheduler = jdnp3.schedule.getDefaultScheduler();

var webSocket;
var webSocketMessageQueue = [];

var ATTRIBUTE_MAP = {};
ATTRIBUTE_MAP.active = 'state';
ATTRIBUTE_MAP.online = 'ol';
ATTRIBUTE_MAP.restart = 'rs';
ATTRIBUTE_MAP.localForced = 'lf';
ATTRIBUTE_MAP.remoteForced = 'rf';
ATTRIBUTE_MAP.chatterFilter = 'cf';
ATTRIBUTE_MAP.communicationsLost = 'cl';

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
				if (message.type == 'binaryInputPoint') {
					
					for (var property in message) {
						var id = 'bi-' + message.index;
					    if (message.hasOwnProperty(property) && property in ATTRIBUTE_MAP) {
							$("[id$=" + id + "-" + ATTRIBUTE_MAP[property] + "]").prop('checked', message[property])
					    }
					}
					
					for (var i = 1; i < 4; ++i) {
						var id = 'bi-' + message.index + '-cl-' + i;
						if (i == message.eventClass) {
							$('[id$=' + id + ']').prop('checked', 'true');
						} else {
							$('[id$=' + id + ']').prop('checked', '');
						}
					}
					
					$('[id$=bi-' + message.index + '-sg]').html(message.staticType.group);
					for (var i = 0; i < 3; ++i) {
						var id = 'bi-' + message.index + '-st-' + i;
						if (i == message.staticType.variation) {
							$('[id$=' + id + ']').prop('checked', 'true');
						} else {
							$('[id$=' + id + ']').prop('checked', '');
						}
					}
					
					$('[id$=bi-' + message.index + '-eg]').html(message.staticType.group);
					for (var i = 0; i < 4; ++i) {
						var id = 'bi-' + message.index + '-ev-' + i;
						if (i == message.eventType.variation) {
							$('[id$=' + id + ']').prop('checked', 'true');
						} else {
							$('[id$=' + id + ']').prop('checked', '');
						}
					}
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

getBinaryValue = function(id) {
	if (!/bi-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid binary input.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'binaryInputPoint',
			'index': index,
	};
	
	if (!$('[id$=bi-' + index + '-state]').length) {
		throw "Element " + id + " does not exist.";
	}
	data.active = $('[id$=bi-' + index + '-state]').prop('checked')  ? true : false;
	data.chatterFilter = $('[id$=bi-' + index + '-cf]').prop('checked')  ? true : false;
	data.localForced = $('[id$=bi-' + index + '-lf]').prop('checked')  ? true : false;
	data.remoteForced = $('[id$=bi-' + index + '-rf]').prop('checked')  ? true : false;
	data.communicationsLost = $('[id$=bi-' + index + '-cl]').prop('checked')  ? true : false;
	data.restart = $('[id$=bi-' + index + '-rs]').prop('checked')  ? true : false;
	data.online = $('[id$=bi-' + index + '-ol]').prop('checked')  ? true : false;

	data.eventClass = 0;
	for (var i = 1; i < 4; ++i) {
		var id = 'bi-' + index + '-cl-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.eventClass = parseInt(regexArray[1]);
		}
	}

	data.staticType = {'group': 1, 'variation': 0};
	for (var i = 0; i < 3; ++i) {
		var id = 'bi-' + index + '-st-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.staticType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.eventType = {'group': 2, 'variation': 0};
	for (var i = 0; i < 4; ++i) {
		var id = 'bi-' + index + '-ev-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.eventType.variation = parseInt(regexArray[1]);
		}
	}
	return data;
}

requestChangeValue = function(id, attribute) {
	scheduler.addTask(function() {
		var binaryType = /bi-(\d+)/g.exec(id);
		if (binaryType) {
			var data = getBinaryValue(id);
			if (data.hasOwnProperty(attribute)) {
				data[attribute] = !data[attribute];
				console.log(data);
				webSocket.send(JSON.stringify(data));
			}
		}
	}, 0);
}

requestChangeAttributeValue = function(id, attribute, value) {
	scheduler.addTask(function() {
		var binaryType = /bi-(\d+)/g.exec(id);
		if (binaryType) {
			var data = getBinaryValue(id);
			if (data.hasOwnProperty(attribute)) {
				data[attribute] = value;
				console.log(data);
				webSocket.send(JSON.stringify(data));
			}
		}
	}, 0);
}

requestEvent = function(id) {
	scheduler.addTask(function() {
		var binaryType = /bi-(\d+)/g.exec(id);
		if (binaryType) {
			var data = {
				'type': 'binaryInputEvent',
				'index': parseInt(binaryType[1])
			};
			webSocket.send(JSON.stringify(data));
		}
	}, 0);
}