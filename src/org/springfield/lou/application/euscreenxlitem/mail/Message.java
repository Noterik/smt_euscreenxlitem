package org.springfield.lou.application.euscreenxlitem.mail;

public interface Message {
	public String getSubject();
	public String getRecipient();
	public String getSender();
	public String getBody();
	public String getCCRecipient();
}
