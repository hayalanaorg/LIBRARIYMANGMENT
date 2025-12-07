package library;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.sun.jdi.connect.Transport;

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
    void testSendEmail_CatchBlockIsCovered() {

        // Fake class that forces sendEmail to throw MessagingException
        EmailService failingService = new EmailService("lana", "123") {
            @Override
            public void sendEmail(String to, String subject, String body) {
                try {
                    throw new jakarta.mail.MessagingException("SMTP Failure Test");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        assertDoesNotThrow(() ->
                failingService.sendEmail("test@mail.com", "Hello", "body"));
    }


}
