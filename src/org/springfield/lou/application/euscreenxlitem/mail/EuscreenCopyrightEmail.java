package org.springfield.lou.application.euscreenxlitem.mail;

public class EuscreenCopyrightEmail extends AbstractMessage {
	public EuscreenCopyrightEmail(String recipient, String summery) {
		super();
		this.setRecipient(recipient);
		this.setCCRecipient("r.rozendal@noterik.nl");
		this.setSubject("Somebody showed interest in your item on EUScreen");
		this.setSender("euscreen-portal@noterik.nl");		
		this.setBody(summery);
	}

}
