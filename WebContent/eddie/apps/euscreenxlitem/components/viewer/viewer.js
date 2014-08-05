var Viewer = function(options){
	console.log("Viewer()");
	Component.apply(this, arguments);
	
	this.element = jQuery("#viewer");
	this.container = this.element.find('.wrapper');
};

Viewer.prototype = Object.create(Component.prototype);
Viewer.prototype.setUri = function(uri){
	this.uri = uri;
	
	this.initializeViewer();
};
Viewer.prototype.setVideo = function(data){
	console.log("Viewer.prototype.setVideo(" + message + ")");
	var message = JSON.parse(data);
	var videos = message.video;
	var vidElement = jQuery('<video controls="controls"></video>');
	
	for(var i = 0; i < videos.length; i++){
		var video = videos[i];
		var sourceElement = jQuery('<source src="' + video + '" type="video/mp4"></source>');
		vidElement.append(sourceElement);
	}
	
	this.container.html(vidElement);
};
Viewer.prototype.setAudio = function(data){
	var message = JSON.parse(data);
	var audio = message.audio;
	var mime = message.mime;
	var audioElement = jQuery('<audio controls="controls">')
	var sourceElement = jQuery('<source src="' + audio + '" type="' + mime + '"></source>');
	audioElement.append(sourceElement);
	
	this.element.html(audioElement);
	this.element.addClass('non-visible');
};
Viewer.prototype.setPicture = function(data){
	var message = JSON.parse(data);
	var picture = message.picture;
	var alt = message.alt;	
	var picElement = jQuery('<a href="' + picture + '"><img src="' + picture + '" alt="' + alt +'"></a>');

	this.container.html(picElement);
};
Viewer.prototype.setDoc = function(data){
	console.log("SET DOC!!!!!");
	console.log(data);
	var message = JSON.parse(data);
	var doc = message.doc;
	
	var docElement = jQuery('<object data="' + encodeURI(doc) + '#view=FitH" type="application/pdf" width="100%" height="100%"><p>It appears you don\'t have a PDF plugin for this browser. You can <a href="' + encodeURI(doc) + '">click here to download the PDF file.</a></p></object>');
	
	this.element.html(docElement);
	this.element.addClass('non-visible');
};