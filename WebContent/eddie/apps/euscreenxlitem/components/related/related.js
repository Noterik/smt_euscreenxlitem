var Related = function(options){
	console.log("Related()2");
	Component.apply(this, arguments);
	var self = this;
	
	this.element = jQuery("#related");
	this.moreButton = this.element.find('.more-button');
	this.itemsElement = this.element.find('.items');
	this.template = _.template(this.element.find('#related-items-template').text());
	
	this.moreButton.on('click', function(){
		event.preventDefault();
		self.loadMoreRelated();
	});
};

Related.prototype = Object.create(Component.prototype);
Related.prototype.items = [];
Related.prototype.atItem = 0;
Related.prototype.setRelated = function(data){
	console.log("Related.setRelated()");
	this.items = JSON.parse(data).filter(function(item){
		return item.screenshot;
	});
		
	var itemsToRender = this.items.slice(this.atItem, this.atItem + 5);
	console.log(itemsToRender);
	this.itemsElement.html(this.template({items: itemsToRender}));
};
Related.prototype.loadMoreRelated = function(){
	this.atItem += 5;
	var itemsToRender = this.items.slice(this.atItem, this.atItem + 5);
	this.itemsElement.append(this.template({items: itemsToRender}));
};