package com.contactstorage.Service;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session ;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	
	public boolean sendEmail(String from,String message,String to ,String subject) {
		
		boolean f =false;
		
		// variable for gmail host
		// hosting is service ,in which data is stored on server example messages,attachments ,other data
		// basically host is the place or address where data of that particular website will be stored
		
		String host ="smtp.gmail.com";
		
	    // this is gmail port number	
		// for sending mail in gmail , encrytped type SSL we use port 465
		
	//	int port = 465;
		
		// now we have to get the properties , current system properties
		// we have to use key value pair in properties ,like map
		
		Properties properties =System.getProperties();
		
/////// now we have to add more information into properties
		
		properties.put("mail.smtp.host",host);
		
		properties.put("mail.smtp.port",465);
		
		//SSL (Secure Sockets Layer) is a transaction security standard that provides 
		//encrypted protection between browsers and App Servers.
		//When SSL is enabled for an App Server, 
		//browsers communicate with the App Server by means of an HTTPS connection, 
		//which is HTTP over an encrypted Secure Sockets Layer.
		
		properties.put("mail.smtp.ssl.enable","true");
		
		// we have to use authentication
		
		properties.put("mail.smtp.auth", "true");
		
		// STEP 1 : To get the session object , we have to use Session.getInstance() method
		
		//The javax.mail.Session class provides two methods to get the object of session, 
		//Session.getDefaultInstance() method and Session.getInstance() method.
		//You can use any method to get the session object.
		
	  Session session = Session.getInstance(properties ,new Authenticator() {

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO Auto-generated method stub
			return new PasswordAuthentication("sachinbansal789@gmail.com", "mtgrweifuxjnosxn");
		}
		  
	  });
	  
	  session.setDebug(true);
	  
	  // STEP 2 : COMPOSE THE MESSAGE
	  
	//  The javax.mail.Message class provides methods to compose the message.
	 // But it is an abstract class so its subclass javax.mail.internet.MimeMessage class is mostly used.
	//  To create the message, you need to pass session object in MimeMessage class constructor
	  // this Mimemessage method takes session as an argument
		
	  MimeMessage m = new MimeMessage(session);
	  
	  try {
		  
		  // from email
		  
		  m.setFrom(from);
		  
		  
		  // this takes two argument recipient tyoe and addess, we have to use internet address
		  
		  m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		  
		  // adding subject to email
		  m.setSubject(subject);
		  
		  // adding text message 
		  
		  
		  m.setText(message);
		  		  
//		  // insted of message if we want to send an attachment as well
//		 
//		  String path = "C:\\Users\\HP\\Desktop\\sachin...jpg";   n
//		  
//		  MimeMultipart mimeMultiPart = new MimeMultipart();
//		  
//		  MimeBodyPart textmime = new MimeBodyPart();
//		  
//		  MimeBodyPart filemime= new MimeBodyPart();
//		  
//		  try {
//			  
//			  File file = new File(path);
//			  
//			  textmime.setText(message);
//			  
//			  filemime.attachFile(file);
//			  
//			  mimeMultiPart.addBodyPart(filemime);
//			  
//			  mimeMultiPart.addBodyPart(textmime);
//			  
//		  }catch (Exception e) {
//			  e.printStackTrace();
//		  }
//		  
//		  m.setContent(mimeMultiPart);
//		  
		    
		  
		// STEP : 3 , use TRANSPORT class to send message
		  
		  Transport.send(m);
		  
		  f= true;
		  
	  }catch(Exception e) {
		  e.printStackTrace();
		  
	  }
	  
	  return f;
	  
	}
	
}
