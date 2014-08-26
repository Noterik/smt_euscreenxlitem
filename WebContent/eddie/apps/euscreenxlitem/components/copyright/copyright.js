var Copyright = function(options){
	Component.apply(this, arguments);
	
	this.element = jQuery('#copyright');
	this.container = this.element.find('.contents');
	this.template = _.template(this.element.find('#copyright-template').text());
}
Copyright.prototype = Object.create(Component.prototype);
Copyright.prototype.setText = function(message){
	var data = JSON.parse(message);
	this.container.html(this.template(data));
}