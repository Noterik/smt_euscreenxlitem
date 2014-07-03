var Template = function () {
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
                $(content).show(); $(self).addClass('active');
                overlayButtons.not(self).removeClass('active');
                overlayContents.not($(content)).hide();
            }
        });
	})
};

Template.prototype = Object.create(Page.prototype);
Template.prototype.activateTooltips = function(){	
	this.overlayButtons.tooltip();
};
