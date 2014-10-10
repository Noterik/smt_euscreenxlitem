var Template = function () {
	console.log("Template()");
    Page.apply(this, arguments);
    
    this.overlayButtons = jQuery('button[data-overlay]');
    this.overlayContents = jQuery('.overlaycontent');
    
    var overlayButtons = this.overlayButtons;
	var overlayContents = this.overlayContents;
	
	overlayButtons.each(function(){
		var $this = jQuery(this);
        var content = $this.attr("data-overlay");
        $this.click(function(e){
            e.preventDefault();
            self = this;
            
            if($(content).is(":visible")) { 
                $(content).hide(); $(self).removeClass('active');
            } else { 
            	jQuery(".overlaycontent").hide();
                $(content).show(); $(self).addClass('active');
                overlayButtons.not(self).removeClass('active');
                overlayContents.not($(content)).hide();
                
                if($(this).data('title') == "SHARE"){
                	jQuery(".permalink input").focus();
                	jQuery(".permalink input").select();
                }
            }
            
        });
	});
	
};

Template.prototype = Object.create(Page.prototype);
Template.prototype.activateTooltips = function(){	
	console.log("ACTIVATE TOOLTIPS!!!");
	this.overlayButtons.tooltip();
};
