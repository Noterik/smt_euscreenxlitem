var Copyright = function(options){
	Component.apply(this, arguments);
	
	this.element = jQuery('#copyright');
	this.container = this.element.find('.contents');
	this.template = _.template(this.element.find('#terms-of-use-template').text());
}
Copyright.prototype = Object.create(Component.prototype);
Copyright.prototype.setText = function(message){
	var self = this;
	var data = JSON.parse(message);
	this.container.html(this.template({data: data}));
	
	this.container.find('.switch-action').on('click', function(event){
		event.preventDefault();
		console.log("CLICK CLICK!!");
		var termsOfUse = self.element.find('#terms-of-use');
		var contactProvider = self.element.find('#contact-provider');
		if(termsOfUse.is(':visible')){
			termsOfUse.hide();
			contactProvider.show();
		}else{
			termsOfUse.show();
			contactProvider.hide();
		}
	});
}