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
import org.springfield.lou.fs.*;
import org.springfield.lou.homer.LazyHomer;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.application.types.Filter;
import org.springfield.lou.application.types.conditions.*;


public class EuscreenxlitemApplication extends Html5Application{
	
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
		
		setRelated(s);
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
		String title = node.getProperty(FieldMappings.getSystemFieldName("title"));
		String originalTitle = node.getProperty(FieldMappings.getSystemFieldName("originalTitle"));
		String provider = node.getProperty(FieldMappings.getSystemFieldName("provider"));
		String productionYear = node.getProperty(FieldMappings.getSystemFieldName("year"));
		String country = node.getProperty(FieldMappings.getSystemFieldName("country"));
		String summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus in lectus quis nibh gravida ultrices. Maecenas sollicitudin justo nec urna porta tincidunt. Ut cursus scelerisque interdum. Nulla varius ante et porta aliquet. Aenean nunc augue, aliquam eget nibh ut, mollis pretium lacus. Duis fermentum dui lobortis lorem malesuada, non consectetur erat malesuada. Proin malesuada odio id pulvinar vestibulum. Nam tincidunt arcu egestas nunc scelerisque rhoncus. Nulla et accumsan ante. Quisque ac massa vestibulum, consequat odio non, consectetur diam. Nam pellentesque orci in dolor porta laoreet. Vestibulum at lacinia nisi, ut mattis arcu. Vivamus nec auctor diam. Etiam dolor justo, pellentesque placerat lorem nec, fermentum consequat velit. Vestibulum lorem lectus, elementum sed felis sed, aliquet aliquet felis.";
		String clipTitle = node.getProperty(FieldMappings.getSystemFieldName("clipTitle"));
		String publisher = node.getProperty(FieldMappings.getSystemFieldName("publisher"));
		String broadcastChannel = node.getProperty(FieldMappings.getSystemFieldName("broadcastChannel"));
		
		JSONObject message = new JSONObject();
		message.put("title", title);
		message.put("originalTitle", originalTitle);
		message.put("provider", provider);
		message.put("productionYear", productionYear);
		message.put("country", country);
		message.put("summary", summary);
		message.put("clipTitle", clipTitle);
		message.put("publisher", publisher);
		message.put("broadcastChannel", broadcastChannel);
	
		s.putMsg("template", "", "setData(" + message + ")");
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
			relatedItem.put("screenshot", retrievedNode.getProperty(FieldMappings.getSystemFieldName("screenshot")));
			relatedItem.put("type", retrievedNode.getName());
			relatedItem.put("duration", retrievedNode.getProperty(FieldMappings.getSystemFieldName("duration")));
			objectToSend.add(relatedItem);
		}
		
		s.putMsg("related", "", "setRelated(" + objectToSend + ")");
	}
}
