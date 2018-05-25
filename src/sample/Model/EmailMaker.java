package sample.Model;

import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailMaker {

    public EmailMaker(){}

    public void sendMail(String sendTo, String message){

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = "italianbutcherordering@gmail.com";//
        final String password = "theitalianbutcher2017";
        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("italianbutcherordering@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendTo,false));
            msg.setSubject("Ordre fra \"The Italian Butcher\"");
            msg.setText(message);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        }catch (MessagingException e){ System.out.println(e);}


    }


    public String emailContructor(ArrayList<Product> orderList){

        String DATE_FORMAT = "dd/MM/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Date currentDate = new Date();

        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // plus one
        localDateTime = localDateTime.plusDays(1);


        String message = "Ordre fra \"The Italian Butcher\" " +
                "\n\nSkal leveres venglist:" + dateFormat8.format(localDateTime) +
                "\n\nAdresse: Strandvejen 203, 2900 Hellerup" +
                "\nCVR: 38522612" +
                "\nTel.:26295066" +
                "\n\n";
        for (Product product:orderList){

            message += product.getProductId() + " - " + product.getProductName() + " -  " + product.getQuantity() +"\n";
        }
        System.out.println(message);

        return message;
    }

}