var jdnp3 = jdnp3 || {};
jdnp3.ui = jdnp3.ui || {};

jdnp3.ui.dialog = {};
jdnp3.ui.dialog['refreshCallback'] = null;

jdnp3.ui.createDialog = function(title, component, refreshCallback) {
	var mainDialog = document.getElementById('main-dialog');
	mainDialog.innerHTML = '';

	var mainDialogClose = document.createElement('div');
	var mainDialogCloseButton = document.createElement('div');
	mainDialogCloseButton.innerHTML = '&#x2717;';
	mainDialogCloseButton.style.border = 'none';
	mainDialogCloseButton.style.display = 'inline-block';
	mainDialogCloseButton.style.cursor = 'pointer';
	mainDialogCloseButton.style.fontSize = '1.5em';
	mainDialogCloseButton.onclick = () => {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			jdnp3.ui.destroyDialog();
		}, 0);
	};
	mainDialogClose.style.textAlign = 'right';
	mainDialogClose.appendChild(mainDialogCloseButton);
	mainDialog.appendChild(mainDialogClose);

	var mainDialogTitle = document.createElement('div');
	mainDialogTitle.id = 'main-dialog-title';
	mainDialogTitle.appendChild(document.createTextNode(title));
	mainDialog.appendChild(mainDialogTitle);
	
	jdnp3.ui.dialog['refreshCallback'] = refreshCallback;
	mainDialog.appendChild(component);
	document.getElementById('main-dialog-container').removeAttribute('style');
	document.getElementById('main-dialog-container').onkeydown = function(event) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			if (event.key == 'Escape') {
				jdnp3.ui.destroyDialog();
			} else if (event.key == 'Enter') {
				jdnp3.ui.destroyDialog();
			};
		}, 0);
	}

	document.getElementById('main-dialog-container').focus();
}

jdnp3.ui.destroyDialog = function() {
	var mainDialog = document.getElementById('main-dialog');
	jdnp3.ui.dialog.refreshCallback = null;
	mainDialog.innerHTML = '';
	document.getElementById('main-dialog-container').setAttribute('style', 'display: none;');
}

jdnp3.ui.refreshDialog = function() {
	if (jdnp3.ui.dialog.refreshCallback != null) {
		jdnp3.ui.dialog.refreshCallback();
	}
}

jdnp3.ui.createMenu = function(target, component, data) {
	var data = data || {};
	var mainMenu = document.getElementById('main-menu');
	mainMenu.innerHTML = '';
	mainMenu.appendChild(component);
	
	var top = target.getBoundingClientRect().top;
	var left = target.getBoundingClientRect().left;
	mainMenu.setAttribute('style', 'top: ' + top + 'px; left: ' + left + 'px; ' + (data['style'] || ''));
}

jdnp3.ui.destroyMenu = function() {
	var mainMenu = document.getElementById('main-menu');
	mainMenu.innerHTML = '';
	mainMenu.setAttribute('style', 'display: none;');
}

jdnp3.ui.DataPointItem = function(idPrefix, dataPointIndex, dataPointList) {
	var dataPoint = dataPointList.get(dataPointIndex);
	this.dataPointIndex = dataPointIndex;
	this.dataPointList = dataPointList;
	this.idPrefix = idPrefix;
	
	this.container = document.createElement('tr');
	this.container.id = idPrefix + '-' + this.dataPointIndex + '-view';

	if (dataPoint.name) {
		var nameCell = document.createElement('td');
		nameCell.id = idPrefix + '-' + this.dataPointIndex + '-name';
		nameCell.className = 'full-text-field-label';
		nameCell.appendChild(document.createTextNode(dataPoint.name + ' (' + dataPointIndex + ')'));
		this.container.appendChild(nameCell);
	}
	
	var viewCell = document.createElement('td');
	var view = document.createElement('div');
	view.className = 'point-container';
	this.container.appendChild(viewCell);
	viewCell.appendChild(view);
	this.view = view;
}

jdnp3.ui.DataPointItem.prototype.getComponent = function() {
	return this.container;
}

jdnp3.ui.DataPointItem.prototype.appendItem = function(component) {
	this.view.appendChild(component);
}

jdnp3.ui.DataPointItem.prototype.appendInputText = function(attribute, title, onclick) {
	var id = this.idPrefix + '-' + this.dataPointIndex + '-' + attribute;
	var dataPointList = this.dataPointList;
	var index = this.dataPointIndex;
	var idPrefix = this.idPrefix;
	
	var valueView = document.createElement('div');
	valueView.setAttribute('style', 'border-collapse: separate;');
	valueView.title = title;
	
	var value = document.createElement('div');
	value.className = 'full-text-field-value';
	value.id = id;

	value.onclick = function() {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var container = document.createElement('div');
			var infoDiv = document.createElement('div');
			infoDiv.setAttribute('style', 'width: 300px;');
			var infoSpan = document.createElement('span');
			infoSpan.innerHTML = 'Enter a new value:';
			infoSpan.setAttribute('style', 'font-size: 1.2em;');
			infoDiv.appendChild(infoSpan);
			container.appendChild(infoDiv);
			
			var textField = document.createElement('div');
			textField.className = 'text-field';
			textField.setAttribute('style', 'width: 100%; min-width: 250px');
			container.appendChild(textField);
			textField.title = 'Supports Infinity, -Infinity, MAX, MIN and NaN';
			
			var textFieldValue = document.createElement('input');
			textFieldValue.id = idPrefix + '-' + index + '-newvalue';
			textFieldValue.value = dataPointList.get(index).value;
			textFieldValue.className = 'text-field-value';
			textFieldValue.setAttribute('style', 'border: none');
			textFieldValue.type = 'text';
			textFieldValue.select();
			textField.appendChild(textFieldValue);
			
			textFieldValue.onkeydown = function(event) {
				jdnp3.schedule.getDefaultScheduler().addTask(function() {
					if (event.key == 'Enter') {
						var value = document.getElementById(idPrefix + '-' + index + '-newvalue').value;
						if (value == 'MAX') {
							value = Number.MAX_VALUE;
						} else if (value == 'MIN') {
							value = -1*Number.MAX_VALUE;
						}
						device.requestChangeAttributeValue(dataPointList.get(index), 'value', value);
						jdnp3.ui.destroyDialog();
					} else if (event.key == 'Escape') {
						jdnp3.ui.destroyDialog();
					};
				}, 0);
			}
			
			jdnp3.ui.createDialog('Analog Input ' + index + ' - ' + dataPointList.get(index).name, container, function() {});
			jdnp3.schedule.getDefaultScheduler().addTask(function() {
				textFieldValue.select();
			}, 0);
		}, 0);
	}
	value.appendChild(document.createTextNode('0.0'));
	valueView.appendChild(value);
	this.view.appendChild(valueView);
}

jdnp3.ui.DataPointItem.prototype.appendSlideSwitch = function(attribute, title, onclick) {
	var id = this.idPrefix + '-' + this.dataPointIndex + '-' + attribute;
	this.view.appendChild(jdnp3.ui.createSlideSwitch(id, title, id, onclick));
}

jdnp3.ui.DataPointItem.prototype.appendDipSwitch = function(attribute, title, abbreviation, onclick) {
	var id = this.idPrefix + '-' + this.dataPointIndex + '-' + attribute;
	this.view.appendChild(jdnp3.ui.createDipSwitch(title, abbreviation, id, onclick));
}

jdnp3.ui.DataPointItem.prototype.appendDialogButton = function(menuItems, data) {
	var data = data || {};
	var id = this.idPrefix + '-' + this.dataPointIndex + '-showmenu';
	var dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block; height: 2.5em;');
	this.view.appendChild(dropDownButtonView);
	
	var dropDownButton = document.createElement('input');
	dropDownButton.id = id;
	dropDownButton.name = name;
	dropDownButton.setAttribute('type', 'button');
	dropDownButton.className = 'eventbutton-checkbox';
	dropDownButtonView.appendChild(dropDownButton);
	
	dropDownButtonLabel = document.createElement('label');
	dropDownButtonLabel.className = 'glossy-button';
	dropDownButtonLabel.setAttribute('for', id);
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonView.appendChild(dropDownButtonLabel);
	
	dropDownButton.onclick = function(event) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var menu = document.createElement('div');
			menu.className = 'drop-down-menu';
			
			for (var i = 0; i < menuItems.length; ++i) {
				var menuItem = menuItems[i];
				if (menuItem.separate) {
					var separator = document.createElement('hr');
					separator.setAttribute('style', 'color: #FFFFFF; margin-top: 0px; margin-bottom: 0px; margin-left: auto; margin-right: auto; width: 95%');
					menu.appendChild(separator);
				}
				if (menuItem.text) {
					var item = document.createElement('div');
					menu.appendChild(item);
					var itemSpan = document.createElement('span');
					item.className = 'drop-down-menu-button';
					item.appendChild(itemSpan);
					itemSpan.appendChild(document.createTextNode(menuItem.text));
					
					item.onclick = (function(menuItem) {
						return function(event) {
							jdnp3.schedule.getDefaultScheduler().addTask(function() {
								jdnp3.ui.destroyMenu();
								var callback = menuItem.callback || function() {};
								callback();
							}, 0);
						}
					}(menuItem));
				}
			}
		
			var parent = event.target.parentElement;
			jdnp3.ui.createMenu(parent, menu);
		}, 0);
	};
}

jdnp3.ui.createInputPanel = function(title, action) {
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		var container = document.createElement('div');
		var infoDiv = document.createElement('div');
		infoDiv.setAttribute('style', 'width: 300px;');
		var infoSpan = document.createElement('span');
		infoSpan.innerHTML = 'Enter a value:';
		infoSpan.setAttribute('style', 'font-size: 1.2px;');
		infoDiv.appendChild(infoSpan);
		container.appendChild(infoDiv);
		
		var textField = document.createElement('div');
		textField.className = 'text-field';
		textField.setAttribute('style', 'width: 100%; min-width: 250px');
		container.appendChild(textField);
		
		var idPrefix = 'special';
		var index = 0;
		
		var textFieldValue = document.createElement('input');
		textFieldValue.id = idPrefix + '-' + index + '-newvalue';
		textFieldValue.value = new Date().getTime();
		textFieldValue.className = 'text-field-value';
		textFieldValue.setAttribute('style', 'border: none');
		textFieldValue.type = 'text';
		textFieldValue.select();
		textField.appendChild(textFieldValue);
		
		textFieldValue.onkeypress = function(event) {
			jdnp3.schedule.getDefaultScheduler().addTask(function() {
				if (event.keyCode == 13) {
					action(textFieldValue.value);
					jdnp3.ui.destroyDialog();
				} else if (event.keyCode == 27) {
					jdnp3.ui.destroyDialog();
				};
			}, 0);
		}
		
		jdnp3.ui.createDialog(title, container, function() {});
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			textFieldValue.select();
		}, 0);
	}, 0);
}

jdnp3.ui.createDialogButton = function(id, menuItems, data) {
	var data = data || {};
	var dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block;');
	
	var dropDownButton = document.createElement('input');
	dropDownButton.id = id;
	dropDownButton.name = name;
	dropDownButton.setAttribute('type', 'button');
	dropDownButton.className = 'eventbutton-checkbox';
	dropDownButtonView.appendChild(dropDownButton);
	
	dropDownButtonLabel = document.createElement('label');
	dropDownButtonLabel.className = 'glossy-button';
	dropDownButtonLabel.setAttribute('style', 'min-width: 32px; width: 100%;');
	dropDownButtonLabel.setAttribute('for', id);
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonView.appendChild(dropDownButtonLabel);
	
	dropDownButton.onclick = function(event) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var menu = document.createElement('div');
			menu.className = 'drop-down-menu';
			
			for (var i = 0; i < menuItems.length; ++i) {
				var menuItem = menuItems[i];
				if (menuItem.separate) {
					var separator = document.createElement('hr');
					separator.setAttribute('style', 'color: #FFFFFF; margin-top: 0px; margin-bottom: 0px; margin-left: auto; margin-right: auto; width: 95%');
					menu.appendChild(separator);
				}
				if (menuItem.text) {
					var item = document.createElement('div');
					menu.appendChild(item);
					var itemSpan = document.createElement('span');
					item.className = 'drop-down-menu-button';
					item.appendChild(itemSpan);
					itemSpan.innerHTML = menuItem.text;
					
					item.onclick = (function(menuItem) {
						return function(event) {
							jdnp3.schedule.getDefaultScheduler().addTask(function() {
								jdnp3.ui.destroyMenu();
								var callback = menuItem.callback || function() {};
								callback();
							}, 0);
						}
					}(menuItem));
				}
			}
		
			var parent = event.target.parentElement;
			jdnp3.ui.createMenu(parent, menu);
		}, 0);
	};
	return dropDownButtonView;
}

jdnp3.ui.createDipSwitch = function(title, abbreviation, valueId, onclick) {
	var container = document.createElement('div');
	container.title = title;
	container.className = 'dipswitch';
	
	var input = document.createElement('input');
	input.id = valueId;
	input.className = 'dipswitch-checkbox';
	input.setAttribute('disabled', 'disabled');
	input.setAttribute('type', 'checkbox');
	input.setAttribute('name', valueId);
	container.appendChild(input);
	
	var label = document.createElement('label');
	label.className = 'dipswitch-label';
	label.setAttribute('for', valueId);
	label.onclick = onclick;
	
	var channel = document.createElement('span');
	channel.className = 'dipswitch-channel';
	label.appendChild(channel);
	var switchItem = document.createElement('span');
	switchItem.className = 'dipswitch-switch';
	label.appendChild(switchItem);
	var text = document.createElement('span');
	text.className = 'dipswitch-text';
	text.innerHTML = abbreviation;
	label.appendChild(text);
	
	container.appendChild(label);
	return container;
}

jdnp3.ui.createSlideSwitch = function(id, title, label, onclick, data) {
	data = data || {};
	
	var buttonView = document.createElement('div');
	buttonView.className = 'slideswitch';
	buttonView.title = title;
	
	var button = document.createElement('input');
	button.id = id;
	button.name = id;
	button.setAttribute('disabled', 'true');
	button.setAttribute('type', 'checkbox');
	button.className = 'slideswitch-checkbox';
	buttonView.appendChild(button);
	
	var buttonLabel = document.createElement('label');
	buttonLabel.className = 'slideswitch-label';
	buttonLabel.setAttribute('for', id);
	buttonLabel.onclick = onclick;
	var buttonLabelText = document.createElement('span');
	buttonLabelText.className = 'slideswitch-inner';
	buttonLabel.appendChild(buttonLabelText);
	buttonView.appendChild(buttonLabel);
	
	return buttonView;
}

jdnp3.ui.createCheckbox = function(id, title, label, onclick, data) {
	data = data || {};
	
	var buttonView = document.createElement('div');
	buttonView.setAttribute('style', 'display: inline-block;' + (data['style'] || ''));
	buttonView.title = title;
	
	var button = document.createElement('input');
	button.id = id;
	button.name = id;
	button.setAttribute('disabled', 'true');
	button.setAttribute('type', 'checkbox');
	button.className = 'eventbutton-checkbox';
	buttonView.appendChild(button);
	
	var buttonLabel = document.createElement('label');
	buttonLabel.className = 'glossy-button';
	buttonLabel.setAttribute('for', id);
	buttonLabel.onclick = onclick;
	var buttonLabelText = document.createElement('span');
	buttonLabelText.innerHTML = label;
	buttonLabel.appendChild(buttonLabelText);
	buttonView.appendChild(buttonLabel);
	
	return buttonView;
}

jdnp3.ui.createContextMenuButton = function(id, menuItems, data) {
	var data = data || {};
	
	var menuId = id + '-showmenu';
	var dropDownButtonView = document.createElement('div');
	dropDownButtonView.setAttribute('style', 'display: inline-block; font-size: 1em;');
	dropDownButtonView.setAttribute('tabindex', '1');
	
	var dropDownButton = document.createElement('input');
	dropDownButton.id = menuId;
	dropDownButton.name = name;
	dropDownButton.setAttribute('type', 'button');
	dropDownButton.className = 'eventbutton-checkbox';
	dropDownButtonView.appendChild(dropDownButton);
	
	dropDownButtonLabel = document.createElement('label');
	dropDownButtonLabel.className = 'glossy-button';
	dropDownButtonLabel.setAttribute('style', 'min-width: 32px; ' + (data.style || ''));
	dropDownButtonLabel.setAttribute('for', menuId);
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabelText.setAttribute('style', 'width: 16px; height: 8px; overflow: hidden; display: block; position: relative; left: 16px; margin-left: -8px; top: 50%; margin-top: -8px;');
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonLabelText = document.createElement('span');
	dropDownButtonLabelText.id = id;
	dropDownButtonLabelText.setAttribute('style', 'display: block; position: relative;');
	dropDownButtonLabelText.appendChild(document.createTextNode(''));
	dropDownButtonLabel.appendChild(dropDownButtonLabelText);
	dropDownButtonView.appendChild(dropDownButtonLabel);
	
	dropDownButton.onclick = function(event) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			var menu = document.createElement('div');
			menu.className = 'drop-down-menu';
			
			for (var i = 0; i < menuItems.length; ++i) {
				var menuItem = menuItems[i];
				if (menuItem.separate) {
					var separator = document.createElement('hr');
					separator.setAttribute('style', 'color: #FFFFFF; margin-top: 0px; margin-bottom: 0px; margin-left: auto; margin-right: auto; width: 95%');
					menu.appendChild(separator);
				}
				if (menuItem.text) {
					var item = document.createElement('div');
					menu.appendChild(item);
					var itemSpan = document.createElement('span');
					item.setAttribute('style', 'padding: 5px;')
					item.className = 'drop-down-menu-button';
					item.appendChild(itemSpan);
					itemSpan.innerHTML = menuItem.text;
					
					item.onclick = (function(menuItem) {
						return function(event) {
							jdnp3.schedule.getDefaultScheduler().addTask(function() {
								jdnp3.ui.destroyMenu();
								dropDownButtonView.focus();
								var callback = menuItem.callback || function() {};
								callback();
							}, 0);
						}
					}(menuItem));
				}
			}
		
			var parent = event.target.parentElement;
			jdnp3.ui.createMenu(parent, menu, {style: 'z-index: 4000; ' + (data.menuStyle || ''), orientation: (data.orientation || 'right')});
		}, 0);
	};
	return dropDownButtonView;
}