/* 
* EuscreenxlpreviewApplication.java
* 
* Copyright (c) 2012 Noterik B.V.
* 
* This file is part of Lou, related to the Noterik Springfield project.
*
* Lou is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Lou is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Lou.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.lou.application.types;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.application.types.conditions.AndCondition;
import org.springfield.lou.application.types.conditions.EqualsCondition;
import org.springfield.lou.application.types.conditions.NotCondition;
import org.springfield.lou.application.types.conditions.OrCondition;
import org.springfield.lou.homer.LazyHomer;
import org.springfield.lou.myeuscreen.publications.Collection;
import org.springfield.lou.myeuscreen.rights.IRoleActor;
import org.springfield.lou.myeuscreen.rights.RoleActor;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.ftp.URIParser;


public class EuscreenxlitemApplication extends Html5Application{
	
	private boolean wantedna = true;
	private FSList providers;
	private HashMap<String, String> countriesForProviders;
	public String ipAddress="";
	public static boolean isAndroid;
	
	/*
	 * Constructor for the preview application for EUScreen providers
	 * so they can check and debug their uploaded collections.
	 */
	public EuscreenxlitemApplication(String id) {
		super(id); 
		//System.out.println("EuscreenxlitemApplication()");
		
		this.countriesForProviders = new HashMap<String, String>();
		
		// default scope is each screen is its own location, so no multiscreen effects
		setLocationScope("screen"); 
		
		//refer the header and footer elements from euscreenxl element application. 
		this.addReferid("header", "/euscreenxlelements/header");
		this.addReferid("footer", "/euscreenxlelements/footer");
		this.addReferid("mobilenav", "/euscreenxlelements/mobilenav");
		this.addReferid("linkinterceptor", "/euscreenxlelements/linkinterceptor");
		this.addReferid("warning", "/euscreenxlelements/warning");
		this.addReferid("videocopyright", "/euscreenxlelements/videocopyright");
		this.addReferid("viewer", "/euscreenxlelements/viewer");
		this.addReferid("ads", "/euscreenxlelements/ads");
		this.addReferid("analytics", "/euscreenxlelements/analytics");
		
		this.addReferidCSS("fontawesome", "/euscreenxlelements/fontawesome");
		this.addReferidCSS("bootstrap", "/euscreenxlelements/bootstrap");
		this.addReferidCSS("theme", "/euscreenxlelements/theme");
		this.addReferidCSS("genericadditions", "/euscreenxlelements/generic");
		this.addReferidCSS("all", "/euscreenxlelements/all");
		this.addReferidCSS("terms", "/euscreenxlelements/terms");
		
	}
	
	public void init(Screen s){
		//System.out.println("EuscreenxlitemApplication.init()");
		String id = s.getParameter("id");
		//System.out.println("ITEMID="+id);
		
		
		String uri = "/domain/euscreenxl/user/*/*";
		
		FSList fslist = FSListManager.get(uri);
		List<FsNode> nodes = fslist.getNodesFiltered(id.toLowerCase()); // find the item
		if (nodes!=null && nodes.size()>0) {
			FsNode n = (FsNode)nodes.get(0);
			
			n.getPath();
			
			s.setProperty("mediaNode", n);
			
			// daniel check for old euscreen id
			String pub = n.getProperty("public");
			if ((pub==null || !pub.equals("true")) && !this.inDevelMode()) {
				System.out.println("JUMP TO OLD SITE="+id);
				s.putMsg("social", "", "setOldSite(" + id + ")");
				return;
			}
			
			
			JSONObject socialSettings = new JSONObject();
			socialSettings.put("text", n.getProperty(FieldMappings.getSystemFieldName("originalTitle")));
			
			s.putMsg("social", "", "setSharingSettings(" + socialSettings + ")");
		}
		
		if(!this.inDevelMode()){
			s.putMsg("linkinterceptor", "", "interceptLinks()");
			s.putMsg("template", "", "hideBookmarking()");
		}
		
		//System.out.println("Euscreenxlitem.init2222()");
		if(s.getCapabilities() != null && s.getCapabilities().getDeviceModeName() == null){
			loadContent(s, "footer");
			s.putMsg("template", "", "activateTooltips()");
		}else{
			removeContent(s, "footer");
		}
		
		setRelated(s);
		setTerms(s);
		JSONObject bookmarkerMessage = new JSONObject();
		
		bookmarkerMessage.put("itemId", id);
		s.putMsg("bookmarker", "", "setItem(" + bookmarkerMessage + ")");
	}
	
	public String getFavicon() {
        return "/eddie/apps/euscreenxlelements/img/favicon.png";
    }
	
	public String getMetaHeaders(HttpServletRequest request) {
		System.out.println("Euscreenxlitem.getMetaHeaders()");
		String id =request.getParameter("id");
		//System.out.println("ITEMID="+id);
		
		ipAddress=getClientIpAddress(request);
		
		String uri = "/domain/euscreenxl/user/*/*";
		
		String browserType = request.getHeader("User-Agent");
		if(browserType.indexOf("Mobile") != -1) {
			String ua = request.getHeader("User-Agent").toLowerCase();
			isAndroid = ua.indexOf("android") > -1; //&& ua.indexOf("mobile");	
		}
		
		FSList fslist = FSListManager.get(uri);
		List<FsNode> nodes = fslist.getNodesFiltered(id.toLowerCase()); // find the item
		if (nodes!=null && nodes.size()>0) {
			FsNode n = (FsNode)nodes.get(0);
			
			n.getPath();
						
			// daniel check for old euscreen id
			String pub = n.getProperty("public");
			if (!((pub==null || !pub.equals("true")) && !this.inDevelMode())) {
				String metaString = "<link rel=\"alternate\" type=\"application/rdf+xml\" href=\"http://lod.euscreen.eu/data/" + id + ".rdf\">";
				metaString += "<meta property=\"og:title\" content=\"" + n.getProperty(FieldMappings.getSystemFieldName("title")) + "\" />";
				metaString += "<meta property=\"og:site_name\" content=\"EUscreenXL\" />";
				metaString += "<meta property=\"og:url\" content=\"http://euscreen.eu/item.html?id=" + id + "\" />";
				metaString += "<meta property=\"og:description\" content=\"" + n.getProperty(FieldMappings.getSystemFieldName("summaryEnglish")) + "\" />";
				metaString += "<meta property=\"og:image\" content=\"" + this.setEdnaMapping(n.getProperty(FieldMappings.getSystemFieldName("screenshot"))) + "\" />";
				return metaString;
			}
			
		}
		
		return ""; // default is empty;
	}
	
	private boolean inDevelMode() {
    	return LazyHomer.inDeveloperMode();
    }
	
	public void loadMoreRelated(Screen s){
		System.out.println("EuscreenxlitemApplication.loadMoreRelated()");
	}
	
	public void setDeviceMobile(Screen s){
		JSONObject params = new JSONObject();
		params.put("device", "mobile");
		s.putMsg("viewer", "", "setDevice(" + params + ")");
		s.putMsg("template", "", "setDevice(" + params + ")");
		s.putMsg("social", "", "setDevice(" + params + ")");
	}
	
	public void setDeviceIpad(Screen s){
		JSONObject params = new JSONObject();
		params.put("device", "ipad");
		s.putMsg("viewer", "", "setDevice(" + params + ")");
		s.putMsg("template", "", "setDevice(" + params + ")");
		s.putMsg("social", "", "setDevice(" + params + ")");
	}
	
	public void startViewer(Screen s){
		//System.out.println("EuscreenxlitemApplication.startViewer()");
		FsNode node = (FsNode) s.getProperty("mediaNode");
				
		String name = node.getName();
						
		if(name.equals("video")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawvideo/1");
			String[] videos = rawNode.getProperty("mount").split(",");
			JSONObject objectToSend = new JSONObject();
			JSONArray sourcesArray = new JSONArray();
			String extension = rawNode.getProperty("extension");
			objectToSend.put("screenshot",this.setEdnaMapping(node.getProperty(FieldMappings.getSystemFieldName("screenshot"))));
			objectToSend.put("aspectRatio", node.getProperty(FieldMappings.getSystemFieldName("aspectRatio")));
			objectToSend.put("sources", sourcesArray);
				
			for(int i = 0; i < videos.length; i++){
				JSONObject src = new JSONObject();
				String video = videos[i];
				
				if (video.indexOf("http://")==-1) {
					Random randomGenerator = new Random();
					Integer random= randomGenerator.nextInt(100000000);
					String ticket = Integer.toString(random);

					String videoFile= "/"+video+"/"+node.getPath()+ "/rawvideo/1/raw."+ extension;
					
					try{						
						//System.out.println("CallingSendTicket");						
						sendTicket(videoFile,ipAddress,ticket);}
					catch (Exception e){}
					
					video = "http://" + video + ".noterik.com/progressive/" + video + "/" + node.getPath() + "/rawvideo/1/raw."+ extension+"?ticket="+ticket;
				}
				
				String mime = "video/mp4";
				src.put("src", video);
				src.put("mime", mime);

				sourcesArray.add(src);
			}
			s.putMsg("viewer", "", "setVideo(" + objectToSend + ")");
		}else if(name.equals("audio")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawaudio/1");
			String audio = rawNode.getProperty("mount");
			String extension = rawNode.getProperty("extension");
			String mimeType = "audio/mpeg";
			if(!audio.startsWith("http://")) {
				audio = "http://" + audio + ".noterik.com" + node.getPath() + "/rawaudio/1/raw." + extension;
				if(extension.equalsIgnoreCase("wav")) {
					mimeType = "audio/wav";
				} else if(extension.equalsIgnoreCase("ogg")) {
					mimeType = "audio/ogg";
				}
			}
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("mime", mimeType);
			objectToSend.put("src", audio);
			s.putMsg("viewer", "", "setAudio(" + objectToSend + ")");
		}else if(name.equals("picture")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawpicture/1");
			String rawpicture = rawNode.getProperty("mount");
			String extension = rawNode.getProperty("extension");
			if(!rawpicture.startsWith("http://")) {
				rawpicture = "http://" + rawpicture + ".noterik.com" + node.getPath() + "/rawaudio/1/raw." + extension;
			} else {
				if(rawpicture.contains("/edna")) {
					rawpicture = rawpicture.replace("/edna", "");
				}
			}
			String picture = node.getProperty(FieldMappings.getSystemFieldName("screenshot"));
			if(picture==null) {
				picture=rawpicture;
			} else {
				if(picture.contains("/edna")) {
					picture = picture.replace("/edna", "");
				}
			}
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("src", picture);
			objectToSend.put("alt", node.getProperty(FieldMappings.getSystemFieldName("title")));
			s.putMsg("viewer", "", "setPicture(" + objectToSend + ")");
		}else if(name.equals("doc")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawdoc/1");
			String doc = rawNode.getProperty("mount");
			String extension = rawNode.getProperty("extension");
			if(!doc.startsWith("http://")) {
				doc = "http://" + doc + ".noterik.com" + node.getPath() + "/rawaudio/1/raw." + extension;
			}
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("src", doc);
			s.putMsg("viewer", "", "setDoc(" + objectToSend + ")");
		}
	}
	
	public void setMetadata(Screen s){
		//System.out.println("EuscreenxlitemApplication.setMetadata()");
		
		FsNode node = (FsNode) s.getProperty("mediaNode");
		if (node==null) return; // added by daniel.
		
		String path = node.getPath();
		String[] splits = path.split("/");
		String provider = splits[4];
		
		if(!this.countriesForProviders.containsKey(provider)){
			FsNode providerNode = Fs.getNode("/domain/euscreenxl/user/" + provider + "/account/default");
			try{
				String fullProviderString = providerNode.getProperty("birthdata");
				this.countriesForProviders.put(provider, fullProviderString);
			}catch(NullPointerException npe){
				this.countriesForProviders.put(provider, node.getProperty(FieldMappings.getSystemFieldName("provider")));
			}
		}
		
		JSONObject message = new JSONObject();
		
		HashMap<String, String> mappings = FieldMappings.getMappings();
		for(Iterator<String> i = mappings.keySet().iterator(); i.hasNext();){
			String readable = i.next();
			String systemName = mappings.get(readable);
			
			try{
				String value = node.getProperty(FieldMappings.getSystemFieldName(readable));
				if(value.trim().length() > 0){
					message.put(readable, node.getProperty(FieldMappings.getSystemFieldName(readable)));
				}else{
					message.put(readable, "-");
				}
			}catch(NullPointerException npe){
				message.put(readable, "-");
			}
		}
		
		message.put("type", node.getName());
		
		message.put("provider", this.countriesForProviders.get(provider));
		s.putMsg("metadata", "", "setData(" + message + ")");
	
	}
	
	public void onNewUser(Screen s,String name) {
		System.out.println("onNewUser: " + name);
		s.setProperty("username", name);
		super.onNewUser(s, name);
	}
	
	public void loginSuccess(Screen s){
		s.putMsg("login", "", "loginSuccess()");
	}
	
	public void createCollection(Screen s, String message){
		System.out.println("EuscreenxlitemApplication.createCollection(" + s.getId() + "," + message + ")");
		JSONObject json = (JSONObject) JSONValue.parse(message);
				
		String username = (String) s.getProperty("username");
		String newName = (String) json.get("collectionName");
		
		this.createPublicationsFolder(s);
		
		FsNode userNode = Fs.getNode("/domain/euscreenxl/user/" + username);
		String uri = userNode.getPath() + "/publications/1";
		FSList fslist = FSListManager.get(uri, false);
		List<FsNode> nodes = fslist.getNodes();
				
		for(Iterator<FsNode> i = nodes.iterator(); i.hasNext();){
			FsNode n = i.next();
			if(n.getProperty("name").equals(newName)){
				
				s.putMsg("bookmarker", "", "showNewCollectionWarning()");
				return;
			}
		}
		
		IRoleActor actor = new RoleActor(userNode);
		Collection col = Collection.createCollection(actor, username, newName);
		
		s.putMsg("bookmarker", "", "success()");
		s.setProperty("createdCollection", col.getId());
		this.loadCollections(s);
	}
	
	public void loadCollections(Screen s){
		System.out.println("Euscreenxlitem.loadCollections()");
		String username = (String) s.getProperty("username");
		String uri = "/domain/euscreenxl/user/" + username + "/publications/1/collection";
		FsNode mediaNode = (FsNode) s.getProperty("mediaNode");
		
		String createdCollection = (String) s.getProperty("createdCollection");
		
		System.out.println("loadCollections()");
		System.out.println("URI: " + uri);
		
		FSList fslist = FSListManager.get(uri, false);
		List<FsNode> nodes = fslist.getNodes();
		
		JSONArray collections = new JSONArray();
		
		for(Iterator<FsNode> i = nodes.iterator(); i.hasNext();){
			FsNode node = i.next();
			
			String childrenUri = node.getPath();
			FSList children = FSListManager.get(childrenUri, false);
			List<FsNode> childrenList = children.getNodes();
			
			JSONObject collection = new JSONObject();
			collection.put("id", node.getId());
			collection.put("path", node.getPath());
			collection.put("name", node.getProperty("name"));
			
			for(Iterator<FsNode> ci = childrenList.iterator(); ci.hasNext();){
				FsNode child = ci.next();
				System.out.println("ID: " + child.getId());
				if(child.getId().equals(mediaNode.getId())){
					collection.put("disabled", true);
					break;
				}
			}
			
			if(node.getId().equals(createdCollection)){
				collection.put("selected", true);
			}
			
			collections.add(collection);
		}
		
		s.putMsg("bookmarker", "", "setCollections(" + collections + ")");
	}
	
	public void addToCollection(Screen s, String message){
		System.out.println("EuscreenxlitemApplication.addToCollection(" + s.getId() + "," + message + ")");
		FsNode itemNode = (FsNode) s.getProperty("mediaNode");
		JSONObject data = (JSONObject) JSONValue.parse(message);
		
		String collectionPath = (String) data.get("collectionPath");
		FsNode collection = Fs.getNode(collectionPath);
		
		FsNode newNode = new FsNode(itemNode.getName(), itemNode.getId());
		newNode.setReferid(itemNode.getPath());
		
		System.out.println("NEW NODE XML");
		System.out.println(newNode.asXML());
		
		Fs.insertNode(newNode, collectionPath);
		JSONObject successMessage = new JSONObject();
		successMessage.put("collection", collection.getProperty("name"));
		
		s.putMsg("bookmarker", "", "nodeAddedToCollection(" + successMessage + ")");
		this.loadCollections(s);
	}
	
	private void createPublicationsFolder(Screen s){
		System.out.println("Euscreenxlitem.createPublicationsFolder()");
		String username = (String) s.getProperty("username");
		String userUri = "/domain/euscreenxl/user/" + username;
		String publicationsUri = userUri + "/publications/1";
	
		FsNode node = Fs.getNode(publicationsUri);
		if(node == null){
			FsNode newNode = new FsNode("publications", "1");
			Fs.insertNode(newNode, userUri);
		}
	}
	
	private void setTerms(Screen s){
		FsNode node = (FsNode) s.getProperty("mediaNode");
		String terms = node.getProperty(FieldMappings.getSystemFieldName("terms"));
		String provider_user = URIParser.getUserIdFromUri(node.getPath());
		JSONObject message = new JSONObject();
		message.put("copyright", terms);
		message.put("id", node.getId());
		message.put("provider", provider_user);
		String path = node.getPath();
		String[] splits = path.split("/");
		String provider = splits[4];
		message.put("providerReadable", this.countriesForProviders.get(provider));
		message.put("title", node.getProperty(FieldMappings.getSystemFieldName("title")));
		s.putMsg("copyright", "", "setText(" + message + ")");
	}
	
	private void setRelated(Screen s){
		//System.out.println("setReslated()");
		FsNode node = (FsNode) s.getProperty("mediaNode");
		String topic = node.getProperty(FieldMappings.getSystemFieldName("topic"));
		
		if(topic != null){
		
			String uri = "/domain/euscreenxl/user/*/*";
			FSList fslist = FSListManager.get(uri);
			List<FsNode> nodes = fslist.getNodes();
			
			if(!this.inDevelMode()){
				Filter filter = new Filter();
				filter.addCondition(new EqualsCondition("public", "true"));
				nodes = filter.apply(nodes);
			}
					
			AndCondition and = new AndCondition();
			OrCondition or = new OrCondition();
			NotCondition not = new NotCondition(new EqualsCondition("id", node.getId()));
			
			and.add(or);
			and.add(not);
			
			String[] topics = topic.split(",");
			for(int i = 0; i < topics.length; i++){
				topic = topics[i];
				
				or.add(new EqualsCondition(FieldMappings.getSystemFieldName("topic"), topic));
			}
			
			Filter topicFilter = new Filter();
			topicFilter.addCondition(and);
			
			JSONArray objectToSend = new JSONArray();
			
			List<FsNode> filteredNodes = topicFilter.apply(nodes);
			if(filteredNodes.size() > 10){
				nodes = filteredNodes;
			}else{
				nodes = nodes.subList(1, 20);
			}
			
			for(Iterator<FsNode> i = nodes.iterator(); i.hasNext();){
				FsNode retrievedNode = i.next();
				
				String path = retrievedNode.getPath();
				String[] splits = path.split("/");
				String provider = splits[4];
				
				if(!this.countriesForProviders.containsKey(provider)){
					FsNode providerNode = Fs.getNode("/domain/euscreenxl/user/" + provider + "/account/default");
					try{
						String fullProviderString = providerNode.getProperty("birthdata");
						this.countriesForProviders.put(provider, fullProviderString);
					}catch(NullPointerException npe){
						this.countriesForProviders.put(provider, retrievedNode.getProperty(FieldMappings.getSystemFieldName("provider")));
					}
				}
				
				JSONObject relatedItem = new JSONObject();
				
				relatedItem.put("id", retrievedNode.getId());
				relatedItem.put("title", retrievedNode.getProperty(FieldMappings.getSystemFieldName("title")));
				relatedItem.put("originalTitle", retrievedNode.getProperty(FieldMappings.getSystemFieldName("originalTitle")));
				relatedItem.put("provider", this.countriesForProviders.get(provider));
				relatedItem.put("country", retrievedNode.getProperty(FieldMappings.getSystemFieldName("country")));
				relatedItem.put("screenshot", setEdnaMapping(retrievedNode.getProperty(FieldMappings.getSystemFieldName("screenshot"))));
				relatedItem.put("year", retrievedNode.getProperty(FieldMappings.getSystemFieldName("year")));
				relatedItem.put("type", retrievedNode.getName());
				relatedItem.put("duration", retrievedNode.getProperty(FieldMappings.getSystemFieldName("duration")));
				relatedItem.put("language", retrievedNode.getProperty(FieldMappings.getSystemFieldName("language")));
				
				objectToSend.add(relatedItem);
			}
			
			s.putMsg("related", "", "setRelated(" + objectToSend + ")");
		}
	}
	
	public void sendContentProviderMail(Screen s, String data) {
		//System.out.println("Send mail to CP: " + data);
		
		JSONObject form = (JSONObject) JSONValue.parse(data);

		String mailfrom = "euscreen-portal@noterik.nl";
		String mailsubject = "Somebody showed interest in your item on EUScreen";
		String email = (String) form.get("email");
		String id = (String) form.get("identifier");
		String provider = (String) form.get("provider");
		String subject = (String) form.get("subject");
		String title = (String) form.get("title");
		String message = (String) form.get("message");
		message = message.replaceAll("(\r\n|\n)", "<br />");
		
		//Form validation
		JSONObject mailResponse = new JSONObject();
		Set<String> keys = form.keySet();
		boolean errors = false;
		for(String key : keys) {
			String value = (String) form.get(key);
			if(value==null || value.isEmpty()) {
				errors = true;
				break;
			}
		}
		
		if(errors) {
			mailResponse.put("status", "false");
			mailResponse.put("message", "Please fill in all the fields.");
			s.putMsg("copyright", "", "showMailResponse(" + mailResponse + ")");
			return;
		}
		
		
		String toemail = null;
		//Find the provider email
		FsNode providerNode = Fs.getNode("/domain/euscreenxl/user/" + provider + "/account/default");
		toemail = providerNode.getProperty("email");
		
		String body = "Identifier: " + id + "<br/>";
		body += "Title: " + title + "<br/>";
		body += "Link to item on EUScreen:<br/>";
		body += "<a href=\"http://euscreen.eu/item.html?id=" +id+ "\">http://euscreen.eu/item.html?id="+id+"</a><br/>";
		body += "-------------------------------------------<br/><br/>";
		body += "Subject: " + subject + "<br/>";
		body += "Message:<br/>";
		body += message + "<br/><br/>";
		body += "-------------------------------------------<br/>";
		body += "You can contact the sender of this message on: <a href=\"mailto:"+email+"\">"+email+"</a><br/>";
		
		if(this.inDevelMode()){ //In devel mode always send the email to the one filled in the form
			toemail = email;
		}
		
		//!!! Hack to send the email to the one filled in the form (for testing purposes). When on production it should be removed
		//toemail = email;
		
		boolean success = true;
		if(toemail!=null) {
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				Session session = (Session) envCtx.lookup("mail/Session");
	
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(mailfrom));
				InternetAddress to[] = new InternetAddress[1];
				to[0] = new InternetAddress(toemail);
				msg.setRecipients(Message.RecipientType.TO, to);
				
				InternetAddress bcc[] = new InternetAddress[1];
				bcc[0] = new InternetAddress("r.rozendal@noterik.nl");
				msg.setRecipients(Message.RecipientType.BCC, bcc);
				
				msg.setSubject(mailsubject);
				msg.setContent(body, "text/html");
				Transport.send(msg);
			} catch(Exception e) {
				System.out.println("Failed sending email: " + e);
				success = false;
			}
		} else {
			success = false;
		}
		
		String response = "Your message has been successfuly sent.";
		if(!success) response = "There was a problem sending your mail.<br/>Please try again later.";
		
		mailResponse.put("status", Boolean.toString(success));
		mailResponse.put("message", response);
		
		s.putMsg("copyright", "", "showMailResponse(" + mailResponse + ")");
		
	}
	
	private String setEdnaMapping(String screenshot) {
		if(screenshot != null){
			if (!wantedna) {
				screenshot = screenshot.replace("edna/", "");
			} else {
				int pos = screenshot.indexOf("edna/");
				if 	(pos!=-1) {
					screenshot = "http://images.euscreenxl.eu/"+screenshot.substring(pos+5);
				}
			}
		}
		return screenshot;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	//Themis NISV
	/////////////////////////////////////////////////////////////////////////////////////
	private static void sendTicket(String videoFile, String ipAddress, String ticket) throws IOException {
		URL serverUrl = new URL("http://82.94.187.227:8001/acl/ticket");
		HttpURLConnection urlConnection = (HttpURLConnection)serverUrl.openConnection();
		
		Long Sytime = System.currentTimeMillis();
		Sytime = Sytime / 1000;
		String expiry = Long.toString(Sytime+(15*60));
		
		// Indicate that we want to write to the HTTP request body
		
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		videoFile=videoFile.substring(1);
		
		//System.out.println("I send this video address to the ticket server:"+videoFile);
		//System.out.println("And this ticket:"+ticket);
		//System.out.println("And this EXPIRY:"+expiry);
		
		// Writing the post data to the HTTP request body
		BufferedWriter httpRequestBodyWriter = 
		new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
		String content="";
		if (isAndroid){
			content = "<fsxml><properties><ticket>"+ticket+"</ticket>"
			+ "<uri>/"+videoFile+"</uri><ip>"+ipAddress+"</ip> "
			+ "<role>user</role>"
			+ "<expiry>"+expiry+"</expiry><maxRequests>4</maxRequests></properties></fsxml>";
			isAndroid=false;
			//System.out.println("Android ticket!");
		}
		else {
			content = "<fsxml><properties><ticket>"+ticket+"</ticket>"
			+ "<uri>/"+videoFile+"</uri><ip>"+ipAddress+"</ip> "
			+ "<role>user</role>"
			+ "<expiry>"+expiry+"</expiry><maxRequests>1</maxRequests></properties></fsxml>";
		}
		//System.out.println("sending content!!!!"+content);
		httpRequestBodyWriter.write(content);
		httpRequestBodyWriter.close();
		
		// Reading from the HTTP response body
		Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
		while(httpResponseScanner.hasNextLine()) {
			System.out.println(httpResponseScanner.nextLine());
		}
		httpResponseScanner.close();		
	}
	
	private static final String[] HEADERS_TO_TRY = { 
		"X-Forwarded-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
		"REMOTE_ADDR" };
		
	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
		String ip = request.getHeader(header);
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
		return ip;
		}
		}
		return request.getRemoteAddr();
	}
}
