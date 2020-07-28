package org.ckCoder.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class MailGatewayTest {

    @Test
    void instanciate() {
        boolean status = false;

        try {
            Properties properties = new Properties();
            // Set Connection properties
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

            Transport.send(message);

            status = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(status);
    }
}