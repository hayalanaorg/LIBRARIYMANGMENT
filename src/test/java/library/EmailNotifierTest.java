package library;

import org.junit.jupiter.api.Test;
import service.EmailService;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link EmailNotifier} class.
 *
 * <p>This test verifies the Observer implementation responsible for sending
 * email reminders through {@link EmailService}.</p>
 *
 * <h2>Key behaviors tested:</h2>
 * <ul>
 *     <li>Ensuring {@code notify()} correctly delegates email sending to EmailService</li>
 *     <li>Validating correct email address retrieval from {@link Member}</li>
 *     <li>Ensuring fixed subject "Book Reminder" is applied</li>
 *     <li>Confirming email body matches the notification message</li>
 * </ul>
 *
 * <p>A {@code FakeEmailService} subclass is used instead of the real
 * SMTP-based service to safely capture method calls during testing.</p>
 *
 * @version 1.0
 * @author
 *     Lana Omar (Documentation)
 * @since 2025-12-07
 */
public class EmailNotifierTest {

    /**
     * A fake EmailService implementation used only for testing.
     * It stores the last sent email details instead of performing SMTP calls.
     */
    static class FakeEmailService extends EmailService {

        String lastTo;
        String lastSubject;
        String lastBody;

        /**
         * Creates a fake service without real credentials.
         */
        public FakeEmailService() {
            super("fakeUser", "fakePass");
        }

        /**
         * Captures email arguments instead of sending an actual email.
         */
        @Override
        public void sendEmail(String to, String subject, String body) {
            lastTo = to;
            lastSubject = subject;
            lastBody = body;
        }
    }

    /**
     * Tests that {@link EmailNotifier#notify(Member, String)} correctly calls
     * the underlying EmailService with the right email, subject, and body.
     */
    @Test
    void testNotifierCallsEmailService() {

        FakeEmailService fake = new FakeEmailService();

        EmailNotifier notifier = new EmailNotifier(fake);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        notifier.notify(m, "Hello Lana");

        assertEquals("lana@mail.com", fake.lastTo);
        assertEquals("Book Reminder", fake.lastSubject);
        assertEquals("Hello Lana", fake.lastBody);
    }
}
