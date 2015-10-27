var Myeuscreenhistory = function(){
	Component.apply(this, arguments);
	var self = this;

};
Myeuscreenhistory.prototype = Object.create(Component.prototype);

Myeuscreenhistory.prototype.addItemToHistory = function(id) {
	console.log("----------------------------------------------");
	var cookie = document.cookie;
	var re = /smt_euscreenxl_user_[a-zA-Z0-9]+_/; 
	var rs;
 
	if ((rs = re.exec(cookie)) !== null) {
	    if (rs.index === re.lastIndex) {
	        re.lastIndex++;
	    }
	    // eg m[0] etc.
	}
	var splitedRegexResult = rs[0].split('_');

	var user = splitedRegexResult[3];
	
	var historyApiUrl = location.protocol + "//" + location.host +  "/" + "myeuscreen.html?action=addHistory&user=" + user + "&id=" + id;
	console.log(historyApiUrl);

	$('<iframe>', {
   src: historyApiUrl,
   id:  'hiddenIframeCall',
   frameborder: 0,
   scrolling: 'no'
   }).appendTo('#myeuscreenhistory'); 
}