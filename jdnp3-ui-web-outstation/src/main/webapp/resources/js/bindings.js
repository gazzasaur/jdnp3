var jdnp3 = jdnp3 || {};
jdnp3.bindings = jdnp3.bindings || {};

jdnp3.bindings.dataLinkBindings = [];

jdnp3.bindings.SetBindingsMessageHandler = function() {
}

jdnp3.bindings.SetBindingsMessageHandler.prototype.processMessage = function(bindings) {
	jdnp3.bindings.dataLinkBindings = [];
	bindings.forEach(function(item) {
		jdnp3.bindings.dataLinkBindings.add(item);
	});
}

jdnp3.bindings.createBindingsView = function(bindings) {
}
