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

jdnp3.counter.counterPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.counter.counterPoints = new jdnp3.pointlist.List(
		jdnp3.counter.insertCounter,
		jdnp3.counter.appendCounter,
		jdnp3.counter.updateCounter
	);
}, 0);

jdnp3.counter.SetCounterMessageHandler = function() {
}

jdnp3.counter.SetCounterMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.counter.counterPoints.add(dataPoint);
}

jdnp3.counter.insertCounter = function(index, dataPoint) {
	document.getElementById('counters').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('counterTable').elements
	rows[index].insertBefore(jdnp3.counter.createCounterView(dataPoint));
	jdnp3.counter.updateCounter(index, dataPoint);
}

jdnp3.counter.appendCounter = function(index, dataPoint) {
	document.getElementById('counters').setAttribute('style', 'display: inline-block; text-align: center;');
	document.getElementById('counterTable').appendChild(jdnp3.counter.createCounterView(dataPoint));
	jdnp3.counter.updateCounter(index, dataPoint);
} 

jdnp3.counter.updateCounter = function(index, dataPoint) {
	var id = 'ci-' + dataPoint.index;
	
	document.getElementById(id + "-value").innerHTML = '';
	document.getElementById(id + "-value").appendChild(document.createTextNode(dataPoint.value));

	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.counter.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.counter.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	jdnp3.ui.refreshDialog();
}

jdnp3.counter.createCounterView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('ci', dataPoint.index, jdnp3.counter.counterPoints);
	view.appendInputText('value', 'Value', function() {});
	view.appendDipSwitch('dc', 'Discontinuity', 'DC', function() {var attribute = 'discontinuity'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ro', 'Rollover', 'RO', function() {var attribute = 'rollover'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.counter.counterPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDialogButton([
	    {text: 'Create Event', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		device.requestEvent('counterEvent', jdnp3.counter.counterPoints.get(index));
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.counter.createRefreshCallback(index);
	    		var counter = jdnp3.counter.counterPoints.get(index);
	    		jdnp3.ui.createDialog('Counter ' + index + ' - ' + counter.name, jdnp3.counter.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.counter.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 20, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-1', '32-bit Integer With Flags', 1, function() {var value = {'group': 20, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-2', '16-bit Integer With Flags', 2, function() {var value = {'group': 20, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-3', '32-bit Integer Delta With Flags', 3, function() {var value = {'group': 20, 'variation': 3}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-4', '16-bit Integer Delta With Flags', 4, function() {var value = {'group': 20, 'variation': 4}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-5', '32-bit Integer Without Flags', 5, function() {var value = {'group': 20, 'variation': 5}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-6', '16-bit Integer Without Flags', 6, function() {var value = {'group': 20, 'variation': 6}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-7', '32-bit Integer Delta Without Flags', 7, function() {var value = {'group': 20, 'variation': 7}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-st-8', '16-bit Integer Delta Without Flags', 8, function() {var value = {'group': 20, 'variation': 8}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 22, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-1', '32-bit Integer Without Time', 1, function() {var value = {'group': 22, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-2', '16-bit Integer Without Time', 2, function() {var value = {'group': 22, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-3', '32-bit Integer Delta Without Time', 3, function() {var value = {'group': 22, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-4', '16-bit Integer Delta Without Time', 4, function() {var value = {'group': 22, 'variation': 4}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-5', '32-bit Integer Absolute Time', 5, function() {var value = {'group': 22, 'variation': 5}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-6', '16-bit Integer Absolute Time', 6, function() {var value = {'group': 22, 'variation': 6}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-7', '32-bit Integer Delta Absolute Time', 7, function() {var value = {'group': 22, 'variation': 7}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ci-' + index + '-ev-8', '16-bit Integer Delta Absolute Time', 8, function() {var value = {'group': 22, 'variation': 8}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.counter.counterPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.counter.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.counter.counterPoints.get(index);
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'ci-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 8; ++i) {
			var fieldId = 'ci-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 8; ++i) {
			var fieldId = 'ci-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}