var Bookmarker = function(){
	this.element = jQuery("#bookmarker");
	this.template = _.template(this.element.find("#bookmarker_template").text());
	this.iframeListener = null;
};
Bookmarker.prototype = Object.create(Component.prototype);
Bookmarker.prototype.setItem = function(message){
	console.log("Bookmarker.prototype.setItem(" + message + ")");
	var data = JSON.parse(message);
	
	var item = {
		id: data.itemId
	}
	
	var embed = {
		width: this.element.parent().width(),
		height: 150
	}
	
	this.element.append(this.template({item: item, embed: embed}));
	
	var $iframe = this.element.find("iframe");
	var iframe = $iframe[0];
	
	var MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

	iframe.addEventListener('load', function() {
	  setIframeHeight();

	  var target = iframe.contentDocument.body;

	  var observer = new MutationObserver(function(mutations) {
	    setIframeHeight();
	  });

	  var config = {
	    attributes: true,
	    childList: true,
	    characterData: true,
	    subtree: true
	  };
	  observer.observe(target, config);
	  
	  if(this.iframeListener){
		  jQuery(window).off('message', this.iframeListener);
	  }
	  
	  this.iframeListener = function(event){
		  var iFrameMessage = event.originalEvent.data;
		  
		  if(iFrameMessage['from'] && iFrameMessage['from'] === "embedbookmarker"){
			  var message = iFrameMessage.message;
			  
			  if(message['status'] === "ADDED_TO_COLLECTION"){
				  if(eddie.getComponent('template') != null && eddie.getComponent('template').hideOverlay){
					  eddie.getComponent('template').hideOverlay();
			  	  }
				  
				  if(iFrameMessage['type'] && iFrameMessage['type'] === 'notification'){
					  if(eddie.getComponent('alert') != null){
						  eddie.getComponent('alert').setAlert({
							  type: 'success',
							  message: message.message
						  });
					  }
				  }
			  }
			  
			  
			  
		  }
		  /*
		  if(message['data'] && message['data']['message'] && message['data']['message']['status']){
			  var status = message['message']['status'];
			  console.log("STATUS: " + status);
			  switch(status.status){
			  	case "ADDED_TO_COLLECTION":
			  		if(eddie.getComponent('template') != null && eddie.getComponent('template').hideOverlay){
			  			eddie.getComponent('template').hideOverlay();
			  		}
			  		break;
			  }
		  }
		  */
	  }
	  jQuery(window).on('message', this.iframeListener);
	});
	
	var url = eddie.getComponent('urltransformer').getURL('myeuscreen', {'page': 'bookmarkembed', 'item': item.id});
	console.log("URL: " + url);
	iframe.src = url;

	function setIframeHeight() {
		$iframe.height('auto');
		var newHeight = $('html', iframe.contentDocument).height();
		$iframe.height(newHeight);
	}
	
};