var Bookmarker = function(){
	this.element = jQuery("#bookmarker");
	this.template = _.template(this.element.find("#bookmarker_template").text());
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
	  console.log("IFRAME LOADED!");
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
	});
	
	iframe.src = "http://beta.euscreenxl.eu/myeuscreen.html?page=bookmarkembed&item=" + item.id;

	function setIframeHeight() {
		$iframe.height('auto');
		var newHeight = $('html', iframe.contentDocument).height();
		$iframe.height(newHeight);
	}
	
}