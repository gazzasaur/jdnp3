var jdnp3 = jdnp3 || {};
jdnp3.doublebitbinaryinput = jdnp3.doublebitbinaryinput || {};

jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP = {};
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.online = 'ol';
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.chatterFilter = 'cf';
jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints = new jdnp3.pointlist.List(
		jdnp3.doublebitbinaryinput.insertDoubleBitBinaryInput,
		jdnp3.doublebitbinaryinput.appendDoubleBitBinaryInput,
		jdnp3.doublebitbinaryinput.updateDoubleBitBinaryInput
	);
}, 0);

jdnp3.doublebitbinaryinput.SetDoubleBitBinaryInputMessageHandler = function() {
}

jdnp3.doublebitbinaryinput.SetDoubleBitBinaryInputMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.add(dataPoint);
}

jdnp3.doublebitbinaryinput.insertDoubleBitBinaryInput = function(index, dataPoint) {
	document.getElementById('doubleBitBinaryInputs').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('doubleBitBinaryInputTable').elements
	rows[index].insertBefore(jdnp3.doublebitbinaryinput.createDoubleBitBinaryInputView(dataPoint));
	jdnp3.doublebitbinaryinput.updateDoubleBitBinaryInput(index, dataPoint);
}

jdnp3.doublebitbinaryinput.appendDoubleBitBinaryInput = function(index, dataPoint) {
	document.getElementById('doubleBitBinaryInputs').setAttribute('style', 'display: inline-block; text-align: center;');
	document.getElementById('doubleBitBinaryInputTable').appendChild(jdnp3.doublebitbinaryinput.createDoubleBitBinaryInputView(dataPoint));
	jdnp3.doublebitbinaryinput.updateDoubleBitBinaryInput(index, dataPoint);
} 

jdnp3.doublebitbinaryinput.updateDoubleBitBinaryInput = function(index, dataPoint) {
	var id = 'di-' + dataPoint.index;

	var stringValue = '' + dataPoint.value;
	if (stringValue.length > 10) {
		stringValue = parseFloat(dataPoint.value).toExponential();
	}
	document.getElementById(id + "-value").innerHTML = dataPoint.value;

	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.doublebitbinaryinput.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	jdnp3.ui.refreshDialog();
}

jdnp3.doublebitbinaryinput.createDoubleBitBinaryInputView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('di', dataPoint.index, jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints);
	view.appendInputText('value', 'Value', function() {});
	view.appendDipSwitch('cf', 'Chatter Filter', 'CF', function() {var attribute = 'chatterFilter'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDialogButton([
	    {text: 'Create Event', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		device.requestEvent('doubleBitBinaryInputEvent', jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index));
	    	}, 0);
	    }},
	    {text: 'Create Event with Time', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		jdnp3.ui.createInputPanel('Enter Event Timestamp', function(value) {
		    		device.requestEvent('doubleBitBinaryInputEvent', jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), value);
	    		});
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.doublebitbinaryinput.createRefreshCallback(index);
	    		var binaryInput = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index);
	    		jdnp3.ui.createDialog('Double Bit Binary Input ' + index + ' - ' + binaryInput.name, jdnp3.doublebitbinaryinput.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.doublebitbinaryinput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Trigger Event On Change:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('di-' + index + '-triggerEventOnChange', 'Trigger Event On Change', 'triggerEventOnChange', function() {var attribute = 'triggerEventOnChange'; var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 1, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-st-1', 'Packed', 1, function() {var value = {'group': 1, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-st-2', 'Flags', 2, function() {var value = {'group': 1, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 2, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-ev-1', 'Without Time', 1, function() {var value = {'group': 2, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-ev-2', 'Absolute Time', 2, function() {var value = {'group': 2, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('di-' + index + '-ev-3', 'Relative Time', 3, function() {var value = {'group': 2, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.doublebitbinaryinput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.doublebitbinaryinput.doubleBitBinaryinputPoints.get(index);
		
		var fieldId = 'di-' + index + '-triggerEventOnChange';
		document.getElementById(fieldId).checked = dataPoint.triggerEventOnChange;

		for (var i = 0; i < 4; ++i) {
			var fieldId = 'di-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var fieldId = 'di-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'di-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}