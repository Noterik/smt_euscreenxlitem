var Template = function () {
    Page.apply(this, arguments);
    
    this.itemTitleElement = jQuery(".navbar-header a.title");
    this.itemOriginalTitleElement = jQuery(".basic-info .original-title");
    this.itemProviderElement = jQuery(".basic-info .provider");
    this.itemProductionYearElement = jQuery(".basic-info .production-year");
    this.itemCountryProductionelement = jQuery(".basic-info .country-production");
    
};

Template.prototype = Object.create(Page.prototype);
Template.prototype.setData = function(data){
	var message = JSON.parse(data);
	
	console.log(message);
	
	for(var field in message){
		if(!message[field])
			message[field] = "-";
		
		jQuery('*[data-field="' + field + '"]').text(message[field]);
	}
}
