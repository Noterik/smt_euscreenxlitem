var Social = function(options){
	Component.apply(this, arguments);
	
	this.element = jQuery('#social');
		
	this.settings = {};
	
	// sharing buttons
    this.$twitterButton = jQuery('#button-twitter');
    this.$facebookButton = jQuery('#button-facebook');
    this.$copyButton = jQuery('.share-copy');
    
    this.urlChanged();
};
Social.prototype = Object.create(Component.prototype);
Social.prototype.urlChanged = function(){
    // social buttons
    jQuery(".permalink input").val(document.location);
    
    this.$copyButton.click(async function() {
	try {
	    jQuery(".permalink input").focus();
	    jQuery(".permalink input").select();
	    await copy(document.location);
	}
	catch(e) {
	    console.error(e);
	}
    });
};
Social.prototype.setDevice = function(data){
	data = JSON.parse(data);
	if(data.device != "desktop"){
		jQuery(".permalink input").removeAttr('readonly');
	}
};
Social.prototype.setSharingSettings = function(message){
    console.log("Social.setSharingSettings(" + message + ")");
    var data = JSON.parse(message);
	
    this.settings = data;
	
    this.$twitterButton.SocialSharing({ type : 'twitter', url : document.location, text : data.text });
    this.$facebookButton.SocialSharing({ type : 'facebook', url : document.location, text : data.text });
};
Social.prototype.setOldSite = function(message){
	window.location.href = 'https://euscreen.eu/';
}

function copy(text) {
    return new Promise((resolve, reject) => {
        if (typeof navigator !== "undefined" && typeof navigator.clipboard !== "undefined" && navigator.permissions !== "undefined") {
            const type = "text/plain";
            const blob = new Blob([text], { type });
            const data = [new ClipboardItem({ [type]: blob })];
            navigator.permissions.query({name: "clipboard-write"}).then((permission) => {
                if (permission.state === "granted" || permission.state === "prompt") {
                    navigator.clipboard.write(data).then(resolve, reject).catch(reject);
                }
                else {
                    reject(new Error("Permission not granted!"));
                }
            });
        }
        else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
            var textarea = document.createElement("textarea");
            textarea.textContent = text;
            textarea.style.position = "fixed";
            textarea.style.width = '2em';
            textarea.style.height = '2em';
            textarea.style.padding = 0;
            textarea.style.border = 'none';
            textarea.style.outline = 'none';
            textarea.style.boxShadow = 'none';
            textarea.style.background = 'transparent';
            document.body.appendChild(textarea);
            textarea.focus();
            textarea.select();
            try {
                document.execCommand("copy");
                document.body.removeChild(textarea);
                resolve();
            }
            catch (e) {
                document.body.removeChild(textarea);
                reject(e);
            }
        }
        else {
            reject(new Error("None of copying methods are supported by this browser!"));
        }
    });    
}