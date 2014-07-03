(function($) {
	$.fn.slidePanelJS = function(customSettings) {
		setTimeout(function(){
			console.log(jQuery(customSettings.openButton)[0]);
		}, 1000);
		
		console.log("SLIDE PANEL JS");
		var settings = $.extend({
            openButton: 	'#openButton' ,
            pageSection:	'#pageSection',
            navbarSection:	'#navbarSection',
            speed:		   	200                
        }, customSettings || {});
        
        // vars 
        settings.openButton = $(settings.openButton);
        settings.body = $('body');
        settings.html = $('html');
        settings.pageSection = $(settings.pageSection);
        settings.navbarSection = $(settings.navbarSection);
        var obj = this;
        
        // events
        settings.openButton.click(function(event){
            event.preventDefault();
        });
        
        // init
        var init = function() {
        	console.log("INIT!!!");
        	settings.openButton.click(function(){
	            if (obj.hasClass('open')) {
	                slideIn();
	            } else {
		            slideOut();
	            }
            });
        };
        
        // functions
        var slideOut = function() {
        	
        	// set style
        	obj.addClass("open");
        	settings.openButton.addClass("active");
        	settings.pageSection.addClass('slided');
        	
        	// animate
        	obj.animate({left:0}, settings.speed);
        	settings.pageSection.animate({left:obj.width()}, settings.speed);
        	settings.navbarSection.css({width:'100%'}).animate({left:obj.width()}, settings.speed);
        	
        	// hide scrolling
	        settings.body.css({'overflow-x':'hidden'});
	        settings.html.css({'overflow-x':'hidden'});
        };
        
        var slideIn = function() {
        
        	// set style
        	obj.removeClass("open");
        	settings.openButton.removeClass("active");
        	
        	// animate
        	obj.animate({left:'-'+obj.width()}, settings.speed);
        	settings.pageSection.animate({left:0}, settings.speed);
        	settings.navbarSection.animate({left:0}, settings.speed, function(){ settings.pageSection.removeClass('slided'); });
        	
        	// reset scrolling
	        settings.body.css({'overflow-x':'auto'});
	        settings.html.css({'overflow-x':'auto'});
        };
        
        // init
        init();
        
	};
})(jQuery);