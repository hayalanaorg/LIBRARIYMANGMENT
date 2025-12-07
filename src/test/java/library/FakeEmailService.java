package library;

import service.EmailService;


public class FakeEmailService extends EmailService {

  
    public FakeEmailService(String u, String p) {
        super(u, p);
    }

   
    @Override
    public void sendEmail(String to, String subject, String body) {
        // Fake email sending — intentionally does nothing
    }
}
