var ASCII8Decoding = {	
	/**
	 * This is the Daniel infamous encoding
	 * 
	 * @requires jQuery
	 * @requires swfobject		
	 */
	encode : function(input)
	{
		// Log
		
		var output = "";
		for (var i=0;i<input.length;i++) {
			var code = input.charCodeAt(i);
			if (code>127 && code<1000) {
				output+="\\"+code;
			} else if (code==13) {
				output+="\\013";
			} else {
				if (code==37) {
					output+="\\037";
				} else if (code==35) {
					output+="\\035";
				} else if (code==61) {
					output+="\\061";
				} else if (code==92) {
					output+="\\092";
				}  
				else {
					if (code>999) {
						var t = code.toString(16);
						if (t.length==2) {
							output+="\\0x00"+code.toString(16);
						} else if (t.length==3) {
							output+="\\0x0"+code.toString(16);
						} else {
							output+="\\0x"+code.toString(16);		
						}
					} else {
						output+=input.charAt(i);	
					}
				}
			}
		}
		return output;
	},
	decode : function(input)
	{
		// Log
		
		if (input == null) return "";
		if (input.indexOf("\\")!=-1) 
		{
			var pos = input.indexOf("\\");
			var output  = "";
			while (pos != -1) {
				// is it dec or hex encoding ?
				if (input.charAt(pos+2)=="x") {
					//trace("its hex its hex "+input.substr(pos+1,6));
					output += input.substr(0,pos);
					var code = parseInt(input.substr(pos+1,6));
					output += String.fromCharCode(code);
					//trace("its a "+String.fromCharCode(code));
					input = input.substring(pos+7);
				} else {
					output += input.substr(0,pos);
					code = parseInt(input.substr(pos+1,3));
					output += String.fromCharCode(code);
					input = input.substring(pos+4);
				}
				pos = input.indexOf("\\");
			}
			output += input;
			return output;
		} else {
			return input;
		}
		return null;
	}
}