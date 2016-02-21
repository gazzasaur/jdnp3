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

jdnp3.iin.internalIndicators = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.iin.internalIndicators = new jdnp3.pointlist.List(
		jdnp3.iin.insertInternalIndicators,
		jdnp3.iin.appendInternalIndicators,
		jdnp3.iin.updateInternalIndicators
	);
}, 0);

jdnp3.iin.SetInternalIndicatorsMessageHandler = function() {
}

jdnp3.iin.SetInternalIndicatorsMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.iin.internalIndicators.add(dataPoint);
}

jdnp3.iin.insertInternalIndicators = function(index, dataPoint) {
	document.getElementById('internalIndicators').setAttribute('style', 'display: block; margin-bottom: 5px;');
	var rows = document.getElementById('internalIndicatorsTable').elements
	rows[index].insertBefore(jdnp3.iin.createInternalIndicatorsView(dataPoint));
	jdnp3.iin.updateInternalIndicators(index, dataPoint);
}

jdnp3.iin.appendInternalIndicators = function(index, dataPoint) {
	device.site = dataPoint.site;
	device.device = dataPoint.device;
	document.getElementById('internalIndicators').setAttribute('style', 'display: block; margin-bottom: 5px;');
	document.getElementById('internalIndicatorsTable').appendChild(jdnp3.iin.createInternalIndicatorsView(dataPoint));
	jdnp3.iin.updateInternalIndicators(index, dataPoint);
} 

jdnp3.iin.updateInternalIndicators = function(index, dataPoint) {
	var id = 'iin-' + dataPoint.index;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.iin.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.iin.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	jdnp3.ui.refreshDialog();
}

jdnp3.iin.createInternalIndicatorsView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('iin', dataPoint.index, jdnp3.iin.internalIndicators);
	view.appendDipSwitch('bc', 'Broadcast', '1.0', function() {var attribute = 'broadcast'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('c1', 'Class 1 Events', '1.1', function() {var attribute = 'class1Events'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('c2', 'Class 2 Events', '1.2', function() {var attribute = 'class2Events'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('c3', 'Class 3 Events', '1.3', function() {var attribute = 'class3Events'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('nt', 'Need Time', '1.4', function() {var attribute = 'needTime'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lc', 'Local Control', '1.5', function() {var attribute = 'localControl'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('dt', 'Device Trouble', '1.6', function() {var attribute = 'deviceTrouble'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('dr', 'Device Restart', '1.7', function() {var attribute = 'deviceRestart'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('nf', 'No Function Code Support', '2.0', function() {var attribute = 'noFunctionCodeSupport'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ou', 'Object Unknown', '2.1', function() {var attribute = 'objectUnknown'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('pe', 'Parameter Error', '2.2', function() {var attribute = 'parameterError'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('eo', 'Event Buffer Overflow', '2.3', function() {var attribute = 'eventBufferOverflow'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ae', 'Already Executing', '2.4', function() {var attribute = 'alreadyExecuting'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cc', 'Configuration Corrupt', '2.5', function() {var attribute = 'configurationCorrupt'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);});
	view.appendDialogButton([
		{text: 'Edit', separate: false, callback: function() {
			jdnp3.schedule.getDefaultScheduler().addTask(function() {
				jdnp3.ui.destroyMenu();
				var refreshCallback = jdnp3.iin.createRefreshCallback();
				jdnp3.ui.createDialog('Internal Indicators', jdnp3.iin.createDialog(index), refreshCallback);
				refreshCallback(index);
			}, 0);
		}}
	]);
	return view.getComponent();
}


jdnp3.iin.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Readonly:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('iin-' + index + '-readonly', 'Readonly', 'readonly', function() {var attribute = 'readonly'; var dataPoint = jdnp3.iin.internalIndicators.get(index); device.requestChangeSingleAttributeValue('internalIndicator', attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.iin.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.iin.internalIndicators.get(index);
		
		var fieldId = 'iin-' + index + '-readonly';
		document.getElementById(fieldId).checked = dataPoint.readonly;
	}
}