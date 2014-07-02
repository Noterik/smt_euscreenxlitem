var Template = function () {
    Page.apply(this, arguments);
    
    this.overlayButtons = jQuery('button[data-overlay]');
    
    this.overlayButtons.tooltip();
};

Template.prototype = Object.create(Page.prototype);
