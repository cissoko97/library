package org.ckCoder.utils;

import org.apache.log4j.Logger;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailGateway {

    private static Logger logger = Logger.getLogger(MailGateway.class);

    public static void instanciate() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.connectiontimeout", 30000);
        properties.put("mail.smtp.writetimeout", 30000);
        properties.put("mail.smtp.timeout", 30000);
        properties.put("mail.smtp.starttls.enable", "true");

        //Set auth for user who send the mmail
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("davidluiz.matjaba@gmail.com", "bysjonvtposhlgoh");
            }
        };
        Session session = Session.getInstance(properties, authenticator);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("davidluiz.matjaba@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("boriscissoko@gmail.com"));
        message.setSubject("Hi, everyone");
        message.setText("Hi, This mail is to inform you...");

//        message.setContent();

        Transport.send(message);

    }


}
