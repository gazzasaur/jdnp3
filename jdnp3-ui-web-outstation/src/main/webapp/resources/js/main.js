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
		if (e.data) {
			message = jQuery.parseJSON(e.data);
			console.log(message);
			if (message.type == 'binaryInput') {
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
				
				for (var i = 0; i < 3; ++i) {
					var id = 'bi-' + message.index + '-st-' + i;
					if (i == message.staticVariation) {
						$('[id$=' + id + ']').prop('checked', 'true');
					} else {
						$('[id$=' + id + ']').prop('checked', '');
					}
				}
				
				for (var i = 0; i < 4; ++i) {
					var id = 'bi-' + message.index + '-ev-' + i;
					if (i == message.eventVariation) {
						$('[id$=' + id + ']').prop('checked', 'true');
					} else {
						$('[id$=' + id + ']').prop('checked', '');
					}
				}
			}
		}
	}
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
			'type': 'binaryInput',
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

	data.staticVariation = 0;
	for (var i = 0; i < 3; ++i) {
		var id = 'bi-' + index + '-st-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.staticVariation = parseInt(regexArray[1]);
		}
	}
	
	data.eventVariation = 0;
	for (var i = 0; i < 4; ++i) {
		var id = 'bi-' + index + '-ev-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.eventVariation = parseInt(regexArray[1]);
		}
	}
	return data;
}

requestChangeValue = function(id, attribute) {
	var binaryType = /bi-(\d+)/g.exec(id);
	if (binaryType) {
		var data = getBinaryValue(id);
		if (data.hasOwnProperty(attribute)) {
			data[attribute] = !data[attribute];
			console.log(data);
			webSocket.send(JSON.stringify(data));
		}
	}
}

requestChangeAttributeValue = function(id, attribute, value) {
	var binaryType = /bi-(\d+)/g.exec(id);
	if (binaryType) {
		var data = getBinaryValue(id);
		if (data.hasOwnProperty(attribute)) {
			data[attribute] = value;
			console.log(data);
			webSocket.send(JSON.stringify(data));
		}
	}
}
