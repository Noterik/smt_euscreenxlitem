var Metadata = function(options){
	Component.apply(this, arguments);
	
	var self = this;
	this.element = jQuery('#metadata');
	this.template = _.template(this.element.find('#metadata-template').text());
	this.contents = this.element.find('.contents');
	this.showMoreButton = jQuery('#show-extra-filters');
	this.showLessButton = jQuery('#hide-extra-filters');
	
	this.showMoreButton.on('click', function(){
		self.showLessButton.show();
		self.showMoreButton.hide();
	});
	
	this.showLessButton.on('click', function(){
		self.showLessButton.hide();
		self.showMoreButton.show();
	});
	
};
Metadata.prototype = Object.create(Component.prototype);
Metadata.prototype.setData = function(message){
	var data = JSON.parse(message);
		
	var template = _.template(this.element.find('#' + data.type + '-template').text())
	this.contents.html(template({item: data}));
}

Metadata.prototype.setTitle = function(message) {
    var data = JSON.parse(message);
    
    jQuery('.item-title').text(data.title);
}