var jdnp3 = jdnp3 || {};
jdnp3.analog = jdnp3.analog || {};

jdnp3.analog.ATTRIBUTE_MAP = {};
jdnp3.analog.ATTRIBUTE_MAP.online = 'ol';
jdnp3.analog.ATTRIBUTE_MAP.restart = 'rs';
jdnp3.analog.ATTRIBUTE_MAP.overRange = 'or';
jdnp3.analog.ATTRIBUTE_MAP.localForced = 'lf';
jdnp3.analog.ATTRIBUTE_MAP.remoteForced = 'rf';
jdnp3.analog.ATTRIBUTE_MAP.referenceError = 're';
jdnp3.analog.ATTRIBUTE_MAP.communicationsLost = 'cl';
jdnp3.analog.ATTRIBUTE_MAP.autoUpdateOnSuccess = 'autoUpdate';

jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP = {};
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.SUCCESS = 'Success';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.TIMEOUT = 'Timeout';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NO_SELECT = 'No Select';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.FORMAT_ERROR = 'Format Error';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NOT_SUPPORTED = 'Not Supported';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.ALREADY_ACTIVE = 'Already Active';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.HARDWARE_ERROR = 'Hardware Error';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.LOCAL = 'Local';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.TOO_MANY_OBJS = 'Too Many Objects';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NOT_AUTHORIZED = 'Not Authorised';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.AUTOMATION_INHIBIT = 'Automation Inhibit';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.PROCESSING_LIMITED = 'Processing Limited';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.OUT_OF_RANGE = 'Out of Range';
jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP.NON_PARTICIPATING = 'Non-Participating';

jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP = {};
for (var key in jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP) {
	jdnp3.analog.DISPLAY_NAME_STATUS_CODE_MAP[jdnp3.analog.STATUS_CODE_DISPLAY_NAME_MAP[key]] = key;
}

jdnp3.analog.AnalogInputPoints = [];

jdnp3.analog.SetAnalogInputMessageHandler = function() {
}

jdnp3.analog.SetAnalogInputMessageHandler.prototype.processMessage = function(dataPoint) {
	var i = 0;
	var index = 0;
	for (var i = 0; i < jdnp3.analog.AnalogInputPoints.length; ++i) {
		if (jdnp3.analog.AnalogInputPoints[i]['index'] > dataPoint['index']) {
			break;
		} else if (jdnp3.analog.AnalogInputPoints[i]['index'] == dataPoint['index']) {
			index = i;
			break;
		}
		index = i;
	}
	
	if (i == jdnp3.analog.AnalogInputPoints.length) {
		document.getElementById('analogInputTable').appendChild(jdnp3.analog.createAnalogInputView(dataPoint.index, dataPoint.name));
		jdnp3.analog.AnalogInputPoints.push(dataPoint);
	} else if (jdnp3.analog.AnalogInputPoints[i]['index'] == dataPoint['index']) {
		jdnp3.analog.AnalogInputPoints.splice(i, 1, dataPoint);
	} else {
		jdnp3.analog.AnalogInputPoints.splice(i, 0, dataPoint);
	}
	
	var id = 'ai-' + dataPoint.index;
	
	var stringValue = '' + dataPoint.value;
	if (stringValue.length > 10) {
		stringValue = parseFloat(dataPoint.value).toExponential();
	}
	document.getElementById(id + "-value").innerHTML = dataPoint.value;
	
	for (var property in dataPoint) {
		if (dataPoint.hasOwnProperty(property) && property in jdnp3.analog.ATTRIBUTE_MAP) {
			document.getElementById(id + "-" + jdnp3.analog.ATTRIBUTE_MAP[property]).checked = dataPoint[property];
		}
	}
	
	jdnp3.schedule.getDefaultScheduler().addTask(function() {jdnp3.ui.refreshDialog()}, 0);
}

jdnp3.analog.createAnalogInputView = function(index, name) {
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
		var container = document.createElement('div');
		
		var textField = document.createElement('div');
		textField.className = 'text-field';
		textField.setAttribute('style', 'width: 100%; min-width: 250px');
		container.appendChild(textField);
		
		var textFieldValue = document.createElement('input');
		textFieldValue.className = 'text-field-value';
		textFieldValue.setAttribute('style', 'border: none');
		textFieldValue.type = 'text';
		textField.appendChild(textFieldValue);
		textFieldValue.onkeypress = function() {if (event.keyCode == 13) {if ($('[id$=ai-#{cc.attrs.point.index}-newvalue]').val() == 'MAX') $('[id$=ai-#{cc.attrs.point.index}-newvalue]').val(Number.MAX_VALUE); if ($('[id$=ai-#{cc.attrs.point.index}-newvalue]').val() == 'MIN') $('[id$=ai-#{cc.attrs.point.index}-newvalue]').val(-1*Number.MAX_VALUE); requestChangeAttributeValue('ai-#{cc.attrs.point.index}', 'value', $('[id$=ai-#{cc.attrs.point.index}-newvalue]').val()); PF('ai-value#{cc.attrs.point.index}').hide();} else if (event.keyCode == 27) {PF('ai-value#{cc.attrs.point.index}').hide();};}
	
		jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + jdnp3.analog.AnalogInputPoints[index].name, container, function() {});
//		<div class="text-field">
//			<h:inputText id="ai-#{cc.attrs.point.index}-newvalue" style="border: none;" styleClass="text-field-value" title="Value" " />
//		</div>
//	</p:outputPanel>
//	<p:outputPanel>
//		<div>
//			<h:outputText style="font-size: 8px;" value="Also supports Infinity, -Infinity, MAX, MIN and NaN" />
//		</div>
	};
	analogInputValue.appendChild(document.createTextNode('0.0'));
	analogInputValueView.appendChild(analogInputValue);
	analogInputView.appendChild(analogInputValueView);
	
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Reference Error', 'RE', 'ai-' + index + '-re', function() {var attribute = 'referenceError'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Over Range', 'OR', 'ai-' + index + '-or', function() {var attribute = 'overRange'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Local Forced', 'LF', 'ai-' + index + '-lf', function() {var attribute = 'localForced'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Remote Forced', 'RF', 'ai-' + index + '-rf', function() {var attribute = 'remoteForced'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Communications Lost', 'CL', 'ai-' + index + '-cl', function() {var attribute = 'communicationsLost'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Restart', 'RS', 'ai-' + index + '-rs', function() {var attribute = 'restart'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Online', 'OL', 'ai-' + index + '-ol', function() {var attribute = 'online'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, !jdnp3.analog.AnalogInputPoints[index][attribute]);}));
	
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
					var callback = jdnp3.analog.createRefreshCallback(index);
					var analogInput = jdnp3.analog.AnalogInputPoints[index];
					jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + analogInput.name, jdnp3.analog.createDialog(index), callback);
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

jdnp3.analog.createDialog = function(index) {
	var parent = document.createElement('div');
	var table = document.createElement('table');
	table.setAttribute('cellpadding', '5');
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Class:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;');

	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-0', 'Off', 0, function() {var value = 0; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-1', 'Class 1', 1, function() {var value = 1; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-2', 'Class 2', 2, function() {var value = 2; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-cl-3', 'Class 3', 3, function() {var value = 3; var attribute = 'eventClass'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	
	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow);
	
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Static Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-0', 'Any', 0, function() {var value = {'group': 30, 'variation': 0}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-1', '32-bit Integer with Flags', 1, function() {var value = {'group': 30, 'variation': 1}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-2', '16-bit Integer with Flags', 2, function() {var value = {'group': 30, 'variation': 2}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-3', '32-bit Integer without Flags', 3, function() {var value = {'group': 30, 'variation': 3}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-4', '16-bit Integer with Flags', 4, function() {var value = {'group': 30, 'variation': 4}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-5', '32-bit Float', 5, function() {var value = {'group': 30, 'variation': 5}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-st-6', '64-bit Float', 6, function() {var value = {'group': 30, 'variation': 6}; var attribute = 'staticType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
		
	var staticElementRow = document.createElement('tr');
	var staticElementCell = document.createElement('td');
	staticElementCell.appendChild(document.createTextNode('Event Type:'));
	staticElementRow.appendChild(staticElementCell);
	var staticElementItems = document.createElement('td');
	staticElementItems.setAttribute('style', 'text-align: right;')
	
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-0', 'Any', 0, function() {var value = {'group': 32, 'variation': 0}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-1', '32-bit Integer without Time', 1, function() {var value = {'group': 32, 'variation': 1}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-2', '16-bit Integer without Time', 2, function() {var value = {'group': 32, 'variation': 2}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-3', '32-bit Integer Absolute Time', 3, function() {var value = {'group': 32, 'variation': 3}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-4', '16-bit Integer Absolute Time', 4, function() {var value = {'group': 32, 'variation': 4}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-5', '32-bit Float without Time', 5, function() {var value = {'group': 32, 'variation': 5}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-6', '64-bit Float without Time', 6, function() {var value = {'group': 32, 'variation': 6}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-7', '32-bit Float Absolute Time', 7, function() {var value = {'group': 32, 'variation': 7}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));
	staticElementItems.appendChild(jdnp3.ui.createCheckbox('ai-' + index + '-ev-8', '64-bit Float Absolute Time', 8, function() {var value = {'group': 32, 'variation': 8}; var attribute = 'eventType'; device.requestChangeAttributeValue(jdnp3.analog.AnalogInputPoints[index], attribute, value);}, {'style': 'margin-left: 5px;'}));

	staticElementRow.appendChild(staticElementItems);
	table.appendChild(staticElementRow)
	
	parent.appendChild(table);
	
	return parent;
}

jdnp3.analog.createRefreshCallback = function(index) {
	return function() {
		var dataPoint = jdnp3.analog.AnalogInputPoints[index];
		
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