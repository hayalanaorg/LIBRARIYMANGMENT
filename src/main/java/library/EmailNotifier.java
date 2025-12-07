package library;

import service.EmailService;

/**
 * An implementation of the {@link Observer} interface responsible for sending
 * real email notifications to library members when certain events occur,
 * such as overdue loan reminders.
 * <p>
 * This class uses an underlying {@link EmailService} to deliver emails.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * EmailService service = new SMTPSender();
 * EmailNotifier notifier = new EmailNotifier(service);
 * notifier.notify(member, "Your book is overdue!");
 * </pre>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class EmailNotifier implements Observer {

    /** The email service used to send actual email messages. */
    private final EmailService emailService;

    /**
     * Creates an {@code EmailNotifier} with the given email service.
     *
     * @param emailService a concrete implementation of {@link EmailService}
     *                     used to send notification emails
     */
    public EmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends an email notification to the specified member.
     * <p>
     * The notification includes a predefined subject ("Book Reminder") and
     * a custom message describing the overdue or reminder details.
     * </p>
     *
     * @param m       the member who will receive the notification
     * @param message the email body content describing the reminder
     */
    @Override
    public void notify(Member m, String message) {
        String subject = "Book Reminder";
        emailService.sendEmail(m.getEmail(), subject, message);
    }
}
