var jdnp3 = jdnp3 || {};
jdnp3.message = jdnp3.message || {};

jdnp3.message.WebSocketWorker = function(messageReceiver) {
	this.messageReceiver = messageReceiver;
	this.connected = false;
	this.closed = false;
}

jdnp3.message.Messanger = function(url) {
	this.url = url;
	this.webSocket = null;
	this.messageReceiver = null;
}

jdnp3.message.Messanger.prototype.setMessageReceiver = function(messageReceiver) {
	this.messageReceiver = messageReceiver;
}

jdnp3.message.Messanger.prototype.connect = function() {
	if (this.webSocket != null) {
		return;
	}
	this.webSocket = new WebSocket(this.url);
	
	var messanger = this;
	this.webSocket.onopen = function() {
		console.log('Connection to ' + this.url + ' is open.')
	}
	
	this.webSocket.onmessage = function(message) {
		messanger.messageReceived(message)
	}
	
	this.webSocket.onclose = function() {
		messanger.webSocket = null;
		console.log('Connection to ' + this.url + ' is closed.')
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			messanger.connect();
		}, 1000);
	}
	
	this.webSocket.onerror = function() {
		console.log('Connection to ' + this.url + ' is closed due to an error.')
	}
}

jdnp3.message.Messanger.prototype.messageReceived = function(message) {
	var messageReceiver = this.messageReceiver;
	if (messageReceiver != null) {
		jdnp3.schedule.getDefaultScheduler().addTask(function() {
			messageReceiver.messageReceived(message);
		}, 0);
	}
}

jdnp3.message.Messanger.prototype.sendMessage = function(message) {
	var messanger = this;
	jdnp3.schedule.getDefaultScheduler().addTask(function() {
		messanger.webSocket.send(JSON.stringify(message));
	}, 0);
}
