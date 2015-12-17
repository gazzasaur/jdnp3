var jdnp3 = jdnp3 || {};
jdnp3.pointlist = jdnp3.pointlist || {};

jdnp3.pointlist.List = function(insertCallback, appendCallback, updateCallback, removeCallback, clearCallback) {
	this.points = [];
	this.insertCallback = insertCallback || function(index, dataPoint) {};
	this.appendCallback = appendCallback || function(index, dataPoint) {};
	this.updateCallback = updateCallback || function(index, dataPoint) {};
	this.removeCallback = removeCallback || function(index, dataPoint) {};
	this.clearCallback = clearCallback || function() {};
}

jdnp3.pointlist.List.prototype.contains = function(pointIndex) {
	return this.calculateIndex(pointIndex).exists;
}

jdnp3.pointlist.List.prototype.get = function(pointIndex) {
	var indexCalculation = this.calculateIndex(pointIndex);
	if (!indexCalculation.exists) {
		throw 'Index ' + pointIndex + ' is not in the list.';
	}
	return this.points[indexCalculation.index];
}

jdnp3.pointlist.List.prototype.add = function(dataPoint) {
	var pointIndex = dataPoint.index;
	var indexCalculation = this.calculateIndex(pointIndex);
	
	if (indexCalculation.exists) {
		this.points[indexCalculation.index] = dataPoint;
		this.updateCallback(indexCalculation.index, dataPoint);
	} else if (indexCalculation.index < this.points.length) {
		this.points.splice(indexCalculation.index, 0, dataPoint);
		this.insertCallback(indexCalculation.index, dataPoint);
	} else {
		this.points.push(dataPoint);
		this.appendCallback(indexCalculation.index, dataPoint);
	}
}

jdnp3.pointlist.List.prototype.remove = function(index) {
	
}

jdnp3.pointlist.List.prototype.calculateIndex = function(pointIndex) {
	var calculation = {
		'exists': false,
		'index': 0,
	};
	
	for (var i = 0; i < this.points.length; ++i) {
		calculation.index = i;
		if (this.points[i]['index'] == pointIndex) {
			calculation.exists = true;
			return calculation;
		} else if (this.points[i]['index'] > pointIndex) {
			return calculation;
		}
	}
	calculation.index = this.points.length;
	return calculation;
}