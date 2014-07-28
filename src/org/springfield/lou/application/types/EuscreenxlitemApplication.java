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

import java.io.BufferedReader;
import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import org.springfield.lou.application.Html5Application;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.application.components.BasicComponent;
import org.springfield.lou.application.components.ComponentInterface;
import org.springfield.lou.application.types.conditions.EqualsCondition;
import org.springfield.lou.homer.LazyHomer;
import org.springfield.fs.*;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.application.types.Filter;
import org.springfield.lou.application.types.conditions.*;


public class EuscreenxlitemApplication extends Html5Application{
	
	private boolean wantedna = true;
	/*
	 * Constructor for the preview application for EUScreen providers
	 * so they can check and debug their uploaded collections.
	 */
	public EuscreenxlitemApplication(String id) {
		super(id); 
		
		System.out.println("EuscreenxlitemApplication()");
				
		// default scope is each screen is its own location, so no multiscreen effects
		setLocationScope("screen"); 
		
		//refer the header and footer elements from euscreenxl element application. 
		this.addReferid("header", "/euscreenxlelements/header");
		this.addReferid("footer", "/euscreenxlelements/footer");
		this.addReferid("mobilenav", "/euscreenxlelements/mobilenav");
		
	}
	
	public void init(Screen s){
		System.out.println("EuscreenxlitemApplication.init()");
		String id = s.getParameter("id");
		String uri = "/domain/euscreenxl/user/*/*";
		
		FSList fslist = FSListManager.get(uri);
		List<FsNode> nodes = fslist.getNodesFiltered(id.toLowerCase()); // find the item
		if (nodes!=null && nodes.size()>0) {
			FsNode n = (FsNode)nodes.get(0);
			s.setProperty("mediaNode", n);
		}
		
		System.out.println("Euscreenxlitem.init()");
		if(s.getCapabilities() != null && s.getCapabilities().getDeviceModeName() == null){
			loadContent(s, "footer");
			s.putMsg("template", "", "activateTooltips()");
		}else{
			removeContent(s, "footer");
		}
		
		setRelated(s);
		setTerms(s);
	}
	
	public void loadMoreRelated(Screen s){
		System.out.println("EuscreenxlitemApplication.loadMoreRelated()");
	}
	
	public void startViewer(Screen s){
		System.out.println("EuscreenxlitemApplication.startViewer()");
		FsNode node = (FsNode) s.getProperty("mediaNode");
		String name = node.getName();
		
		System.out.println("NAME: " + name);
		
		if(name.equals("video")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawvideo/1");
			String[] videos = rawNode.getProperty("mount").split(",");
			JSONObject objectToSend = new JSONObject();
			JSONArray videosArray = new JSONArray();
			objectToSend.put("video", videosArray);
			for(int i = 0; i < videos.length; i++){
				videosArray.add(videos[i]);
			}
			s.putMsg("viewer", "", "setVideo(" + objectToSend + ")");
		}else if(name.equals("audio")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawaudio/1");
			String audio = rawNode.getProperty("mount");
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("audio", audio);
			s.putMsg("viewer", "", "setAudio(" + objectToSend + ")");
		}else if(name.equals("picture")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawpicture/1");
			String picture = rawNode.getProperty("mount");
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("picture", node.getProperty(FieldMappings.getSystemFieldName("screenshot")));
			objectToSend.put("raw", picture);
			objectToSend.put("alt", node.getProperty(FieldMappings.getSystemFieldName("title")));
			s.putMsg("viewer", "", "setPicture(" + objectToSend + ")");
		}else if(name.equals("doc")){
			FsNode rawNode = Fs.getNode(node.getPath() + "/rawdoc/1");
			String doc = rawNode.getProperty("mount");
			JSONObject objectToSend = new JSONObject();
			objectToSend.put("doc", doc);
			s.putMsg("viewer", "", "setDoc(" + objectToSend + ")");
		}
	}
	
	public void setMetadata(Screen s){
		System.out.println("EuscreenxlitemApplication.setMetadata()");
		
		FsNode node = (FsNode) s.getProperty("mediaNode");
		
		JSONObject message = new JSONObject();
		
		HashMap<String, String> mappings = FieldMappings.getMappings();
		for(Iterator<String> i = mappings.keySet().iterator(); i.hasNext();){
			String readable = i.next();
			String systemName = mappings.get(readable);
			
			try{
				message.put(readable, node.getProperty(FieldMappings.getSystemFieldName(readable)));
			}catch(NullPointerException npe){
				message.put(readable, "-");
			}
		}
		
		s.putMsg("metadata", "", "setData(" + message + ")");
	
	}
	
	private void setTerms(Screen s){
		FsNode node = (FsNode) s.getProperty("mediaNode");
		String terms = node.getProperty(FieldMappings.getSystemFieldName("terms"));
		
		JSONObject message = new JSONObject();
		message.put("terms", terms);
		s.putMsg("terms", "", "setText(" + message + ")");
	}
	
	private void setRelated(Screen s){
		
		FsNode node = (FsNode) s.getProperty("mediaNode");
		String topic = node.getProperty(FieldMappings.getSystemFieldName("topic"));
		
		String uri = "/domain/euscreenxl/user/*/*";
		FSList fslist = FSListManager.get(uri);
		List<FsNode> nodes = fslist.getNodes();
		
		System.out.println("SEARCH FOR: " + topic);
		
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
		
		nodes = topicFilter.apply(nodes);
		
		for(Iterator<FsNode> i = nodes.iterator(); i.hasNext();){
			FsNode retrievedNode = i.next();
			
			JSONObject relatedItem = new JSONObject();
			
			relatedItem.put("id", retrievedNode.getId());
			relatedItem.put("title", retrievedNode.getProperty(FieldMappings.getSystemFieldName("title")));
			relatedItem.put("provider", retrievedNode.getProperty(FieldMappings.getSystemFieldName("provider")));
			relatedItem.put("country", retrievedNode.getProperty(FieldMappings.getSystemFieldName("country")));
			relatedItem.put("screenshot", setEdnaMapping(retrievedNode.getProperty(FieldMappings.getSystemFieldName("screenshot"))));
			relatedItem.put("type", retrievedNode.getName());
			relatedItem.put("duration", retrievedNode.getProperty(FieldMappings.getSystemFieldName("duration")));
			objectToSend.add(relatedItem);
		}
		
		s.putMsg("related", "", "setRelated(" + objectToSend + ")");
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
}
