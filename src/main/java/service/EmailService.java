package service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Provides functionality for sending emails using Gmail's SMTP server.
 * <p>
 * This class is used by the library system to send real notifications,
 * typically through {@link library.EmailNotifier} in the Observer pattern.
 * </p>
 *
 * <h2>SMTP Configuration:</h2>
 * <ul>
 *     <li>Host: smtp.gmail.com</li>
 *     <li>Port: 587</li>
 *     <li>Security: TLS</li>
 *     <li>Authentication: Required</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * EmailService email = new EmailService("yourEmail@gmail.com", "appPassword");
 * email.sendEmail("member@mail.com", "Reminder", "Your book is overdue.");
 * </pre>
 *
 * <h3>Important Notes:</h3>
 * <ul>
 *     <li>Requires an App Password (Google Accounts → Security → App Passwords)</li>
 *     <li>Regular Gmail passwords will not work</li>
 *     <li>Exceptions are printed to console for debugging</li>
 * </ul>
 *
 * @author
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class EmailService {

    /** Gmail address used to authenticate and send emails. */
    private final String username;

    /** App password associated with the Gmail account. */
    private final String password;

    /**
     * Creates a new email service with the provided SMTP credentials.
     *
     * @param username Gmail address used to send messages
     * @param password App password required for Gmail SMTP authentication
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an email using Gmail's SMTP server.
     *
     * @param to      recipient email address
     * @param subject subject of the message
     * @param body    content of the email
     *
     * @throws RuntimeException wrapped exception if message sending fails
     */
    public void sendEmail(String to, String subject, String body) {

        // SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Authentication setup
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Build email message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(body);

            // Send email
            Transport.send(msg);

            System.out.println("Email successfully sent to " + to);

        } catch (MessagingException ex) {
            ex.printStackTrace();
            System.out.println("Email failed: " + ex.getMessage());
        }
    }
}
