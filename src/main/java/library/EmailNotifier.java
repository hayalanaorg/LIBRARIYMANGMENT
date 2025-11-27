package library;

import service.EmailService;

/**
 * Observer implementation that sends real emails using EmailService.
 */
public class EmailNotifier implements Observer {

    private final EmailService emailService;

    public EmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(Member m, String message) {
        String subject = "Book Reminder";
        emailService.sendEmail(m.getEmail(), subject, message);
    }
}

