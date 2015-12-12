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
	document.getElementById('analogInputs').setAttribute('style', 'display: inline-block; text-align: center;');
	var rows = document.getElementById('analogInputTable').elements
	rows[index].insertBefore(jdnp3.analoginput.createAnalogInputView(dataPoint));
	jdnp3.analoginput.updateAnalogInput(index, dataPoint);
}

jdnp3.analoginput.appendAnalogInput = function(index, dataPoint) {
	document.getElementById('analogInputs').setAttribute('style', 'display: inline-block; text-align: center;');
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
	var analogInputRow = document.createElement('tr');
	
	var analogInputNameCell = document.createElement('td');
	analogInputNameCell.className = 'full-text-field-label';
	analogInputNameCell.appendChild(document.createTextNode(name + ' (' + index + ')'));
	analogInputRow.appendChild(analogInputNameCell);
	
	var analogInputViewCell = document.createElement('td');
	var analogInputView = document.createElement('div');
	analogInputView.className = "zero-padding";
	
	var analogInputValueView = document.createElement('div');
	analogInputValueView.className = 'full-text-field-value-view';
	var analogInputValue = document.createElement('span');
	analogInputValue.id = 'ai-' + index + '-value';
	analogInputValue.className = 'full-text-field-value';
	analogInputValue.onclick = function() {
		var dataPoint = jdnp3.analoginput.analogInputPoints.get(index);

		var container = document.createElement('div');
		
		var infoDiv = document.createElement('div');
		infoDiv.setAttribute('style', 'width: 300px;');
		container.appendChild(infoDiv);
		
		var infoSpan = document.createElement('span');
		infoSpan.innerHTML = 'Enter a new value:';
		infoSpan.setAttribute('style', 'font-size: 18px;');
		infoDiv.appendChild(infoSpan);
		
		var textField = document.createElement('div');
		textField.className = 'text-field';
		textField.setAttribute('style', 'width: 100%; min-width: 250px');
		container.appendChild(textField);
		textField.title = 'Supports Infinity, -Infinity, MAX, MIN and NaN';
		
		var textFieldValue = document.createElement('input');
		textFieldValue.id = 'ai-' + index + '-newvalue';
		textFieldValue.value = dataPoint.value;
		textFieldValue.className = 'text-field-value';
		textFieldValue.setAttribute('style', 'border: none');
		textFieldValue.type = 'text';
		textFieldValue.select();
		textField.appendChild(textFieldValue);
		textFieldValue.onkeypress = function(event) {
			var dataPoint = jdnp3.analoginput.analogInputPoints.get(index);
			
			if (event.keyCode == 13) {
				var value = document.getElementById('ai-' + index + '-newvalue').value;
				if (value == 'MAX') {
					value = Number.MAX_VALUE;
				} else if (value == 'MIN') {
					value = -1*Number.MAX_VALUE;
				}
				device.requestChangeAttributeValue(dataPoint, 'value', value);
				jdnp3.ui.destroyDialog();
			} else if (event.keyCode == 27) {
				jdnp3.ui.destroyDialog();
			};
		};
	
		jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + dataPoint.name, container, function() {});
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			textFieldValue.select();
		}, 0);
	};
	analogInputValue.appendChild(document.createTextNode('0.0'));
	analogInputValueView.appendChild(analogInputValue);
	analogInputView.appendChild(analogInputValueView);
	
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Reference Error', 'RE', 'ai-' + index + '-re', function() {var attribute = 'referenceError'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Over Range', 'OR', 'ai-' + index + '-or', function() {var attribute = 'overRange'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Local Forced', 'LF', 'ai-' + index + '-lf', function() {var attribute = 'localForced'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Remote Forced', 'RF', 'ai-' + index + '-rf', function() {var attribute = 'remoteForced'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Communications Lost', 'CL', 'ai-' + index + '-cl', function() {var attribute = 'communicationsLost'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Restart', 'RS', 'ai-' + index + '-rs', function() {var attribute = 'restart'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	analogInputView.appendChild(jdnp3.ui.createDipSwitch('Online', 'OL', 'ai-' + index + '-ol', function() {var attribute = 'online'; var dataPoint = jdnp3.analoginput.analogInputPoints.get(index); device.requestChangeAttributeValue(dataPoint, attribute, !dataPoint[attribute]);}));
	
	dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block;');
	
	dropDownButton = document.createElement('input');
	dropDownButton.id = 'ai-' + index + '-showmenu';
	dropDownButton.name = 'ai-' + index + '-showmenu';
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
					var callback = jdnp3.analoginput.createRefreshCallback(index);
					var analogInput = jdnp3.analoginput.analogInputPoints.get(index);
					jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + analogInput.name, jdnp3.analoginput.createDialog(index), callback);
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
	dropDownButtonLabel.setAttribute('for', 'ai-' + index + '-showmenu');
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabelText.innerHTML = '';
	dropDownButtonLabelText.setAttribute('style', 'width: 16px; height: 8px; background-position: -32px -80px; overflow: hidden; display: block; position: absolute; left: 50%; margin-left: -8px; top: 50%; margin-top: -8px; background-repeat: no-repeat; background-image: url("/javax.faces.resource/images/ui-icons_38667f_256x240.png.jsf?ln=primefaces-aristo");');
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonView.appendChild(dropDownButtonLabel);
	
	analogInputView.appendChild(dropDownButtonView);
	
	analogInputViewCell.appendChild(analogInputView);
	analogInputRow.appendChild(analogInputViewCell);
	
	return analogInputRow;
}

jdnp3.analoginput.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
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
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 30, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-1', '32-bit Integer with Flags', 1, function() {var value = {'group': 30, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-2', '16-bit Integer with Flags', 2, function() {var value = {'group': 30, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-3', '32-bit Integer without Flags', 3, function() {var value = {'group': 30, 'variation': 3}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-4', '16-bit Integer with Flags', 4, function() {var value = {'group': 30, 'variation': 4}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-5', '32-bit Float', 5, function() {var value = {'group': 30, 'variation': 5}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-6', '64-bit Float', 6, function() {var value = {'group': 30, 'variation': 6}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analoginput.analogInputPoints.get(index), attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
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
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.analoginput.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.analoginput.analogInputPoints.get(index);
		
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