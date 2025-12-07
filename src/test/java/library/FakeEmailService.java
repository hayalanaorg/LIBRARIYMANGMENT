package library;

import service.EmailService;

class FakeEmailService extends EmailService {
    public FakeEmailService(String u, String p) {
        super(u, p);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        // ما تعمل اشي — Fake بالكامل
    }
}
