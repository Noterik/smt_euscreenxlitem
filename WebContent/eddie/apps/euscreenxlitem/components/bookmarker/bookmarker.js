var Bookmarker = function(){
	var self = this;
	
	this.element = jQuery("#bookmarker");
	this.template = _.template(this.element.find("#bookmarker_collections_template").text());
	this.collapsibleFormWrapper = this.element.find('#new-collection-form');
	this.collectionsTarget = this.element.find(".collections");
	this.collectionSelector = this.element.find('.collection-selector');
	this.newCollectionButton = this.element.find('.folder-action');
	this.collectionForm = this.element.find('.new-collection-form');
	this.collectionWarning = this.collectionForm.find('.warning');
	this.collectionFormSubmitButton = this.collectionForm.find('input[type="submit"]');
	this.cancelButton = this.element.find('#new-collection-form .cancel');
	this.selectCollectionForm = this.element.find('.select-collection-form');
	this.successNotification = this.element.find('#collection-success-message');
	this.completedButton = this.element.find('#collection-success-message input');
	this.selectCollectionFormSubmitButton = this.selectCollectionForm.find('input[type="submit"]');

	this.collectionSelector.collapse();
	this.successNotification.collapse({
		toggle: false
	});
	this.collapsibleFormWrapper.collapse({
		toggle: false
	});
	
	this.newCollectionButton.on('click', function(){
		self.collectionSelector.collapse('hide');
	});
	
	this.cancelButton.on('click', function(){
		self.collapsibleFormWrapper.collapse('hide');
		self.collectionSelector.collapse('show');
	});
	
	this.collectionForm.on('submit', function(event){
		event.preventDefault();
		
		var message = {
			collectionName: self.collectionForm.find('#collection-name').val()	
		};
		
		self.collectionFormSubmitButton.addClass('button-loading');
		eddie.putLou('', 'createCollection(' + JSON.stringify(message) + ')');
		
		return false;
	});
	
	this.selectCollectionForm.on('submit', function(event){
		event.preventDefault();
		
		var val = self.collectionsTarget.val();
		
		if(val !== "null"){
			var message = {
				collectionPath: self.collectionsTarget.val()
			};
			self.selectCollectionFormSubmitButton.addClass('button-loading');
			eddie.putLou('', 'addToCollection(' + JSON.stringify(message) + ')');
		}else{
			jQuery("#bookmark-content").hide();
		}
		
		return false;
	});
	
	this.completedButton.on('click', function(){
		jQuery("#bookmark-content").hide();
		
		console.log("COMPLETED CLICKED!");
		self.successNotification.collapse('hide');
		self.collapsibleFormWrapper.collapse('hide');
		self.collectionSelector.collapse('show');
	});
	
	
};
Bookmarker.prototype = Object.create(Component.prototype);
Bookmarker.prototype.setCollections = function(data){
	var data = JSON.parse(data);
	
	this.collectionsTarget.html(this.template({collections: data}));
};
Bookmarker.prototype.showNewCollectionWarning = function(){
	this.collectionFormSubmitButton.removeClass('button-loading');
	this.collectionWarning.show();
};
Bookmarker.prototype.success = function(){
	this.collectionFormSubmitButton.removeClass('button-loading');
	this.collapsibleFormWrapper.collapse('hide');
	this.collectionSelector.collapse('show');
};
Bookmarker.prototype.nodeAddedToCollection = function(message){
	var data = JSON.parse(message);
	this.selectCollectionFormSubmitButton.removeClass('button-loading');
	this.collapsibleFormWrapper.collapse('hide')
	this.collectionSelector.collapse('hide');
	this.successNotification.collapse('show');
};