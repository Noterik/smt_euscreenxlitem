package org.springfield.lou.application.euscreenxlitem.mail;

public class ForgotPassword extends AbstractMessage {
	public ForgotPassword(String user, String recipient, String appUrl, String ticket) {
		super();
		this.setRecipient(recipient);
		this.setSubject("MyEUscreen forgot password ");
		this.setSender("euscreen.mailer@gmail.com");
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("<p>Dear " + user + ",</p>");
		stringbuilder.append("<br />");
		stringbuilder.append("<p></p>");
		stringbuilder.append("<p>Please click the forgot password link below to complete your MyEUscreen signup.</p>");
		stringbuilder.append("<p>" + appUrl + "myeuscreen.html?page=forgotPassword&name=" + user + "&ticket=" + ticket +"</p>");
		stringbuilder.append("<br />");
		stringbuilder.append("<br />");
		stringbuilder.append("<br />");
		stringbuilder.append("<p>With kind regards, </p>");
		stringbuilder.append("<p>The Euscreen team</p>");
		System.out.println(stringbuilder.toString());
		this.setBody(stringbuilder.toString());
	}

}
