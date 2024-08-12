var jdnp3 = jdnp3 || {};
jdnp3.binaryoutput = jdnp3.binaryoutput || {};

jdnp3.binaryoutput.ATTRIBUTE_MAP = {};
jdnp3.binaryoutput.ATTRIBUTE_MAP.active = 'state';
jdnp3.binaryoutput.ATTRIBUTE_MAP.online = 'ol';
jdnp3.binaryoutput.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.binaryoutput.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.binaryoutput.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.binaryoutput.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP = {};
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.SUCCESS = 'Success';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.TIMEOUT = 'Timeout';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.NO_SELECT = 'No Select';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.FORMAT_ERROR = 'Format Error';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.NOT_SUPPORTED = 'Not Supported';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.ALREADY_ACTIVE = 'Already Active';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.HARDWARE_ERROR = 'Hardware Error';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.LOCAL = 'Local';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.TOO_MANY_OBJS = 'Too Many Objects';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.NOT_AUTHORIZED = 'Not Authorised';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.AUTOMATION_INHIBIT = 'Automation Inhibit';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.PROCESSING_LIMITED = 'Processing Limited';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.OUT_OF_RANGE = 'Out of Range';
jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP.NON_PARTICIPATING = 'Non-Participating';

jdnp3.binaryoutput.DISPLAY_NAME_STATUS_CODE_MAP = {};
for (var key in jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP) {
	jdnp3.binaryoutput.DISPLAY_NAME_STATUS_CODE_MAP[jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP[key]] = key;
}

jdnp3.binaryoutput.binaryOutputPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.binaryoutput.binaryOutputPoints = new jdnp3.pointlist.List(
		jdnp3.binaryoutput.insertBinaryOutput,
		jdnp3.binaryoutput.appendBinaryOutput,
		jdnp3.binaryoutput.updateBinaryOutput
	);
}, 0);

jdnp3.binaryoutput.SetBinaryOutputMessageHandler = function() {
}

jdnp3.binaryoutput.SetBinaryOutputMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.binaryoutput.binaryOutputPoints.add(dataPoint);
	this.updateFilter(document.getElementById('filter').value || '');
}

jdnp3.binaryoutput.SetBinaryOutputMessageHandler.prototype.updateFilter = function(filter) {
	var points = jdnp3.binaryoutput.binaryOutputPoints.points;
	var terms = filter.split(' ').map(v => v.toLowerCase());

	var validCount = 0;
	for (var point of points) {
		if (!filter) {
			document.getElementById('bo-' + point.index + '-view').style.display = '';
			validCount += 1;
			continue;
		}
		var valid = terms.map(term => {
			var v = point.name.toLowerCase().includes(term);
			for (var tagName of Object.keys(point.tags)) {
				v = v || tagName.toLowerCase().includes(term.toLowerCase());
				v = v || point.tags[tagName].toLowerCase().includes(term.toLowerCase());
			}
			return v;
		}).filter(v => !v).length == 0;
		if (!valid) {
			document.getElementById('bo-' + point.index + '-view').style.display = 'none';
		} else {
			document.getElementById('bo-' + point.index + '-view').style.display = '';
			validCount += 1;
		}
	}
	if (validCount) {
		document.getElementById('binaryOutputs').style.display = 'inline-block';
	} else {
		document.getElementById('binaryOutputs').style.display = 'none';
	}
}

jdnp3.binaryoutput.insertBinaryOutput = function(index, dataPoint) {
	document.getElementById('binaryOutputs').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('binaryOutputTable').elements
	rows[index].insertBefore(jdnp3.binaryoutput.createBinaryOutputView(dataPoint));
	jdnp3.binaryoutput.updateBinaryOutput(index, dataPoint);
}

jdnp3.binaryoutput.appendBinaryOutput = function(index, dataPoint) {
	document.getElementById('binaryOutputs').setAttribute('style', 'display: inline-block; text-align: center;');
	document.getElementById('binaryOutputTable').appendChild(jdnp3.binaryoutput.createBinaryOutputView(dataPoint));
	jdnp3.binaryoutput.updateBinaryOutput(index, dataPoint);
} 

jdnp3.binaryoutput.updateBinaryOutput = function(index, dataPoint) {
	var id = 'bo-' + dataPoint.index;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.binaryoutput.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.binaryoutput.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	
	var operationHint = 'Operation Type: ' + dataPoint.operationType + ', Trip Close Code: ' + dataPoint.tripCloseCode + ', On Time: ' + dataPoint.onTime + 'ms, Off Time: ' + dataPoint.offTime + 'ms, Pulse Count: ' + dataPoint.count;
	document.getElementById('bo-' + index + '-os').setAttribute('title', operationHint);
	var operationCountElement = document.getElementById('bo-' + index + '-oc')
	operationCountElement.innerHTML = '';
	operationCountElement.appendChild(document.createTextNode(dataPoint.operatedCount));
	
	jdnp3.ui.refreshDialog();
}

jdnp3.binaryoutput.createBinaryOutputView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('bo', dataPoint.index, jdnp3.binaryoutput.binaryOutputPoints);
	view.appendSlideSwitch('state', 'State', function() {var attribute = 'active'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	
	var operationElement = document.createElement('div');
	operationElement.id = 'bo-' + index + '-os';
	operationElement.title = 'No Operation';
	operationElement.className = 'text-field';
	operationElement.setAttribute('style', 'cursor: pointer;');
	view.appendItem(operationElement);
	
	var operationElementText = document.createElement('span');
	operationElementText.id = 'bo-' + index + '-oc';
	operationElementText.className = 'text-field-value';
	operationElementText.setAttribute('style', 'width: 100%; text-align: center;');
	operationElement.appendChild(operationElementText);
	
	view.appendDialogButton([
	    {text: 'Create Event', callback: function() {
	        jdnp3.schedule.getDefaultScheduler().addTask(function() {
	            jdnp3.ui.destroyMenu();
	            	device.requestEvent('binaryOutputEvent', jdnp3.binaryoutput.binaryOutputPoints.get(index));
	            }, 0);
	    }},
	    {text: 'Create Event with Time', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		jdnp3.ui.createInputPanel('Enter Event Timestamp', function(value) {
		    		device.requestEvent('binaryOutputEvent', jdnp3.binaryoutput.binaryOutputPoints.get(index), value);
	    		});
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.binaryoutput.createRefreshCallback(index);
	    		var binaryOutput = jdnp3.binaryoutput.binaryOutputPoints.get(index);
	    		jdnp3.ui.createDialog('Binary Output ' + index + ' - ' + binaryOutput.name, jdnp3.binaryoutput.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.binaryoutput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Trigger Event On Change:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('bo-' + index + '-triggerEventOnChange', 'Trigger Event On Change', 'triggerEventOnChange', function() {var attribute = 'triggerEventOnChange'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Return Status:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	var updateFunction = function(displayName) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var statusCode = jdnp3.binaryoutput.DISPLAY_NAME_STATUS_CODE_MAP[displayName];
			var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index);
			device.requestChangeAttributeValue(dataPoint, 'statusCode', statusCode);
		}, 0);
	};
	
	staticElementItems.appendChild(jdnp3.ui.createContextMenuButton('bo-' + index + 'returnstatus', [
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
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('bo-' + index + '-autoUpdate', 'Auto-Update on Success', 'autoUpdate', function() {var attribute = 'autoUpdateOnSuccess'; var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Command Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cc-0', 'Off', 0, function() {var value = 0; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cc-1', 'Class 1', 1, function() {var value = 1; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cc-2', 'Class 2', 2, function() {var value = 2; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-cc-3', 'Class 3', 3, function() {var value = 3; var attribute = 'commandEventClass'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 1, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-st-1', 'Packed', 1, function() {var value = {'group': 1, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-st-2', 'Flags', 2, function() {var value = {'group': 1, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 2, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ev-1', 'Without Time', 1, function() {var value = {'group': 2, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ev-2', 'Absolute Time', 2, function() {var value = {'group': 2, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Command Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ce-0', 'Any', 0, function() {var value = {'group': 13, 'variation': 0}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ce-1', 'Without Time', 1, function() {var value = {'group': 13, 'variation': 1}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bo-' + index + '-ce-2', 'Absolute Time', 2, function() {var value = {'group': 13, 'variation': 2}; var attribute = 'commandEventType'; device.requestChangeAttributeValue(jdnp3.binaryoutput.binaryOutputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)

	var tags = jdnp3.binaryoutput.binaryOutputPoints.get(index)['tags'];

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

jdnp3.binaryoutput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.binaryoutput.binaryOutputPoints.get(index);
		
		var fieldId = 'bo-' + index + '-triggerEventOnChange';
		document.getElementById(fieldId).checked = dataPoint.triggerEventOnChange;

		var textElement = document.getElementById('bo-' + index + 'returnstatus');
		var text = jdnp3.binaryoutput.STATUS_CODE_DISPLAY_NAME_MAP[jdnp3.binaryoutput.binaryOutputPoints.get(index).statusCode];
		textElement.innerHTML = '';
		textElement.appendChild(document.createTextNode(text));
		
		var fieldId = 'bo-' + index + '-autoUpdate';
		document.getElementById(fieldId).checked = dataPoint.autoUpdateOnSuccess;
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'bo-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'bo-' + index + '-cc-' + i;
			if (i == dataPoint.commandEventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var fieldId = 'bo-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var fieldId = 'bo-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var fieldId = 'bo-' + index + '-ce-' + i;
			if (i == dataPoint.commandEventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}