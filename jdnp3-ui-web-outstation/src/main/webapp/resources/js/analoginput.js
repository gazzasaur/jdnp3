var jdnp3 = jdnp3 || {};
jdnp3.analoginput = jdnp3.analoginput || {};

jdnp3.analoginput.ATTRIBUTE_MAP = {};
jdnp3.analoginput.ATTRIBUTE_MAP.online = 'ol';
jdnp3.analoginput.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.analoginput.ATTRIBUTE_MAP.overRange = 'or';
jdnp3.analoginput.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.analoginput.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.analoginput.ATTRIBUTE_MAP.referenceError = 're';
jdnp3.analoginput.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.analoginput.analogInputPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.analoginput.analogInputPoints = new jdnp3.pointlist.List(
		jdnp3.analoginput.insertAnalogInput,
		jdnp3.analoginput.appendAnalogInput,
		jdnp3.analoginput.updateAnalogInput
	);
}, 0);

jdnp3.analoginput.SetAnalogInputMessageHandler = function() {
}

jdnp3.analoginput.SetAnalogInputMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.analoginput.analogInputPoints.add(dataPoint);
}

jdnp3.analoginput.insertAnalogInput = function(index, dataPoint) {
	document.getElementById('analogInputs').removeAttribute('style');
	var rows = document.getElementById('analogInputTable').elements
	rows[index].insertBefore(jdnp3.analoginput.createAnalogInputView(dataPoint));
	jdnp3.analoginput.updateAnalogInput(index, dataPoint);
}

jdnp3.analoginput.appendAnalogInput = function(index, dataPoint) {
	document.getElementById('analogInputs').removeAttribute('style');
	document.getElementById('analogInputTable').appendChild(jdnp3.analoginput.createAnalogInputView(dataPoint));
	jdnp3.analoginput.updateAnalogInput(index, dataPoint);
} 

jdnp3.analoginput.updateAnalogInput = function(index, dataPoint) {
	var id = 'ai-' + dataPoint.index;
	
	var stringValue = '' + dataPoint.value;
	if (stringValue.length > 10) {
		stringValue = parseFloat(dataPoint.value).toExponential();
	}
	document.getElementById(id + "-value").innerHTML = dataPoint.value;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.analoginput.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.analoginput.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	jdnp3.ui.refreshDialog();
}

jdnp3.analoginput.createAnalogInputView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('ai', dataPoint.index, jdnp3.analoginput.analogInputPoints);
	view.appendInputText('value', 'Value', function() {});
	view.appendDipSwitch('re', 'Reference Error', 'RE', function() {var attribute = 'referenceError'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('or', 'Over Range', 'OR', function() {var attribute = 'overRange'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDialogButton([
		{text: 'Create Event', callback: function() {
			jdnp3.schedule.getDefaultScheduler().addTask(function() {
				jdnp3.ui.destroyMenu();
				device.requestEvent('analogInputEvent', jdnp3.analoginput.analogInputPoints.get(index));
			}, 0);
		}},
	    {text: 'Create Event with Time', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		jdnp3.ui.createInputPanel('Enter Event Timestamp', function(value) {
		    		device.requestEvent('analogInputEvent', jdnp3.analoginput.analogInputPoints.get(index), value);
	    		});
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.analoginput.createRefreshCallback(index);
	    		var analogInput = jdnp3.analoginput.analogInputPoints.get(index);
	    		jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + analogInput.name, jdnp3.analoginput.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.analoginput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Trigger Event On Change:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('ai-' + index + '-triggerEventOnChange', 'Trigger Event On Change', 'triggerEventOnChange', function() {var attribute = 'triggerEventOnChange'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 30, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-1', '32-bit Integer with Flags', 1, function() {var value = {'group': 30, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-2', '16-bit Integer with Flags', 2, function() {var value = {'group': 30, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-3', '32-bit Integer without Flags', 3, function() {var value = {'group': 30, 'variation': 3}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-4', '16-bit Integer without Flags', 4, function() {var value = {'group': 30, 'variation': 4}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-5', '32-bit Float', 5, function() {var value = {'group': 30, 'variation': 5}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-6', '64-bit Float', 6, function() {var value = {'group': 30, 'variation': 6}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 32, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-1', '32-bit Integer without Time', 1, function() {var value = {'group': 32, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-2', '16-bit Integer without Time', 2, function() {var value = {'group': 32, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-3', '32-bit Integer Absolute Time', 3, function() {var value = {'group': 32, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-4', '16-bit Integer Absolute Time', 4, function() {var value = {'group': 32, 'variation': 4}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-5', '32-bit Float without Time', 5, function() {var value = {'group': 32, 'variation': 5}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-6', '64-bit Float without Time', 6, function() {var value = {'group': 32, 'variation': 6}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-7', '32-bit Float Absolute Time', 7, function() {var value = {'group': 32, 'variation': 7}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-8', '64-bit Float Absolute Time', 8, function() {var value = {'group': 32, 'variation': 8}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	var tags = jdnp3.analoginput.analogInputPoints.get(index)['tags'];

	if (Object.keys(tags).length) {
		var staticElementRow = document.createElement('tr');
		var staticElementCell = document.createElement('td');
		var staticElementHeader = document.createElement('h3');
		staticElementHeader.appendChild(document.createTextNode('Tags'));
		staticElementCell.appendChild(staticElementHeader);
		staticElementRow.appendChild(staticElementCell);
		table.appendChild(staticElementRow)
	}

	for (var tag of Object.keys(tags)) {
		var staticElementRow = document.createElement('tr');
		var staticElementCell = document.createElement('td');
		staticElementCell.setAttribute('style', 'padding-top: 0.5em; padding-bottom: 0.5em;');
		staticElementCell.appendChild(document.createTextNode(tag));
		staticElementRow.appendChild(staticElementCell);
		var staticElementItems = document.createElement('td');
		staticElementItems.setAttribute('style', 'padding-top: 0.5em; padding-bottom: 0.5em;');
		staticElementItems.appendChild(document.createTextNode(tags[tag]));
		staticElementRow.appendChild(staticElementItems);
		table.appendChild(staticElementRow)
	}

	parent.appendChild(table);
	
	return parent;
}

jdnp3.analoginput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.analoginput.analogInputPoints.get(index);

		var fieldId = 'ai-' + index + '-triggerEventOnChange';
		document.getElementById(fieldId).checked = dataPoint.triggerEventOnChange;

		for (var i = 0; i < 4; ++i) {
			var fieldId = 'ai-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 7; ++i) {
			var fieldId = 'ai-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 9; ++i) {
			var fieldId = 'ai-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}