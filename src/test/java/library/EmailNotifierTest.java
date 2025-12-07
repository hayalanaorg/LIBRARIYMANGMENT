package library;

import org.junit.jupiter.api.Test;
import service.EmailService;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmailNotifierTest {

    static class FakeEmailService extends EmailService {

        String lastTo;
        String lastSubject;
        String lastBody;

      
        public FakeEmailService() {
            super("fakeUser", "fakePass");
        }

       
        @Override
        public void sendEmail(String to, String subject, String body) {
            lastTo = to;
            lastSubject = subject;
            lastBody = body;
        }
    }

 
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
