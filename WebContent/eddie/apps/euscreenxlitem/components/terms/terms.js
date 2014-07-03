var Terms = function(options){
	Component.apply(this, arguments);
	
	this.element = jQuery('#terms');
	this.container = this.element.find('.contents');
	this.template = _.template(this.element.find('#terms-template').text());
}
Terms.prototype = Object.create(Component.prototype);
Terms.prototype.setText = function(message){
	var data = JSON.parse(message);
	
	this.container.html(this.template({terms: data.terms}));
}