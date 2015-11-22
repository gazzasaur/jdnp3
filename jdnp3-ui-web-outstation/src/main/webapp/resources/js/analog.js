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
jdnp3.analog.ATTRIBUTE_MAP.autoUpdateOnSuccess = 'autoUpdate';

jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP = {};
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.SUCCESS = 'Success';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.TIMEOUT = 'Timeout';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NO_SELECT = 'No Select';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.FORMAT_ERROR = 'Format Error';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NOT_SUPPORTED = 'Not Supported';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.ALREADY_ACTIVE = 'Already Active';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.HARDWARE_ERROR = 'Hardware Error';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.LOCAL = 'Local';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.TOO_MANY_OBJS = 'Too Many Objects';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NOT_AUTHORIZED = 'Not Authorised';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.AUTOMATION_INHIBIT = 'Automation Inhibit';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.PROCESSING_LIMITED = 'Processing Limited';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.OUT_OF_RANGE = 'Out of Range';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NON_PARTICIPATING = 'Non-Participating';

jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP = {};
for (var key in jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP) {
	jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP[jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP[key]] = key;
}

jdnp3.analog.getAnalogInput = function(id) {
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
	for (var i = 0; i < 4; ++i) {
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

jdnp3.analog.setAnalogInput = function(analogDataPoint) {
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
	
	for (var i = 0; i < 4; ++i) {
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

jdnp3.analog.getAnalogOutput = function(id) {
	if (!/ao-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid analog output.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'analogOutputPoint',
			'index': index,
	};
	
	if (!$('[id$=ao-' + index + '-value]').length) {
		throw "Element " + id + " does not exist.";
	}
	data.value = parseFloat($('[id$=ao-' + index + '-value]').html());
	for (var property in jdnp3.analog.ATTRIBUTE_MAP) {
		data[property] = $('[id$=ao-' + index + '-' + jdnp3.analog.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}

	data.statusCode = $('[id$=ao-status-options' + index + '] span').html();
	if (data.statusCode in jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP) {
		data.statusCode = jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP[data.statusCode];
	}

	data.eventClass = 0;
	for (var i = 0; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventClass = parseInt(regexArray[1]);
		}
	}
	
	data.commandEventClass = 0;
	for (var i = 0; i < 4; ++i) {
		var fieldId = id + '-cc-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.commandEventClass = parseInt(regexArray[1]);
		}
	}

	data.staticType = {'group': 40, 'variation': 0};
	for (var i = 0; i < 7; ++i) {
		var fieldId = id + '-st-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.staticType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.eventType = {'group': 42, 'variation': 0};
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.commandEventType = {'group': 43, 'variation': 0};
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ce-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.commandEventType.variation = parseInt(regexArray[1]);
		}
	}

	return data;
}

jdnp3.analog.setAnalogOutput = function(analogDataPoint) {
	var id = 'ao-' + analogDataPoint.index;
	
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
	
	if (analogDataPoint.statusCode in jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP) {
		$('[id$=ao-status-options' + analogDataPoint.index + '] span').html(jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP[analogDataPoint.statusCode]);
	} else {
		$('[id$=ao-status-options' + analogDataPoint.index + '] span').html(analogDataPoint.statusCode);
	}
	
	$('[id$=ao-' + analogDataPoint.index + '-oc]').html(analogDataPoint.operatedCount);

	for (var i = 0; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if (i == analogDataPoint.eventClass) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}

	for (var i = 0; i < 4; ++i) {
		var fieldId = 'ao-' + analogDataPoint.index + '-cc-' + i;
		if (i == analogDataPoint.commandEventClass) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}

	for (var i = 0; i < 5; ++i) {
		var fieldId = id + '-st-' + i;
		if (i == analogDataPoint.staticType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if (i == analogDataPoint.eventType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ce-' + i;
		if (i == analogDataPoint.commandEventType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
}
