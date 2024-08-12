var jdnp3 = jdnp3 || {};
jdnp3.binaryinput = jdnp3.binaryinput || {};

jdnp3.binaryinput.ATTRIBUTE_MAP = {};
jdnp3.binaryinput.ATTRIBUTE_MAP.active = 'state';
jdnp3.binaryinput.ATTRIBUTE_MAP.online = 'ol';
jdnp3.binaryinput.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.binaryinput.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.binaryinput.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.binaryinput.ATTRIBUTE_MAP.chatterFilter = 'cf';
jdnp3.binaryinput.ATTRIBUTE_MAP.communicationsLost = 'cl';

jdnp3.binaryinput.binaryInputPoints = null;
jdnp3.binaryinput.filteredBinaryInputPoints = null;
jdnp3.schedule.getDefaultScheduler().addTask(function() {
	jdnp3.binaryinput.binaryInputPoints = new jdnp3.pointlist.List(
		jdnp3.binaryinput.insertBinaryInput,
		jdnp3.binaryinput.appendBinaryInput,
		jdnp3.binaryinput.updateBinaryInput
	);
}, 0);

jdnp3.binaryinput.SetBinaryInputMessageHandler = function() {
}

jdnp3.binaryinput.SetBinaryInputMessageHandler.prototype.processMessage = function(dataPoint) {
	jdnp3.binaryinput.binaryInputPoints.add(dataPoint);
	this.updateFilter(document.getElementById('filter').value || '');
}

jdnp3.binaryinput.SetBinaryInputMessageHandler.prototype.updateFilter = function(filter) {
	var points = jdnp3.binaryinput.binaryInputPoints.points;
	var terms = filter.split(' ').map(v => v.toLowerCase());

	var validCount = 0;
	for (var point of points) {
		if (!filter) {
			document.getElementById('bi-' + point.index + '-view').style.display = '';
			validCount += 1;
			continue;
		}
		var valid = terms.map(term => point.name.toLowerCase().includes(term)).filter(v => !v).length == 0;
		if (!valid) {
			document.getElementById('bi-' + point.index + '-view').style.display = 'none';
		} else {
			document.getElementById('bi-' + point.index + '-view').style.display = '';
			validCount += 1;
		}
	}
	if (validCount) {
		document.getElementById('binaryInputs').style.display = 'inline-block';
	} else {
		document.getElementById('binaryInputs').style.display = 'none';
	}
}

jdnp3.binaryinput.insertBinaryInput = function(index, dataPoint) {
	document.getElementById('binaryInputs').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('binaryInputTable').elements
	rows[index].insertBefore(jdnp3.binaryinput.createBinaryInputView(dataPoint));
	jdnp3.binaryinput.updateBinaryInput(index, dataPoint);
}

jdnp3.binaryinput.appendBinaryInput = function(index, dataPoint) {
	document.getElementById('binaryInputs').setAttribute('style', 'display: inline-block; text-align: center;');
	document.getElementById('binaryInputTable').appendChild(jdnp3.binaryinput.createBinaryInputView(dataPoint));
	jdnp3.binaryinput.updateBinaryInput(index, dataPoint);
} 

jdnp3.binaryinput.updateBinaryInput = function(index, dataPoint) {
	var id = 'bi-' + dataPoint.index;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.binaryinput.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.binaryinput.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	jdnp3.ui.refreshDialog();
}

jdnp3.binaryinput.createBinaryInputView = function(dataPoint) {
	var name = dataPoint.name;
	var index = dataPoint.index;
	
	var view = new jdnp3.ui.DataPointItem('bi', dataPoint.index, jdnp3.binaryinput.binaryInputPoints);
	view.appendSlideSwitch('state', 'State', function() {var attribute = 'active'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cf', 'Chatter Filter', 'CF', function() {var attribute = 'chatterFilter'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('lf', 'Local Forced', 'LF', function() {var attribute = 'localForced'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rf', 'Remote Forced', 'RF', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('cl', 'Communications Lost', 'CL', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('rs', 'Restart', 'RS', function() {var attribute = 'restart'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDipSwitch('ol', 'Online', 'OL', function() {var attribute = 'online'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);});
	view.appendDialogButton([
	    {text: 'Create Event', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		device.requestEvent('binaryInputEvent', jdnp3.binaryinput.binaryInputPoints.get(index));
	    	}, 0);
	    }},
	    {text: 'Create Event with Time', callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		jdnp3.ui.createInputPanel('Enter Event Timestamp', function(value) {
		    		device.requestEvent('binaryInputEvent', jdnp3.binaryinput.binaryInputPoints.get(index), value);
	    		});
	    	}, 0);
	    }},
	    {text: 'Edit', separate: true, callback: function() {
	    	jdnp3.schedule.getDefaultScheduler().addTask(function() {
	    		jdnp3.ui.destroyMenu();
	    		var refreshCallback = jdnp3.binaryinput.createRefreshCallback(index);
	    		var binaryInput = jdnp3.binaryinput.binaryInputPoints.get(index);
	    		jdnp3.ui.createDialog('Binary Input ' + index + ' - ' + binaryInput.name, jdnp3.binaryinput.createDialog(index), refreshCallback);
	    		refreshCallback(index);
	    	}, 0);
	    }}
	]);
	return view.getComponent();
}

jdnp3.binaryinput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Trigger Event On Change:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');
	
	staticElementItems.appendChild(jdnp3.ui.createSlideSwitch('bi-' + index + '-triggerEventOnChange', 'Trigger Event On Change', 'triggerEventOnChange', function() {var attribute = 'triggerEventOnChange'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);

	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 1, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-st-1', 'Packed', 1, function() {var value = {'group': 1, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-st-2', 'Flags', 2, function() {var value = {'group': 1, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 2, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-ev-1', 'Without Time', 1, function() {var value = {'group': 2, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-ev-2', 'Absolute Time', 2, function() {var value = {'group': 2, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('bi-' + index + '-ev-3', 'Relative Time', 3, function() {var value = {'group': 2, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.binaryinput.binaryInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)

	var tags = jdnp3.binaryinput.binaryInputPoints.get(index)['tags'];

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

jdnp3.binaryinput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index);
		
		var fieldId = 'bi-' + index + '-triggerEventOnChange';
		document.getElementById(fieldId).checked = dataPoint.triggerEventOnChange;

		for (var i = 0; i < 4; ++i) {
			var fieldId = 'bi-' + index + '-cl-' + i;
			if (i == dataPoint.eventClass) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 3; ++i) {
			var fieldId = 'bi-' + index + '-st-' + i;
			if (i == dataPoint.staticType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
		
		for (var i = 0; i < 4; ++i) {
			var fieldId = 'bi-' + index + '-ev-' + i;
			if (i == dataPoint.eventType.variation) {
				document.getElementById(fieldId).setAttribute('checked', 'true');
			} else {
				document.getElementById(fieldId).removeAttribute('checked');
			}
		}
	}
}