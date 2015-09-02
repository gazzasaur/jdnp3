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

jdnp3.binary.setBinary = function(binaryDataPoint) {
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
	
	$('[id$=bi-' + binaryDataPoint.index + '-sg]').html(binaryDataPoint.staticType.group);
	for (var i = 0; i < 3; ++i) {
		var id = 'bi-' + binaryDataPoint.index + '-st-' + i;
		if (i == binaryDataPoint.staticType.variation) {
			$('[id$=' + id + ']').prop('checked', 'true');
		} else {
			$('[id$=' + id + ']').prop('checked', '');
		}
	}
	
	$('[id$=bi-' + binaryDataPoint.index + '-eg]').html(binaryDataPoint.staticType.group);
	for (var i = 0; i < 4; ++i) {
		var id = 'bi-' + binaryDataPoint.index + '-ev-' + i;
		if (i == binaryDataPoint.eventType.variation) {
			$('[id$=' + id + ']').prop('checked', 'true');
		} else {
			$('[id$=' + id + ']').prop('checked', '');
		}
	}
}
