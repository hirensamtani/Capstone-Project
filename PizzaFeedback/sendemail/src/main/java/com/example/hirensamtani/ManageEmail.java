package com.example.hirensamtani;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ManageEmail  {
    private EmailBean emailBean;

    public ManageEmail(EmailBean emailBean){
        this.emailBean = emailBean;
    }


    public int sendEmail(final EmailBean emailBean){
        int success = 0;

        // Recipient's email ID needs to be mentioned.
        /*String to = "abcd@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "web@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";
*/
        // Get system properties
        Properties props = new Properties();

        // Setup mail server
        props.put("mail.smtp.host", emailBean.getHost());
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");






        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        //return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                        return new PasswordAuthentication(emailBean.getFromEmailAddress(),emailBean.getFromEmailPassword());
                    }
                });

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(emailBean.getFromEmailAddress()));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailBean.getToEmail()));

            // Set Subject: header field
            message.setSubject(emailBean.getSubject());

            // Now set the actual message
            message.setText(emailBean.getMessage());

            /*BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(emailBean.getMessage());

            Multipart multipart = new MimeMultipart();*/

            // Set text message part
            /*multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();*/
            /*String filename = "/home/manisha/file.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);*/
            /*multipart.addBodyPart(messageBodyPart);*/

            // Send the complete message parts
            //message.setContent(multipart);

            // Send message
            Transport.send(message);
            success=1;
            //System.out.println("Sent message successfully....");
        }catch (Exception mex) {
            mex.printStackTrace();
            success=0;
        }





        return success;
    }


}
