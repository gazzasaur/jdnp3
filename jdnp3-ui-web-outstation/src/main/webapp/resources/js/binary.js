var jdnp3 = jdnp3 || {};
jdnp3.binary = jdnp3.binary || {};

jdnp3.binary.ATTRIBUTE_MAP = {};
jdnp3.binary.ATTRIBUTE_MAP.active = 'state';
jdnp3.binary.ATTRIBUTE_MAP.online = 'ol';
jdnp3.binary.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.binary.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.binary.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.binary.ATTRIBUTE_MAP.chatterFilter = 'cf';
jdnp3.binary.ATTRIBUTE_MAP.communicationsLost = 'cl';
jdnp3.binary.ATTRIBUTE_MAP.autoUpdateOnSuccess = 'autoUpdate';

jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP = {};
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.SUCCESS = 'Success';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.TIMEOUT = 'Timeout';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.NO_SELECT = 'No Select';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.FORMAT_ERROR = 'Format Error';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.NOT_SUPPORTED = 'Not Supported';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.ALREADY_ACTIVE = 'Already Active';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.HARDWARE_ERROR = 'Hardware Error';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.LOCAL = 'Local';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.TOO_MANY_OBJS = 'Too Many Objects';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.NOT_AUTHORIZED = 'Not Authorised';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.AUTOMATION_INHIBIT = 'Automation Inhibit';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.PROCESSING_LIMITED = 'Processing Limited';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.OUT_OF_RANGE = 'Out of Range';
jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP.NON_PARTICIPATING = 'Non-Participating';

jdnp3.binary.DISPLAY_NAME_STATUS_CODE_MAP = {};
for (var key in jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP) {
	jdnp3.binary.DISPLAY_NAME_STATUS_CODE_MAP[jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP[key]] = key;
}

jdnp3.binary.getBinaryInput = function(id) {
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
	for (var property in jdnp3.binary.ATTRIBUTE_MAP) {
		data[property] = $('[id$=bi-' + index + '-' + jdnp3.binary.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}

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

jdnp3.binary.setBinaryInput = function(binaryDataPoint) {
	for (var property in binaryDataPoint) {
		var id = 'bi-' + binaryDataPoint.index;
		if (binaryDataPoint.hasOwnProperty(property) && property in jdnp3.binary.ATTRIBUTE_MAP) {
			$("[id$=" + id + "-" + jdnp3.binary.ATTRIBUTE_MAP[property] + "]").prop('checked', binaryDataPoint[property])
		}
	}
	
	for (var i = 1; i < 4; ++i) {
		var id = 'bi-' + binaryDataPoint.index + '-cl-' + i;
		if (i == binaryDataPoint.eventClass) {
			$('[id$=' + id + ']').prop('checked', 'true');
		} else {
			$('[id$=' + id + ']').prop('checked', '');
		}
	}
	
	for (var i = 0; i < 3; ++i) {
		var id = 'bi-' + binaryDataPoint.index + '-st-' + i;
		if (i == binaryDataPoint.staticType.variation) {
			$('[id$=' + id + ']').prop('checked', 'true');
		} else {
			$('[id$=' + id + ']').prop('checked', '');
		}
	}
	
	for (var i = 0; i < 4; ++i) {
		var id = 'bi-' + binaryDataPoint.index + '-ev-' + i;
		if (i == binaryDataPoint.eventType.variation) {
			$('[id$=' + id + ']').prop('checked', 'true');
		} else {
			$('[id$=' + id + ']').prop('checked', '');
		}
	}
}

jdnp3.binary.getBinaryOutput = function(id) {
	if (!/bo-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid binary input.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'binaryOutputPoint',
			'index': index,
	};

	if (!$('[id$=bo-' + index + '-state]').length) {
		throw "Element " + id + " does not exist.";
	}
	for (var property in jdnp3.binary.ATTRIBUTE_MAP) {
		data[property] = $('[id$=bo-' + index + '-' + jdnp3.binary.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}

	data.statusCode = $('[id$=bo-status-options' + index + '] span').html();
	if (data.statusCode in jdnp3.binary.DISPLAY_NAME_STATUS_CODE_MAP) {
		data.statusCode = jdnp3.binary.DISPLAY_NAME_STATUS_CODE_MAP[data.statusCode];
	}

	data.eventClass = 0;
	for (var i = 1; i < 4; ++i) {
		var id = 'bo-' + index + '-cl-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.eventClass = parseInt(regexArray[1]);
		}
	}
	
	data.commandEventClass = 0;
	for (var i = 1; i < 4; ++i) {
		var id = 'bo-' + index + '-cc-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.commandEventClass = parseInt(regexArray[1]);
		}
	}

	data.staticType = {'group': 10, 'variation': 0};
	for (var i = 0; i < 3; ++i) {
		var id = 'bo-' + index + '-st-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.staticType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.eventType = {'group': 11, 'variation': 0};
	for (var i = 0; i < 3; ++i) {
		var id = 'bo-' + index + '-ev-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.eventType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.commandEventType = {'group': 13, 'variation': 0};
	for (var i = 0; i < 3; ++i) {
		var id = 'bo-' + index + '-ce-' + i;
		if ($('[id$=' + id + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(id);
			data.commandEventType.variation = parseInt(regexArray[1]);
		}
	}
	return data;
}

jdnp3.binary.setBinaryOutput = function(binaryDataPoint) {
	for (var property in binaryDataPoint) {
		var id = 'bo-' + binaryDataPoint.index;
		if (binaryDataPoint.hasOwnProperty(property) && property in jdnp3.binary.ATTRIBUTE_MAP) {
			$("[id$=" + id + "-" + jdnp3.binary.ATTRIBUTE_MAP[property] + "]").prop('checked', binaryDataPoint[property])
		}

		if (binaryDataPoint.statusCode in jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP) {
			$('[id$=bo-status-options' + binaryDataPoint.index + '] span').html(jdnp3.binary.STATUS_CODE_DISPLAY_NAME_MAP[binaryDataPoint.statusCode]);
		} else {
			$('[id$=bo-status-options' + binaryDataPoint.index + '] span').html(binaryDataPoint.statusCode);
		}
		
		operationHint = 'Operation Type: ' + binaryDataPoint.operationType + ', Trip Close Code: ' + binaryDataPoint.tripCloseCode + ', On Time: ' + binaryDataPoint.onTime + 'ms, Off Time: ' + binaryDataPoint.offTime + 'ms, Pulse Count: ' + binaryDataPoint.count;
		$('[id$=bo-' + binaryDataPoint.index + '-os]').prop('title', operationHint);
		$('[id$=bo-' + binaryDataPoint.index + '-oc]').html(binaryDataPoint.operatedCount);
		
		for (var i = 1; i < 4; ++i) {
			var id = 'bo-' + binaryDataPoint.index + '-cl-' + i;
			if (i == binaryDataPoint.eventClass) {
				$('[id$=' + id + ']').prop('checked', 'true');
			} else {
				$('[id$=' + id + ']').prop('checked', '');
			}
		}
		
		for (var i = 1; i < 4; ++i) {
			var id = 'bo-' + binaryDataPoint.index + '-cc-' + i;
			if (i == binaryDataPoint.commandEventClass) {
				$('[id$=' + id + ']').prop('checked', 'true');
			} else {
				$('[id$=' + id + ']').prop('checked', '');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var id = 'bo-' + binaryDataPoint.index + '-st-' + i;
			if (i == binaryDataPoint.staticType.variation) {
				$('[id$=' + id + ']').prop('checked', 'true');
			} else {
				$('[id$=' + id + ']').prop('checked', '');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var id = 'bo-' + binaryDataPoint.index + '-ev-' + i;
			if (i == binaryDataPoint.eventType.variation) {
				$('[id$=' + id + ']').prop('checked', 'true');
			} else {
				$('[id$=' + id + ']').prop('checked', '');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var id = 'bo-' + binaryDataPoint.index + '-ce-' + i;
			if (i == binaryDataPoint.commandEventType.variation) {
				$('[id$=' + id + ']').prop('checked', 'true');
			} else {
				$('[id$=' + id + ']').prop('checked', '');
			}
		}
	}
}
