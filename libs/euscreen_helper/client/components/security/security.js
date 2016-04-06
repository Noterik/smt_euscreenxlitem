var Security = function(){
	
	this.on('domain-changed', function(domain){
		document.domain = domain;
	});
	
};
Security.prototype = Object.create(Component.prototype);