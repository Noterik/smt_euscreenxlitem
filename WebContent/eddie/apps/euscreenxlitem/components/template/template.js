var Template = function () {
	console.log("Template()");
	var self = this;
	this.device = "desktop";
    Page.apply(this, arguments);
    
    this.overlayButtons = jQuery('button[data-overlay]');
    this.overlayContents = jQuery('.overlaycontent');
    
    var overlayButtons = this.overlayButtons;
	var overlayContents = this.overlayContents;
	
	var self = this;
	
	overlayButtons.each(function(){
		var $this = jQuery(this);
        var content = $this.attr("data-overlay");
        $this.click(function(e){
            e.preventDefault();
            var element = this;
            
            if($(content).is(":visible")) { 
                $(content).hide(); $(element).removeClass('active');
            } else { 
            	jQuery(".overlaycontent").hide();
                $(content).show(); $(element).addClass('active');
                overlayButtons.not().removeClass('active');
                overlayContents.not($(content)).hide();
                
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
