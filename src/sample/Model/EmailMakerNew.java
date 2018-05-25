//package sample.Model;
//
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.util.Base64;
//import com.google.api.client.util.store.FileDataStoreFactory;
//
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.*;
//import com.google.api.services.gmail.Gmail;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.*;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//
//public class EmailMakerNew {
//
//    private static final String APPLICATION_NAME =
//            "mail";
//
//
//    private static final java.io.File DATA_STORE_DIR = new java.io.File("credentials/gmail");
//
//    private static FileDataStoreFactory DATA_STORE_FACTORY;
//
//    private static final JsonFactory JSON_FACTORY =
//            JacksonFactory.getDefaultInstance();
//
//    private static HttpTransport HTTP_TRANSPORT;
//
//    private static final List<String> SCOPES =
//            Arrays.asList(GmailScopes.GMAIL_SEND);
//
//    static {
//        try {
//            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    public static Credential authorize() throws IOException {
//        // Load client secrets.
//        InputStream in =
//                new FileInputStream("src\\sample\\Model\\client_secret.json");
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(
//                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                        .setDataStoreFactory(DATA_STORE_FACTORY)
//                        .setAccessType("offline")
//                        .build();
//        Credential credential = new AuthorizationCodeInstalledApp(
//                flow, new LocalServerReceiver()).authorize("user");
//        System.out.println(
//                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
//        return credential;
//    }
//
//    public static Gmail getGmailService() throws IOException {
//        Credential credential = authorize();
//        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    public static MimeMessage createEmail(String to,
//                                          String from,
//                                          String subject,
//                                          String bodyText)
//            throws MessagingException {
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//
//        MimeMessage email = new MimeMessage(session);
//
//        email.setFrom(new InternetAddress(from));
//        email.addRecipient(javax.mail.Message.RecipientType.TO,
//                new InternetAddress(to));
//        email.setSubject(subject);
//        email.setText(bodyText);
//        return email;
//    }
//
//    public static Message createMessageWithEmail(MimeMessage emailContent)
//            throws MessagingException, IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        emailContent.writeTo(buffer);
//        byte[] bytes = buffer.toByteArray();
//        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
//        Message message = new Message();
//        message.setRaw(encodedEmail);
//        return message;
//    }
//
//    public static Message sendMessage(Gmail service,
//                                      String userId,
//                                      MimeMessage emailContent)
//            throws MessagingException, IOException {
//        Message message = createMessageWithEmail(emailContent);
//        message = service.users().messages().send(userId, message).execute();
//
//        System.out.println("Message id: " + message.getId());
//        System.out.println(message.toPrettyString());
//        return message;
//    }
//
//}
