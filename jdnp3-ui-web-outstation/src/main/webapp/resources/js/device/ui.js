var jdnp3 = jdnp3 || {};
jdnp3.ui = jdnp3.ui || {};

jdnp3.ui.dialog = {};
jdnp3.ui.dialog['refreshCallback'] = null;

jdnp3.ui.createDialog = function(title, component, refreshCallback) {
	var mainDialogTitle = document.getElementById('main-dialog-title');
	mainDialogTitle.innerHTML = title;
	jdnp3.ui.dialog['refreshCallback'] = refreshCallback;
	
	var mainDialog = document.getElementById('main-dialog');
	mainDialog.innerHTML = '';
	mainDialog.appendChild(component);
	PF('main-dialog').show();
}

jdnp3.ui.destroyDialog = function(component, refreshCallback) {
	var mainDialog = document.getElementById('main-dialog');
	dnp3.ui.dialog.refreshCallback = null;
	mainDialog.innerHTML = '';
	PF('main-dialog').hide();
}

jdnp3.ui.refreshDialog = function() {
	if (jdnp3.ui.dialog.refreshCallback != null) {
		jdnp3.ui.dialog.refreshCallback();
	}
}

jdnp3.ui.createMenu = function(target, component) {
	var mainMenu = document.getElementById('main-menu');
	mainMenu.innerHTML = '';
	mainMenu.appendChild(component);
	
	var top = target.getBoundingClientRect().top;
	var left = target.getBoundingClientRect().right;
	mainMenu.setAttribute('style', 'overflow: hidden; border: 1px solid black; border-radius: 5px; display: block; position: fixed; top: ' + top + 'px; left: ' + left + 'px;');
}

jdnp3.ui.destroyMenu = function() {
	var mainMenu = document.getElementById('main-menu');
	mainMenu.innerHTML = '';
	mainMenu.setAttribute('style', 'display: none;');
}

jdnp3.ui.creatDipSwitch = function(title, abbreviation, valueId, onclick) {
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

jdnp3.ui.createButton = function(id, title, label, data) {
	return jdnp3.ui.createInput(id, title, label, 'button', data);
}

jdnp3.ui.createCheckbox = function(id, title, label, onclick, data) {
	return jdnp3.ui.createInput(id, title, label, 'checkbox', onclick, data);
}

jdnp3.ui.createInput = function(id, title, label, type, onclick, data) {
	data = data || {};
	
	var buttonView = document.createElement('div');
	buttonView.setAttribute('style', 'display: inline-block;' + (data['style'] || ''));
	buttonView.title = title;
	
	var button = document.createElement('input');
	button.id = id;
	button.name = id;
	button.setAttribute('disabled', 'true');
	button.setAttribute('type', type || 'button');
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
