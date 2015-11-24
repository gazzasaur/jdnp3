var jdnp3 = jdnp3 || {};
jdnp3.counter = jdnp3.counter || {};

jdnp3.counter.ATTRIBUTE_MAP = {};
jdnp3.counter.ATTRIBUTE_MAP.online = 'ol';
jdnp3.counter.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.counter.ATTRIBUTE_MAP.rollover = 'ro';
jdnp3.counter.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.counter.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.counter.ATTRIBUTE_MAP.discontinuity = 'dc';
jdnp3.counter.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.counter.getCounter = function(id) {
	if (!/co-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid counter.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'counterPoint',
			'index': index,
	};
	
	if (!$('[id$=co-' + index + '-value]').length) {
		throw "Element " + id + " does not exist.";
	}
	data.value = parseInt($('[id$=co-' + index + '-value]').html());
	for (var property in jdnp3.counter.ATTRIBUTE_MAP) {
		data[property] = $('[id$=co-' + index + '-' + jdnp3.counter.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}

	data.eventClass = 0;
	for (var i = 0; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventClass = parseInt(regexArray[1]);
		}
	}

	data.staticType = {'group': 20, 'variation': 0};
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-st-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.staticType.variation = parseInt(regexArray[1]);
		}
	}
	
	data.eventType = {'group': 22, 'variation': 0};
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if ($('[id$=' + fieldId + ']').prop('checked')) {
			var regexArray = /.*-(\d+)/g.exec(fieldId);
			data.eventType.variation = parseInt(regexArray[1]);
		}
	}
	return data;
}

jdnp3.counter.setCounter = function(dataPoint) {
	var id = 'co-' + dataPoint.index;
	
	$("[id$=" + id + "-value]").html(dataPoint.value);
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.counter.ATTRIBUTE_MAP) {
			$("[id$=" + id + "-" + jdnp3.counter.ATTRIBUTE_MAP[property] + "]").prop('checked', dataPoint[property])
		}
	}
	
	for (var i = 0; i < 4; ++i) {
		var fieldId = id + '-cl-' + i;
		if (i == dataPoint.eventClass) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	$('[id$=' + id + '-sg]').html(dataPoint.staticType.group);
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-st-' + i;
		if (i == dataPoint.staticType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
	
	$('[id$=' + id + '-eg]').html(dataPoint.staticType.group);
	for (var i = 0; i < 9; ++i) {
		var fieldId = id + '-ev-' + i;
		if (i == dataPoint.eventType.variation) {
			$('[id$=' + fieldId + ']').prop('checked', 'true');
		} else {
			$('[id$=' + fieldId + ']').prop('checked', '');
		}
	}
}
