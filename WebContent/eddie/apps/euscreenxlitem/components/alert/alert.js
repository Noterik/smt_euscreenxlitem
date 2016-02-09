var Alert = function(options){
	Component.apply(this, arguments);
	this.element = jQuery('#alert');
	this.template = _.template(this.element.find('#alertTemplate').text());
	this.alertContainer = this.element.find('.alert-container');
	this.alertContainer.css('left', this.element.offset().left);
	this.alertContainer.css('width', this.element.outerWidth());
}
Alert.prototype = Object.create(Component.prototype);
Alert.prototype.setAlert = function(alert){
	console.log("Alert.setAlert(" , alert , ")");
	var alertDefaults = {
		type: 'success',
		message : '',
		duration: 10000
	};
	alert = jQuery.extend(alertDefaults, alert);
	var alertElement = jQuery(this.template({alert: alert}));
	this.alertContainer.append(alertElement);
	
	if(alert.duration){
		setTimeout(function(){
			if(alertElement.is(':visible')){
				alertElement.fadeOut('quick', function(){
					alertElement.remove();
				});
			}else{
				alertElement.remove();
			}
		}, alert.duration);
	}
}