var Redirector = function(){
	this.element = jQuery("#redirector");
};
Redirector.prototype = Object.create(Component.prototype);
Redirector.prototype.jumpGoogle = function(message){
	//window.location.replace("http://mint-projects.image.ntua.gr/euscreenxl/ItemHtml?id="+message);
	this.element.text("Redirecting...");
	window.location.href ="http://mint-projects.image.ntua.gr/euscreenxl/ItemHtml?id="+message;
};