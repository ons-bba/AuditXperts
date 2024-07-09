//package esprit.experts.utils;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.googleapis.json.GoogleJsonError;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.Message;
//import org.apache.commons.codec.binary.Base64;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.util.Objects;
//import java.util.Properties;
//import java.util.Set;
//
//
//
//public class Gmailer {
//    Gmail service ;
//    public Gmailer() throws IOException, GeneralSecurityException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        GsonFactory json = GsonFactory.getDefaultInstance();
//         service = new Gmail.Builder(HTTP_TRANSPORT, json, getCredientals(HTTP_TRANSPORT, json))
//                .setApplicationName("javaa")
//                .build();
//    }
//
//    private  static  final  String EMAIL ="espritcotransport@gmail.com";
//    private static  Credential getCredientals(final NetHttpTransport HTTP_TRANSPORT , GsonFactory jsonFactory)throws IOException {
//
//
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(jsonFactory,  new InputStreamReader(Objects.requireNonNull(Gmailer.class.getResourceAsStream("/code.json"))));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
//                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        //returns an authorized Credential object.
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    };
//
//    public  void SendEmail(String subject , String msg ,String emailTo ) throws GeneralSecurityException, IOException, MessagingException {
//
//
//        // Encode as MIME message
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//        MimeMessage email = new MimeMessage(session);
//        email.setFrom(new InternetAddress(EMAIL));
//        email.addRecipient(javax.mail.Message.RecipientType.TO,
//                new InternetAddress(emailTo));
//        email.setSubject(subject);
//        email.setText(msg);
//
//
//        // Encode and wrap the MIME message into a gmail message
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        email.writeTo(buffer);
//        byte[] rawMessageBytes = buffer.toByteArray();
//        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//        Message message = new Message();
//        message.setRaw(encodedEmail);
//
//        try {
//            // Create send message
//            message = this.service.users().messages().send("Xperts-Audits", message).execute();
//            System.out.println("Message id: " + message.getId());
//            System.out.println(message.toPrettyString());
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            GoogleJsonError error = e.getDetails();
//            if (error.getCode() == 403) {
//                System.err.println("Unable to send message: " + e.getDetails());
//            } else {
//                throw e;
//            }
//        }
//    }
//
//    public static void  MAIL( String email , String pass , String name ) throws MessagingException, GeneralSecurityException, IOException {
//        String emailBody = String.format("Welcome to Xpert-Audit! \n\nDear %s,\n\nThank you for creating an account with us. Your password is: %s\n\nBest regards,\nXpert-Audit Team", name, pass);
//
//        new  Gmailer().SendEmail("You Account has been created", emailBody , email);
//    }
//}
