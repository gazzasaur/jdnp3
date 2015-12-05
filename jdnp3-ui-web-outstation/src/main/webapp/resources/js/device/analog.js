var jdnp3 = jdnp3 || {};
jdnp3.analog = jdnp3.analog || {};

jdnp3.analog.AnalogInputPoints = [];

jdnp3.analog.SetAnalogInputMessageHandler = function() {
}

jdnp3.analog.SetAnalogInputMessageHandler.prototype.processMessage = function(dataPoint) {
	var i = 0;
	for (var i = 0; i < jdnp3.analog.AnalogInputPoints.length; ++i) {
		if (jdnp3.analog.AnalogInputPoints['index'] <= dataPoint['index']) {
			break;
		}
	}
	
	if (i == jdnp3.analog.AnalogInputPoints.length) {
		jdnp3.analog.AnalogInputPoints.push(dataPoint);
		var analogInputsView = document.getElementById('analogInputs');
		analogInputsView.appendChild(document.createTextNode(dataPoint['name']));
	} else if (jdnp3.analog.AnalogInputPoints[i]['index'] == dataPoint['index']) {
		jdnp3.analog.AnalogInputPoints.splice(i, 1, dataPoint);
	} else {
		jdnp3.analog.AnalogInputPoints.splice(i, 0, dataPoint);
	}
}
