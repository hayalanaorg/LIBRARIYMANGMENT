package library;

import org.junit.jupiter.api.Test;
import service.EmailService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link EmailService} class.
 *
 * <p>Since the real EmailService uses SMTP to send Gmail messages, these tests
 * only verify that the class can be instantiated and that the {@code sendEmail}
 * method does not throw runtime exceptions under normal circumstances.</p>
 *
 * <h2>What this test covers:</h2>
 * <ul>
 *     <li>Constructor validation</li>
 *     <li>Ensuring {@code sendEmail()} executes without throwing exceptions</li>
 *     <li>Mock/fake email sending using a fake subclass</li>
 * </ul>
 *
 * <p>Note: These tests do not verify the actual delivery of emails, since
 * SMTP interactions require live network connections.</p>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class EmailServiceTest {

    /**
     * Ensures the EmailService constructor creates a valid object.
     */
    @Test
    void testEmailServiceConstructor() {
        EmailService es = new EmailService("lana", "123");
        assertNotNull(es);
    }

    /**
     * Ensures sendEmail() does not throw exceptions when using real SMTP settings.
     * <p>This does NOT guarantee the email is actually sent.</p>
     */
    @Test
    void testSendEmailDoesNotThrow1() {
        EmailService es = new EmailService("lana", "123");
        assertDoesNotThrow(() ->
                es.sendEmail("test@mail.com", "Hello", ""));
    }

    /**
     * Ensures sendEmail() does not throw exceptions when using a FakeEmailService,
     * which overrides sendEmail and avoids network operations.
     */
    @Test
    void testSendEmailDoesNotThrow2() {
        EmailService es = new FakeEmailService("lana", "123");

        assertDoesNotThrow(() ->
                es.sendEmail("test@mail.com", "Hello", ""));
    }

    /**
     * Simple fake EmailService used for safe testing.
     * This avoids any SMTP communication.
     */
    static class FakeEmailService extends EmailService {

        public FakeEmailService(String username, String password) {
            super(username, password);
        }

        @Override
        public void sendEmail(String to, String subject, String body) {
            // Fake: does nothing
        }
    }
}
