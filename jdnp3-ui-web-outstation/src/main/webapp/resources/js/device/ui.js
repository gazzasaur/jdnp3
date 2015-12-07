var jdnp3 = jdnp3 || {};
jdnp3.ui = jdnp3.ui || {};

jdnp3.ui.creatDipSwitch = function(title, abbreviation, valueId) {
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
