var jdnp3 = jdnp3 || {};
jdnp3.bindings = jdnp3.bindings || {};

jdnp3.bindings.dataLinkBindings = [];

jdnp3.bindings.SetBindingsMessageHandler = function() {
}

jdnp3.bindings.SetBindingsMessageHandler.prototype.processMessage = function(bindings) {
	var bindingsTable = document.getElementById('bindingsTable');
	bindingsTable.innerHTML = '';
	bindingsTable.setAttribute('style', 'display: inline-block; text-align: center;');

	jdnp3.bindings.dataLinkBindings = [];
	var index = 0;
	bindings.outstationBindings.forEach(function(item) {
		jdnp3.bindings.dataLinkBindings.push(item);
		var tableRow = document.createElement('tr');
		bindingsTable.appendChild(tableRow);
		
		var dataLinkNameCell = document.createElement('td');
 		dataLinkNameCell.className = 'full-text-field-label';
		dataLinkNameCell.appendChild(document.createTextNode('Data Link Name:' + item.dataLinkName));
		tableRow.appendChild(dataLinkNameCell);
		
		var addressCell = document.createElement('td');
		addressCell.className = 'full-text-field-label';
		addressCell.appendChild(document.createTextNode('DNP3 Address:' + item.address));
		tableRow.appendChild(addressCell);

		var dropDownButtonView = document.createElement('div');
		dropDownButtonView.setAttribute('style', 'display: inline-block;');

		var id = 'bindings-' + index;
		var buttonCell = document.createElement('td');
		var dropDownButton = jdnp3.ui.createDialogButton(id, ([
			{text: 'Start', callback: function() {
				jdnp3.schedule.getDefaultScheduler().addTask(function() {
					jdnp3.ui.destroyMenu();
					device.start(bindings.site, bindings.device, item.dataLinkName);
				}, 0);
			}},
			{text: 'Stop', callback: function() {
				jdnp3.schedule.getDefaultScheduler().addTask(function() {
					jdnp3.ui.destroyMenu();
					device.stop(bindings.site, bindings.device, item.dataLinkName);
				}, 0);
			}}
		]));
		
		buttonCell.appendChild(dropDownButton);
		tableRow.appendChild(buttonCell);
		index = index + 1;
	});
}
