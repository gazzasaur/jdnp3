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
	
//	if (document.getElementById(id + '-cl-' + 0) == null) {
//		return;
//	}
//	
//	for (var i = 0; i < 4; ++i) {
//		var fieldId = id + '-cl-' + i;
//		if (i == dataPoint.eventClass) {
//			$('[id$=' + fieldId + ']').prop('checked', 'true');
//		} else {
//			$('[id$=' + fieldId + ']').prop('checked', '');
//		}
//	}
//	
//	$('[id$=' + id + '-sg]').html(analogDataPoint.staticType.group);
//	for (var i = 0; i < 7; ++i) {
//		var fieldId = id + '-st-' + i;
//		if (i == analogDataPoint.staticType.variation) {
//			$('[id$=' + fieldId + ']').prop('checked', 'true');
//		} else {
//			$('[id$=' + fieldId + ']').prop('checked', '');
//		}
//	}
//	
//	$('[id$=' + id + '-eg]').html(analogDataPoint.staticType.group);
//	for (var i = 0; i < 9; ++i) {
//		var fieldId = id + '-ev-' + i;
//		if (i == analogDataPoint.eventType.variation) {
//			$('[id$=' + fieldId + ']').prop('checked', 'true');
//		} else {
//			$('[id$=' + fieldId + ']').prop('checked', '');
//		}
//	}
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
	analogInputValue.appendChild(document.createTextNode('0.0'));
	analogInputValue.setAttribute('onclick', "PF('main-dialog').show()");
	analogInputValueView.appendChild(analogInputValue);
	analogInputView.appendChild(analogInputValueView);
	
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Reference Error', 'RE', 'ai-' + index + '-re'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Over Range', 'OR', 'ai-' + index + '-or'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Local Forced', 'LF', 'ai-' + index + '-lf'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Remote Forced', 'RF', 'ai-' + index + '-rf'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Communications Lost', 'CL', 'ai-' + index + '-cl'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Restart', 'RS', 'ai-' + index + '-rs'));
	analogInputView.appendChild(jdnp3.ui.creatDipSwitch('Online', 'OL', 'ai-' + index + '-ol'));
	
	dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block;');
	
	dropDownButton = document.createElement('input');
	dropDownButton.id = 'ai-' + index + '-menu';
	dropDownButton.name = 'ai-' + index + '-menu';
	dropDownButton.className = 'eventbutton-checkbox';
	dropDownButton.setAttribute('type', 'button');
	dropDownButtonView.appendChild(dropDownButton);
	
	dropDownButtonLabel = document.createElement('label');
	dropDownButtonLabel.className = 'glossy-button';
	dropDownButtonLabel.setAttribute('for', 'ai-' + index + '-menu');
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