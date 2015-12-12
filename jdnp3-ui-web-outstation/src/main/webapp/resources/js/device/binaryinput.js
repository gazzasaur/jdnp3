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
	var binaryInputRow = document.createElement('tr');
	
	var binaryInputNameCell = document.createElement('td');
	binaryInputNameCell.className = 'full-text-field-label';
	binaryInputNameCell.appendChild(document.createTextNode(name + ' (' + index + ')'));
	binaryInputRow.appendChild(binaryInputNameCell);
	
	var binaryInputViewCell = document.createElement('td');
	var binaryInputView = document.createElement('div');
	binaryInputView.className = "zero-padding";
	
	binaryInputView.appendChild(jdnp3.ui.createSlideSwitch('bi-' + index + '-state', 'State', 'bi-' + index + '-state', function() {var attribute = 'active'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Chatter Filter', 'CF', 'bi-' + index + '-cf', function() {var attribute = 'chatterFilter'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Local Forced', 'LF', 'bi-' + index + '-lf', function() {var attribute = 'localForced'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Remote Forced', 'Remote Forced', 'bi-' + index + '-rf', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Communications Lost', 'CL', 'bi-' + index + '-cl', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Restart', 'RS', 'bi-' + index + '-rs', function() {var attribute = 'restart'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	binaryInputView.appendChild(jdnp3.ui.createDipSwitch('Online', 'OL', 'bi-' + index + '-ol', function() {var attribute = 'online'; var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	
	dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block;');
	
	dropDownButton = document.createElement('input');
	dropDownButton.id = 'bi-' + index + '-showmenu';
	dropDownButton.name = 'bi-' + index + '-showmenu';
	dropDownButton.className = 'eventbutton-checkbox';
	dropDownButton.setAttribute('type', 'button');
	dropDownButton.onclick = function(event) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var menu = document.createElement('div');
			menu.className = 'drop-down-menu';
			
			var createEvent = document.createElement('div');
			menu.appendChild(createEvent);
			var createEventSpan = document.createElement('span');
			createEvent.setAttribute('style', 'padding: 10px;')
			createEvent.className = 'drop-down-menu-button';
			createEvent.appendChild(createEventSpan);
			createEventSpan.innerHTML = 'Create Event';
			
			var separator = document.createElement('hr');
			separator.setAttribute('style', 'color: #FFFFFF; margin-top: 0px; margin-bottom: 0px; margin-left: auto; margin-right: auto; width: 95%');
			menu.appendChild(separator);
			
			var edit = document.createElement('div');
			menu.appendChild(edit);
			var createEventSpan = document.createElement('span');
			edit.setAttribute('style', 'padding: 10px;')
			edit.className = 'drop-down-menu-button';
			edit.appendChild(createEventSpan);
			createEventSpan.innerHTML = 'Edit';
			edit.onclick = function() {
				jdnp3.schedule.getDefaultScheduler().addTask(function() {
					jdnp3.ui.destroyMenu();
					var callback = jdnp3.binaryinput.createRefreshCallback(index);
					var binaryInput = jdnp3.binaryinput.binaryInputPoints.get(index);
					jdnp3.ui.createDialog('Binary Input ' + index + ' - ' + binaryInput.name, jdnp3.binaryinput.createDialog(index), callback);
					callback(index);
				}, 0);
			};
			
			var parent = event.target.parentElement;
			jdnp3.ui.createMenu(parent, menu);
		}, 0);
	};
	dropDownButtonView.appendChild(dropDownButton);
	
	dropDownButtonLabel = document.createElement('label');
	dropDownButtonLabel.className = 'glossy-button';
	dropDownButtonLabel.setAttribute('for', 'bi-' + index + '-showmenu');
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabelText.innerHTML = '';
	dropDownButtonLabelText.setAttribute('style', 'width: 16px; height: 8px; background-position: -32px -80px; overflow: hidden; display: block; position: absolute; left: 50%; margin-left: -8px; top: 50%; margin-top: -8px; background-repeat: no-repeat; background-image: url("/javax.faces.resource/images/ui-icons_38667f_256x240.png.jsf?ln=primefaces-aristo");');
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonView.appendChild(dropDownButtonLabel);
	
	binaryInputView.appendChild(dropDownButtonView);
	
	binaryInputViewCell.appendChild(binaryInputView);
	binaryInputRow.appendChild(binaryInputViewCell);
	
	return binaryInputRow;
}

jdnp3.binaryinput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
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
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.binaryinput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.binaryinput.binaryInputPoints.get(index);
		
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