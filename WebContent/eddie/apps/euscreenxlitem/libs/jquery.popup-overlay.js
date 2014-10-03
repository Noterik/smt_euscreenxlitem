/*
    popupOverlayJS
*/
(function($) {
    $.fn.popupOverlayJS = function(customSettings) {
        var settings = $.extend({
            $overlayContents:  '.contentOverlay',
            contentOverlayIdAttr: 'data-overlay'             
        }, customSettings || {});
        
        // var
        var overlayButton = this;
        overlayButton.each(function () {
            var $this = jQuery(this);
            var content = $this.attr(settings.contentOverlayIdAttr);
            $this.click(function(e){
                e.preventDefault();
                self = this;
                if($(content).is(":visible")) { 
                    $(content).hide(); $(self).removeClass('active');
                } else { 
                    $(content).show(); $(self).addClass('active');
                    overlayButton.not(self).removeClass('active');
                    settings.$overlayContents.not($(content)).hide();
                }
            });
        });
    };
})(jQuery);