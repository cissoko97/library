package org.ckCoder.utils;

import org.apache.log4j.Logger;
import org.ckCoder.models.Line;
import org.ckCoder.models.User;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailGateway {


    public static void instanciate(User user, Collection<Line> books) throws MessagingException {
        Logger logger = Logger.getLogger(MailGateway.class);
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
            message.setFrom(new InternetAddress("davidluiz.matjaba@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));


            Multipart multipart = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText("Hi " + user.getPerson().getSurname() + " " +
                    user.getPerson().getName() + ". " + "list of book command");

            multipart.addBodyPart(bodyPart);
            OutputStream outputStream = null;
            for (Line l : books) {
                try {
                    File file =  File.createTempFile("hello", ".tmp");
                    //System.out.println("absolute path : " + file.getAbsolutePath());
                    outputStream = new FileOutputStream(file);
                    outputStream.write(l.getBook().getBookBinary());

                    bodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    bodyPart.setDataHandler(new DataHandler(source));
                    bodyPart.setFileName(l.getBook().getTitle() + ".pdf");
                    multipart.addBodyPart(bodyPart);
                    file.deleteOnExit();
                   // System.out.println("this file book exists" + file.exists());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(outputStream != null)
                            outputStream.close();
                    } catch (IOException ignored) {
                        logger.debug("a book file could not be created before sending the email");
                        System.out.println("Error while closing stream" + ignored);
                    }
                }
            }


            // message.setText("Hi, This mail is to inform you...");

            message.setContent(multipart);
            logger.info("send email to : " + user.getEmail());
            System.out.println("send email to : " + user.getEmail());
            Transport.send(message);
            logger.debug("email send succesfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
