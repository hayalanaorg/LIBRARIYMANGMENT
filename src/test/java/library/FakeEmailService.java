package library;

import service.EmailService;

/**
 * A fake (test-double) implementation of {@link EmailService} used exclusively
 * for unit testing.
 *
 * <p>This class overrides the {@code sendEmail} method so that it performs
 * no real SMTP operations. It allows tests to verify email-related behavior
 * without requiring an internet connection, Gmail credentials, or any external
 * dependencies.</p>
 *
 * <h2>Why use this fake implementation?</h2>
 * <ul>
 *     <li>Prevents real emails from being sent during tests</li>
 *     <li>Ensures tests run quickly and reliably</li>
 *     <li>Avoids network failures or authentication issues</li>
 *     <li>Allows safe verification of email logic in classes such as
 *         {@link EmailNotifier}</li>
 * </ul>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class FakeEmailService extends EmailService {

    /**
     * Creates a new FakeEmailService instance.
     * The username and password are ignored but required by the superclass.
     *
     * @param u the email username (ignored)
     * @param p the email password (ignored)
     */
    public FakeEmailService(String u, String p) {
        super(u, p);
    }

    /**
     * Overrides the real email sending method.
     * <p>
     * In this fake version, the method intentionally does nothing, ensuring
     * that no actual SMTP request is made.
     * </p>
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email body text
     */
    @Override
    public void sendEmail(String to, String subject, String body) {
        // Fake email sending — intentionally does nothing
    }
}
