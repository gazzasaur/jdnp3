var jdnp3 = jdnp3 || {};
jdnp3.iin = jdnp3.iin || {};

jdnp3.iin.ATTRIBUTE_MAP = {};
jdnp3.iin.ATTRIBUTE_MAP.broadcast = 'bc';
jdnp3.iin.ATTRIBUTE_MAP.class1Events = 'c1';
jdnp3.iin.ATTRIBUTE_MAP.class2Events = 'c2';
jdnp3.iin.ATTRIBUTE_MAP.class3Events = 'c3';
jdnp3.iin.ATTRIBUTE_MAP.needTime = 'nt';
jdnp3.iin.ATTRIBUTE_MAP.localControl = 'lc';
jdnp3.iin.ATTRIBUTE_MAP.deviceTrouble = 'dt';
jdnp3.iin.ATTRIBUTE_MAP.deviceRestart = 'dr';
jdnp3.iin.ATTRIBUTE_MAP.noFunctionCodeSupport = 'nf';
jdnp3.iin.ATTRIBUTE_MAP.objectUnknown = 'ou';
jdnp3.iin.ATTRIBUTE_MAP.parameterError = 'pe';
jdnp3.iin.ATTRIBUTE_MAP.eventBufferOverflow = 'eo';
jdnp3.iin.ATTRIBUTE_MAP.alreadyExecuting = 'ae';
jdnp3.iin.ATTRIBUTE_MAP.configurationCorrupt = 'cc';

jdnp3.iin.getInternalIndicators = function(id) {
	if (!/ii-(\d+)/g.exec(id)) {
		throw "Element " + id + " is not a valid internal indicators field.";
	}
	
	var index = getDataPointIndex(id);
	var data = {
			'type': 'internalIndicators',
	};
	
	for (var property in jdnp3.iin.ATTRIBUTE_MAP) {
		data[property] = $('[id$=ii-' + index + '-' + jdnp3.iin.ATTRIBUTE_MAP[property] + ']').prop('checked')  ? true : false;
	}
	return data;
}

jdnp3.iin.setInternalIndicators = function(iinDataPoint) {
	for (var property in iinDataPoint) {
		var id = 'ii-0';
		if (iinDataPoint.hasOwnProperty(property) && property in jdnp3.iin.ATTRIBUTE_MAP) {
			$("[id$=" + id + "-" + jdnp3.iin.ATTRIBUTE_MAP[property] + "]").prop('checked', iinDataPoint[property])
		}
	}
}
