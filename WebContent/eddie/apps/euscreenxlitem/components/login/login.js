function Login(options){
    var self = this;        

    this.element = jQuery("#login");
    var form = this.element.find('.login');
    var usernameField = form.find('#bookmark-user-email');
    var passwordField = form.find('#bookmark-user-password');
    this.warning = form.find('.warning');
    this.submitButton = form.find('input[type="submit"]');
    
    this.element.collapse();
      
    form.submit(function(e){
    	e.preventDefault();
    	e.stopPropagation();
    	
    	var username = usernameField.val();
    	var password = passwordField.val();
    	
    	self.submitButton.addClass('button-loading');
    	
    	eddie.putLou("login", "login(" + username + "," + password + ")");     
    	
    	return false;
    });

}
Login.prototype = Object.create(Component.prototype);
Login.prototype.ticket = function(){
	console.log("Login.ticket()");
	var args = arguments[0];
	var splitArgs = args.split(",");
	
	var success = splitArgs[0];
	
	if(success == "-1"){
		this.warning.text("Incorrect username or password");
		this.warning.show();
	}else{
		this.warning.hide();
	}
	this.submitButton.removeClass('button-loading');
};
Login.prototype.loginSuccess = function(){
	this.element.collapse('toggle');
	this.submitButton.removeClass('button-loading');
};