package CheckForm;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public static void sendResetPasswordEmail(String email, String resetCode) {
        final String username = "accessnameit@gmail.com";
        final String password = "inqa mbyt lgak bijc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
            message.setSubject("Reset Your Password");
            message.setText("Dear user,"
                + "\n\n Here is your password reset code: " + resetCode);

            Transport.send(message);

            System.out.println("Email sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
