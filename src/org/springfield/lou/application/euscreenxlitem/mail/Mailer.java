package org.springfield.lou.application.euscreenxlitem.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {
	Properties mailerProperties;
	Session session;
	
	public Mailer() throws IOException{		
		mailerProperties = new Properties();
		mailerProperties.load(new FileInputStream("/springfield/lou/configs/myeuscreen/euscreenmail.properties"));
		session = Session.getInstance(mailerProperties,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					Properties credentials = new Properties();
					try {
						credentials.load(new FileInputStream("/springfield/lou/configs/myeuscreen/euscreenmail.properties"));
						return new PasswordAuthentication(credentials.getProperty("mail.user"), credentials.getProperty("mail.password"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
		});
	}
	
	public Mailer(String recipient, String sender, String message){
		super();
	}
	
	public void sendMessage(Message message){
		System.out.println("Lets send the message!");
		javax.mail.Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(message.getSender()));
			msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(message.getRecipient()));
			msg.setRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(message.getCCRecipient()));
			msg.setSubject(message.getSubject());
			
			msg.setContent(message.getBody(), "text/html");
						
			Transport.send(msg);
			System.out.println("Message has been send!");
			System.out.println("TO: " + message.getRecipient());
			System.out.println("CC: " + message.getCCRecipient());
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
