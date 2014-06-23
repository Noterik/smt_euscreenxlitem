var Related = function(options){
	Component.apply(this, arguments);
	var self = this;
	
	this.element = jQuery("#related");
	this.items = this.element.find('.items');
	this.template = _.template(this.element.find('#related-items-template').text());
};

Related.prototype = Object.create(Component.prototype);
Related.prototype.setRelated = function(data){
	console.log("Related.prototype.setRelated(" + data + ")");
	var items = JSON.parse(data);
	
	this.items.html(this.template({items: items}));
	this.items.find('.media-item').on('click', function(){
		var id = $(this).data('id');
		
		window.location = "http://player7.noterik.com/lou/domain/euscreenxl/html5application/euscreenxlitem?id=" + id;
	});
};