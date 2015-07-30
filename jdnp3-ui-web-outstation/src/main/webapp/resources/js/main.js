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
			if (message.type == 'binaryInput') {
				for (var property in message) {
					var id = 'bi-p' + message.index;
				    if (message.hasOwnProperty(property) && property in ATTRIBUTE_MAP) {
						$("[id$=" + id + "-" + ATTRIBUTE_MAP[property] + "]").prop('checked', message[property])
				    }
				}
			}
		}
	}
});

getDataPointIndex = function(id) {
	var regexArray = /[a-z0-9]+-p(\d+)/g.exec(id);
	if (!regexArray || regexArray.length != 2) {
		throw "Cannot extract an index value from the id " + id;
	}
	return parseInt(regexArray[1]);
}

getBinaryValue = function(id) {
	if (!/bi-p(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid binary input.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'binaryInput',
			'index': index,
	};
	
	if (!$('[id$=bi-p' + index + '-state]').length) {
		throw "Element " + id + " does not exist.";
	}
	data.active = $('[id$=bi-p' + index + '-state]').prop('checked')  ? true : false;
	data.chatterFilter = $('[id$=bi-p' + index + '-cf]').prop('checked')  ? true : false;
	data.localForced = $('[id$=bi-p' + index + '-lf]').prop('checked')  ? true : false;
	data.remoteForced = $('[id$=bi-p' + index + '-rf]').prop('checked')  ? true : false;
	data.communicationsLost = $('[id$=bi-p' + index + '-cl]').prop('checked')  ? true : false;
	data.restart = $('[id$=bi-p' + index + '-rs]').prop('checked')  ? true : false;
	data.online = $('[id$=bi-p' + index + '-ol]').prop('checked')  ? true : false;
	return data;
}

requestChangeValue = function(id, attribute) {
	var binaryType = /bi-p(\d+)/g.exec(id);
	if (binaryType) {
		var data = getBinaryValue(id);
		if (data.hasOwnProperty(attribute)) {
			data[attribute] = !data[attribute];
			webSocket.send(JSON.stringify(data));
		}
	}
}