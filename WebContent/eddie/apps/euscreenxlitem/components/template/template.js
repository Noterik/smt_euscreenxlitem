var Template = function () {
	console.log("Template()");
	var self = this;
	this.device = "desktop";
    Page.apply(this, arguments);
    
    this.overlayButtons = jQuery('button[data-overlay]');
    this.overlayContents = jQuery('.overlaycontent');
    this.activeOverlay = null;
    
    var overlayButtons = this.overlayButtons;
	var overlayContents = this.overlayContents;
	
	var self = this;
	
	$(window).click(function(e){
		if(self.activeOverlay){
			if(e.target !== self.activeOverlay[0] && !$.contains(self.activeOverlay[0], e.target) && !$(e.target).attr('data-overlay')){
				self.activeOverlay.fadeOut('fast');
			}
		}
	});
	
	overlayButtons.each(function(){
		var $this = jQuery(this);
        
        $this.click(function(e){
            e.preventDefault();
            e.stopPropagation();
            var element = $(this);
            
            var content = $this.attr("data-overlay");
            var $content = jQuery(content);
                                    
            if(jQuery(content).is(":visible")){
            	$content.fadeOut('fast');
            	self.activeOverlay = null;
            	if(self.device == "ipad"){
            	
	            	overlayButtons.each(function(){
	            		var el = this;
	        		    var par = el.parentNode;
	        		    var next = el.nextSibling;
	        		    par.removeChild(el);
	        		    setTimeout(function() {par.insertBefore(el, next);}, 0)
	            	});
            	}
            	
            }else{
            	jQuery(".overlaycontent").hide();
                $content.fadeIn('fast'); 
                self.activeOverlay = $content;
                overlayContents.not($content).hide();
                
                if($(this).data('title') == "SHARE" && self.device == "desktop"){
            		jQuery(".permalink input").focus();
            		jQuery(".permalink input").select();
                }
            }
            
        });
	});
	
};

Template.prototype = Object.create(Page.prototype);
Template.prototype.setDevice = function(data){
	var data = JSON.parse(data);
	
	this.device = data.device;
};
Template.prototype.activateTooltips = function(){	
	console.log("ACTIVATE TOOLTIPS!!!");
	this.overlayButtons.tooltip();
};
Template.prototype.hideBookmarking = function(){
	console.log("Template.prototype.hideBookmarking()");
	jQuery("li.bookmark").hide();
};
Template.prototype.hideOverlay = function(){
	this.activeOverlay.fadeOut('fast');
}
