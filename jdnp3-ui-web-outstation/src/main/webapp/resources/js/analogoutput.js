var jdnp3 = jdnp3 || {};
jdnp3.analogoutput = jdnp3.analogoutput || {};

jdnp3.analogoutput.ATTRIBUTE_MAP = {};
jdnp3.analogoutput.ATTRIBUTE_MAP.online = 'ol';
jdnp3.analogoutput.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.analogoutput.ATTRIBUTE_MAP.overRange = 'or';
jdnp3.analogoutput.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.analogoutput.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.analogoutput.ATTRIBUTE_MAP.referenceError = 're';
jdnp3.analogoutput.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP = {};
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.SUCCESS = 'Success';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.TIMEOUT = 'Timeout';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.NO_SELECT = 'No Select';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.FORMAT_ERROR = 'Format Error';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.NOT_SUPPORTED = 'Not Supported';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.ALREADY_ACTIVE = 'Already Active';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.HARDWARE_ERROR = 'Hardware Error';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.LOCAL = 'Local';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.TOO_MANY_OBJS = 'Too Many Objects';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.NOT_AUTHORIZED = 'Not Authorised';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.AUTOMATION_INHIBIT = 'Automation Inhibit';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.PROCESSING_LIMITED = 'Processing Limited';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.OUT_OF_RANGE = 'Out of Range';
jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP.NON_PARTICIPATING = 'Non-Participating';

jdnp3.analogoutput.DISPLAY_NAME_STATUS_CODE_MAP = {};
for (var key in jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP) {
	jdnp3.analogoutput.DISPLAY_NAME_STATUS_CODE_MAP[jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP[key]] = key;
}

jdnp3.analogoutput.analogOutputPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.analogoutput.analogOutputPoints = new jdnp3.pointlist.List(
		jdnp3.analogoutput.insertAnalogOutput,
		jdnp3.analogoutput.appendAnalogOutput,
		jdnp3.analogoutput.updateAnalogOutput
	);
}, 0);

jdnp3.analogoutput.SetAnalogOutputMessageHandler = function() {
}

jdnp3.analogoutput.SetAnalogOutputMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.analogoutput.analogOutputPoints.add(dataPoint);
}

jdnp3.analogoutput.insertAnalogOutput = function(index, dataPoint) {
	document.getElementById('analogOutputs').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('analogOutputTable').elements
	rows[index].insertBefore(jdnp3.analogoutput.createAnalogOutputView(dataPoint));
	jdnp3.analogoutput.updateAnalogOutput(index, dataPoint);
}

jdnp3.analogoutput.appendAnalogOutput = function(index, dataPoint) {
	document.getElementById('analogOutputs').setAttribute('style', 'display: inline-block; text-align: center;');
	document.getElementById('analogOutputTable').appendChild(jdnp3.analogoutput.createAnalogOutputView(dataPoint));
	jdnp3.analogoutput.updateAnalogOutput(index, dataPoint);
} 

jdnp3.analogoutput.updateAnalogOutput = function(index, dataPoint) {
	var id = 'ao-' + dataPoint.index;
	
	var stringValue = '' + dataPoint.value;
	if (stringValue.length > 10) {
		stringValue = parseFloat(dataPoint.value).toExponential();
	}
	document.getElementById(id + "-value").innerHTML = dataPoint.value;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.analogoutput.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.analogoutput.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	
	var operationHint = 'Analog Output';
	document.getElementById('ao-' + index + '-os').setAttribute('title', operationHint);
	var operationCountElement = document.getElementById('ao-' + index + '-oc')
	operationCountElement.innerHTML = '';
	operationCountElement.appendChild(document.createTextNode(dataPoint.operatedCount));
	
	jdnp3.ui.refreshDialog();
}

jdnp3.analogoutput.createAnalogOutputView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('ao', dataPoint.index, jdnp3.analogoutput.analogOutputPoints);
	view.appendInputText('value', 'Value', function() {});
	view.appendDipSwitch('re', 'Reference Error', 'RE', function() {var attribute = 'referenceError'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('or', 'Over Range', 'OR', function() {var attribute = 'overRange'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	
	var operationElement = document.createElement('div');
	operationElement.id = 'ao-' + index + '-os';
	operationElement.title = 'No Operation';
	operationElement.className = 'text-field';
	operationElement.setAttribute('style', 'width: 100px; cursor: pointer;');
	view.appendItem(operationElement);
	
	var operationElementText = document.createElement('span');
	operationElementText.id = 'ao-' + index + '-oc';
	operationElementText.className = 'text-field-value';
	operationElementText.setAttribute('style', 'width: 100%; text-align: center;');
	operationElement.appendChild(operationElementText);
	
	view.appendDialogButton([
		{text: 'Create Event', callback: function() {
			jdnp3.schedule.getDefaultScheduler().addTask(function() {
				jdnp3.ui.destroyMenu();
				device.requestEvent('analogOutputEvent', jdnp3.analogoutput.analogOutputPoints.get(index));
			}, 0);
		}},
	    {text: 'Create Event with Time', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		jdnp3.ui.createInputPanel('Enter Event Timestamp', function(value) {
		    		device.requestEvent('analogOutputEvent', jdnp3.analogoutput.analogOutputPoints.get(index), value);
	    		});
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.analogoutput.createRefreshCallback(index);
	    		var analogOutput = jdnp3.analogoutput.analogOutputPoints.get(index);
	    		jdnp3.ui.createDialog('Analog Output ' + index + ' - ' + analogOutput.name, jdnp3.analogoutput.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.analogoutput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Return Status:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	var updateFunction = function(displayName) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var statusCode = jdnp3.analogoutput.DISPLAY_NAME_STATUS_CODE_MAP[displayName];
			var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index);
			device.requestChangeAttributeValue(dataPoint, 'statusCode', statusCode);
		}, 0);
	};
	
	staticElementItems.appendChild(jdnp3.ui.createContextMenuButton('ao-' + index + 'returnstatus', [
	   {text: 'Success', callback: function() {updateFunction('Success');}},
	   {text: 'Timeout', separate: true, callback: function() {updateFunction('Timeout');}},
	   {text: 'No Select', callback: function() {updateFunction('No Select');}},
	   {text: 'Format Error', callback: function() {updateFunction('Format Error');}},
	   {text: 'Not Supported', callback: function() {updateFunction('Not Supported');}},
	   {text: 'Already Active', callback: function() {updateFunction('Already Active');}},
	   {text: 'Hardware Error', callback: function() {updateFunction('Hardware Error');}},
	   {text: 'Local', callback: function() {updateFunction('Local');}},
	   {text: 'Too Many Objects', callback: function() {updateFunction('Too Many Objects');}},
	   {text: 'Not Authorised', callback: function() {updateFunction('Not Authorised');}},
	   {text: 'Automation Inhibit', callback: function() {updateFunction('Automation Inhibit');}},
	   {text: 'Processing Limited', callback: function() {updateFunction('Processing Limited');}},
	   {text: 'Out of Range', callback: function() {updateFunction('Out of Range');}},
	   {text: 'Non-Participating', callback: function() {updateFunction('Non-Participating');}}
	], {orientation: 'bottom', style: 'width: 300px', menuStyle: 'width: 300px; padding: 0px;'}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Auto-Update on Success:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('ao-' + index + '-autoUpdate', 'Auto-Update on Success', 'autoUpdate', function() {var attribute = 'autoUpdateOnSuccess'; var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Command Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cc-0', 'Off', 0, function() {var value = 0; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cc-1', 'Class 1', 1, function() {var value = 1; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cc-2', 'Class 2', 2, function() {var value = 2; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-cc-3', 'Class 3', 3, function() {var value = 3; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 40, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-st-1', '32-bit Integer', 1, function() {var value = {'group': 40, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-st-2', '16-bit Integer', 2, function() {var value = {'group': 40, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-st-3', '32-bit Float', 3, function() {var value = {'group': 40, 'variation': 3}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-st-4', '64-bit Float', 4, function() {var value = {'group': 40, 'variation': 4}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 42, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-1', '32-bit Integer Without Time', 1, function() {var value = {'group': 42, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-2', '16-bit Integer Without Time', 2, function() {var value = {'group': 42, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-3', '32-bit Integer Absolute Time', 3, function() {var value = {'group': 42, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-4', '16-bit Integer Absolute Time', 4, function() {var value = {'group': 42, 'variation': 4}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-5', '32-bit Float Without Time', 5, function() {var value = {'group': 42, 'variation': 5}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-6', '64-bit Float Without Time', 6, function() {var value = {'group': 42, 'variation': 6}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-7', '32-bit Float Absolute Time', 7, function() {var value = {'group': 42, 'variation': 7}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ev-8', '64-bit Float Absolute Time', 8, function() {var value = {'group': 42, 'variation': 8}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Command Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-0', 'Any', 0, function() {var value = {'group': 43, 'variation': 0}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-1', '32-bit Integer Without Time', 1, function() {var value = {'group': 43, 'variation': 1}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-2', '16-bit Integer Without Time', 2, function() {var value = {'group': 43, 'variation': 2}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-3', '32-bit Integer Absolute Time', 3, function() {var value = {'group': 43, 'variation': 3}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-4', '16-bit Integer Absolute Time', 4, function() {var value = {'group': 43, 'variation': 4}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-5', '32-bit Float Without Time', 5, function() {var value = {'group': 43, 'variation': 5}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-6', '64-bit Float Without Time', 6, function() {var value = {'group': 43, 'variation': 6}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-7', '32-bit Float Absolute Time', 7, function() {var value = {'group': 43, 'variation': 7}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ao-' + index + '-ce-8', '64-bit Float Absolute Time', 8, function() {var value = {'group': 43, 'variation': 8}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.analogoutput.analogOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.analogoutput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.analogoutput.analogOutputPoints.get(index);
		
		var textElement = document.getElementById('ao-' + index + 'returnstatus');
		var text = jdnp3.analogoutput.STATUS_CODE_DISPLAY_NAME_MAP[jdnp3.analogoutput.analogOutputPoints.get(index).statusCode];
		textElement.innerHTML = '';
		textElement.appendChild(document.createTextNode(text));
		
		var fieldId = 'ao-' + index + '-autoUpdate';
		document.getElementById(fieldId).checked = dataPoint.autoUpdateOnSuccess;
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'ao-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'ao-' + index + '-cc-' + i;
			if (i == dataPoint.commandEventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 5; ++i) {
			var fieldId = 'ao-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 9; ++i) {
			var fieldId = 'ao-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 9; ++i) {
			var fieldId = 'ao-' + index + '-ce-' + i;
			if (i == dataPoint.commandEventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}