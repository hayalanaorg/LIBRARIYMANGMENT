package library;

import org.junit.jupiter.api.Test;
import service.EmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailNotifierTest {

    // Fake EmailService: ما ببعث إيميل، بس بحفظ آخر قيم استلمها
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

        // نستخدم النسخة الوهمية بدل الحقيقي
        FakeEmailService fake = new FakeEmailService();

        // نمررها للـ notifier
        EmailNotifier notifier = new EmailNotifier(fake);

        // عضو وهمي
        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        // استدعاء notify
        notifier.notify(m, "Hello Lana");

        // التحقق
        assertEquals("lana@mail.com", fake.lastTo);         // ✔ من member
        assertEquals("Book Reminder", fake.lastSubject);    // ✔ ثابت حسب notifier
        assertEquals("Hello Lana", fake.lastBody);          // ✔ نفس message
    }
}
