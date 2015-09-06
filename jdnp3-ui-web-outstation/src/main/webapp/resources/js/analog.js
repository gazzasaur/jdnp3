var jdnp3 = jdnp3 || {};
jdnp3.analog = jdnp3.analog || {};

jdnp3.analog.ATTRIBUTE_MAP = {};
jdnp3.analog.ATTRIBUTE_MAP.online = 'ol';
jdnp3.analog.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.analog.ATTRIBUTE_MAP.overRange = 'or';
jdnp3.analog.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.analog.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.analog.ATTRIBUTE_MAP.referenceError = 're';
jdnp3.analog.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.analog.getAnalog = function(id) {
	if (!/ai-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid analog input.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'analogInputPoint',
			'index': index,
	};
	
	if (!$('[id$=ai-' + index + '-value]').length) {
		throw "Element " + id + " does not exist.";
	}
	data.value = parseFloat($('[id$=ai-' + index + '-value]').html());
	for (var property in jdnp3.analog.ATTRIBUTE_MAP) {
		data[property] = $('[id$=ai-' + index + '-' + jdnp3.analog.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}

	data.eventClass = 0;
	for (var i = 1; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventClass = parseInt(regexArray[1]);
		}
	}

	data.staticType = {'group': 30, 'variation': 0};
	for (var i = 0; i < 7; ++i) {
		var fieldId = id + '-st-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.staticType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.eventType = {'group': 32, 'variation': 0};
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventType.variation = parseInt(regexArray[1]);
		}
	}
	return data;
}

jdnp3.analog.setAnalog = function(analogDataPoint) {
	var id = 'ai-' + analogDataPoint.index;
	
	var stringValue = '' + analogDataPoint.value;
	if (stringValue.length > 10) {
		stringValue = parseFloat(analogDataPoint.value).toExponential();
	}
	$("[id$=" + id + "-value]").html(analogDataPoint.value);
	
	for (var property in analogDataPoint) {
		if (analogDataPoint.hasOwnProperty(property) && property in jdnp3.analog.ATTRIBUTE_MAP) {
			$("[id$=" + id + "-" + jdnp3.analog.ATTRIBUTE_MAP[property] + "]").prop('checked', analogDataPoint[property])
		}
	}
	
	for (var i = 1; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if (i == analogDataPoint.eventClass) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	$('[id$=' + id + '-sg]').html(analogDataPoint.staticType.group);
	for (var i = 0; i < 7; ++i) {
		var fieldId = id + '-st-' + i;
		if (i == analogDataPoint.staticType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	$('[id$=' + id + '-eg]').html(analogDataPoint.staticType.group);
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if (i == analogDataPoint.eventType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
}
