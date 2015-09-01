var jdnp3 = jdnp3 || {};
jdnp3.schedule = jdnp3.schedule || {};

jdnp3.schedule.PERIOD = 20;
jdnp3.schedule.DEFAULT_DELAY = 300;

jdnp3.schedule.Task = function(task, ticks, recurring) {
	this.task = task;
	this.ticks = ticks;
	this.recurring = recurring;
	this.remainingTicks = ticks;
}

jdnp3.schedule.Task.prototype.tick = function() {
	--this.remainingTicks;
	return this.remainingTicks;
}

jdnp3.schedule.Task.prototype.getTask = function() {
	return this.task;
}

jdnp3.schedule.Task.prototype.isRecurring = function() {
	return this.recurring;
}

jdnp3.schedule.Task.prototype.reset = function() {
	this.remainingTicks = this.ticks;
}

jdnp3.schedule.Scheduler = function() {
	this.taskQueue = [];
	this.intervalId = 0;
};

jdnp3.schedule.Scheduler.prototype.addTask = function(task, delay, recurring) {
	delay = (delay >= 0) ? delay : jdnp3.schedule.DEFAULT_DELAY;
	var ticks = delay / jdnp3.schedule.PERIOD;
	this.taskQueue.push(new jdnp3.schedule.Task(task, ticks, recurring || false));
}

jdnp3.schedule.Scheduler.prototype.start = function() {
	if (!this.intervaId) {
		var target = this;
		intervalId = setInterval(function() {target.tick();}, jdnp3.schedule.PERIOD);
	}
}

jdnp3.schedule.Scheduler.prototype.stop = function() {
	if (this.intervaId) {
		clearInterval(this.intervalId);
	}
}

jdnp3.schedule.Scheduler.prototype.tick = function() {
	var newTaskList = [];
	for (var task of this.taskQueue) {
		if (task.tick() <= 0) {
			task.getTask()();
			if (task.isRecurring()) {
				task.reset();
				newTaskList.push(task);
			}
		} else {
			newTaskList.push(task);
		}
	}
	this.taskQueue = newTaskList;
}

jdnp3.schedule.defaultScheduler = new jdnp3.schedule.Scheduler();
jdnp3.schedule.defaultScheduler.start();

jdnp3.schedule.getDefaultScheduler = function() {
	return jdnp3.schedule.defaultScheduler;
}
