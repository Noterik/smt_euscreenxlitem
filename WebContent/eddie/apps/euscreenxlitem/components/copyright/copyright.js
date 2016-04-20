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
	if(data.copyRightOrg.indexOf("Rights Reserved") > -1 || data.copyRightOrg.indexOf("Free Access") > -1){
		data.copyRightLink = "http://www.europeana.eu/portal/rights/rr-f.html ";
	}else if (data.copyRightOrg.indexOf("Creative Commons") > -1){
		data.copyRightLink = "https://wiki.creativecommons.org/wiki/CC0";
	}else {
		data.copyRightLink = "#";
	}
	
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
	
	//Submit the form
	this.container.find('.btn-euscreen').on('click', function(event) {
		event.preventDefault();
		$('#mailresponse').parent().removeClass('form-error').removeClass('form-sent');
		$('#mailresponse').empty();
		var $form = $(this).parent();
		var data = {};
		$.each($form.serializeArray(), function(i, field) {
    		data[field.name] = field.value;
		});
		
		
		eddie.putLou("", "sendmail(" + JSON.stringify(data) + ")");
		return false;
	});
}
Copyright.prototype.showMailResponse = function(response){
	var self = this;
	var data = JSON.parse(response);
	if(data.status=='false') {
		$('#mailresponse').parent().addClass('form-error');
	} else {
		$('#mailresponse').parent().addClass('form-sent');
	}
	$('#mailresponse').html(data.message);
}