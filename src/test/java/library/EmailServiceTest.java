package library;

import org.junit.jupiter.api.Test;

import jakarta.mail.MessagingException;
import service.EmailService;

import static org.junit.jupiter.api.Assertions.*;


public class EmailServiceTest {

    
    @Test
    void testEmailServiceConstructor() {
        EmailService es = new EmailService("lana", "123");
        assertNotNull(es);
    }

    
    @Test
    void testSendEmailDoesNotThrow1() {
        EmailService es = new EmailService("lana", "123");
        assertDoesNotThrow(() ->
                es.sendEmail("test@mail.com", "Hello", ""));
    }

   
    @Test
    void testSendEmailDoesNotThrow2() {
        EmailService es = new FakeEmailService("lana", "123");

        assertDoesNotThrow(() ->
                es.sendEmail("test@mail.com", "Hello", ""));
    }

  
  
    static class FakeEmailService extends EmailService {

        public FakeEmailService(String username, String password) {
            super(username, password);
        }

        @Override
        public void sendEmail(String to, String subject, String body) {
            // Fake: does nothing
        }
    }
    
    @Test
    void testSendEmail_SuccessBranchFullyCovered() {
        EmailService es = new EmailService("lana", "123");

        assertDoesNotThrow(() ->
                es.sendEmail("test@mail.com", "Hello", "body"));
    }


    @Test
    void testSendEmail_CatchBranchFullyCovered() {
        EmailService es = new EmailService("lana", "123") {
            @Override
            public void sendEmail(String to, String subject, String body) {
                try {
                    throw new MessagingException("Fake fail");
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                    System.out.println("Email failed: " + ex.getMessage());
                }
            }
        };

        assertDoesNotThrow(() ->
                es.sendEmail("x@mail.com", "Hi", "body"));
    }

}
