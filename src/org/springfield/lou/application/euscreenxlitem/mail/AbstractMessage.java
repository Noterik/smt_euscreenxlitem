package org.springfield.lou.application.euscreenxlitem.mail;

public class AbstractMessage implements Message {
	
	private String recipient;
	private String sender;
	private String body;
	private String subject;
	private String ccrecipient;
	
	public AbstractMessage(){}
	
	public AbstractMessage(String recipient, String sender, String body, String ccRecipient){
		this.recipient = recipient;
		this.sender = sender;
		this.body = body;
		this.ccrecipient = ccRecipient;
	}

	@Override
	public String getRecipient() {
		return this.recipient;
	}
	
	@Override
	public String getCCRecipient() {
		return this.ccrecipient;
	}

	@Override
	public String getSender() {
		return this.sender;
	}
	
	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getBody() {
		return this.body;
	}
	
	public void setRecipient(String recipient){
		this.recipient = recipient;
	}
	
	public void setCCRecipient(String ccrecipient){
		this.ccrecipient = ccrecipient;
	}
	
	public void setSender(String sender){
		this.sender = sender;
	}
	
	public void setBody(String body){
		this.body = body;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}

}
