var Social = function(options){
	Component.apply(this, arguments);
	
	this.element = jQuery('#social');
		
	this.settings = {};
	
	// sharing buttons
    this.$twitterButton = jQuery('#button-twitter');
    this.$facebookButton = jQuery('#button-facebook');
    this.$googleButton = jQuery('#button-google');
    
    this.urlChanged();
};
Social.prototype = Object.create(Component.prototype);
Social.prototype.urlChanged = function(){
	// social buttons
    jQuery(".permalink input").val(document.location);
};
Social.prototype.setDevice = function(data){
	data = JSON.parse(data);
	if(data.device != "desktop"){
		jQuery(".permalink input").removeAttr('readonly');
	}
};
Social.prototype.setSharingSettings = function(message){
	console.log("Social.setSharingSettings(" + message + ")");
	var data = JSON.parse(message);
	
	this.settings = data;
	
	this.$twitterButton.SocialSharing({ type : 'twitter', url : document.location, text : data.text });
    this.$facebookButton.SocialSharing({ type : 'facebook', url : document.location, text : data.text });
    this.$googleButton.SocialSharing({ type : 'google', url : document.location, text : data.text });
}