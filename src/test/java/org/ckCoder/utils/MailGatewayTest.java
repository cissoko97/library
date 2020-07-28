package org.ckCoder.utils;

import org.apache.log4j.Logger;
import org.ckCoder.models.Book;
import org.ckCoder.models.Line;
import org.ckCoder.models.User;
import org.ckCoder.service.BookService;
import org.ckCoder.service.OrderService;
import org.ckCoder.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class MailGatewayTest {
    Logger logger = Logger.getLogger(getClass());
    @Test
    void instanciate() throws MessagingException, SQLException {
        /*boolean status = false;

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
            message.setSubject("Your book command");

            Multipart multipart = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText("Hi name. list of book command");

            multipart.addBodyPart(bodyPart);

            bodyPart = new MimeBodyPart();

            message.setFrom(new InternetAddress("davidluiz.matjaba@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("boriscissoko@gmail.com"));

           // message.setText("Hi, This mail is to inform you...");

            Transport.send(message);
            logger.debug("email send succesfully");
            status = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(status);*/
        final OrderService orderService = new OrderService();
        final UserService userService = new UserService();

        List<Line> lines = orderService.findOrderAndLineOrder(1L);
        System.out.println(lines.size());
        User user = userService.findByEmailAndPassword("davidluiz.matjaba@gmail.com","123456");
        MailGateway mailGateway = new MailGateway();
        mailGateway.instanciate(user, lines);
    }
}