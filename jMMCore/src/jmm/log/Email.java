/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.log;

import java.io.File;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Create and Send a new Bug Report Email
 * 
 * @author Bryan Beck
 * @since 28.11.2012
 */
public class Email { 
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(Email.class);
    
    private final String from;
    private final String to;
    private final String subject;
    private final String message;
    private final String[] attachmentPaths;
    
    private static final Properties properties;
    // Setup mail server
    static{
        properties = System.getProperties();
        // HOST_NAME hast been removed!
        properties.put("mail.smtp.host", "");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }
    
    /**
     * Creates a new email
     * @param from the mail shipper
     * @param to the mail recipent
     * @param subject the mail subject
     * @param message the mail message
     * @param attachmentPaths the paths to mail attachements
     */
    public Email(String from, String to, String subject, String message, String[] attachmentPaths){
        this.from = from;
        this.to = to;
        this.message = message;
        this.subject = subject;
        this.attachmentPaths = attachmentPaths;
    }
    
    /**
     * Sends the email to the recipent 
     * 
     * @return true If the email was sent successfully  <br/> false otherwise
     */
   public boolean send() {      
      // Get the default Session object.
       // USERNAME and PASSWORD has been removed!
       Authenticator authenticator = new SMTPAuthenticator("username", "password");
       Session session = Session.getDefaultInstance(properties, authenticator);

      try{
         // Create a default MimeMessage object.
         MimeMessage mimMessage = new MimeMessage(session);
         // Set From: header field of the header.
         mimMessage.setFrom(new InternetAddress(from));
         // Set To: header field of the header.
         mimMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         // Set Subject: header field
         mimMessage.setSubject(subject);
         // Create the message part 
         BodyPart messageBodyPart = new MimeBodyPart();
         // Fill the message
         messageBodyPart.setText(message);
         // Create a multipar message
         Multipart multipart = new MimeMultipart();
         // Set text message part
         multipart.addBodyPart(messageBodyPart);
         // Part two is attachment         

         for(String filePath: attachmentPaths){
             File file = new File(filePath);
             if(file.exists() && file.isFile()){
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
             }
         }
         // Send the complete message parts
         mimMessage.setContent(multipart );
         // Send message
         Transport.send(mimMessage);
         return true;
      }catch (MessagingException mex) {
         LOG.error("Cannot create or send Email.", mex);
         return false;
      }
   }
   
   private class SMTPAuthenticator extends javax.mail.Authenticator {
        String username;
        String password;

        private SMTPAuthenticator(String authenticationUser, String authenticationPassword) {
            username = authenticationUser;
            password = authenticationPassword;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(new StringBuffer(username).reverse().toString(), new StringBuffer(password).reverse().toString());
        }
    }      
}
