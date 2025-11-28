package library;

import org.junit.jupiter.api.Test;

import service.EmailService;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Test
    void testEmailServiceConstructor() {
        EmailService es = new EmailService("lana", "123");

        assertNotNull(es);
    }

    @Test
    void testSendEmailDoesNotThrow1() {
        EmailService es = new EmailService("lana", "123");

        assertDoesNotThrow(() -> es.sendEmail("test@mail.com", "Hello", ""));
    }


@Test
void testSendEmailDoesNotThrow2() {
    EmailService es = new FakeEmailService("lana", "123");

    assertDoesNotThrow(() ->
        es.sendEmail("test@mail.com", "Hello", "")
    );
}
}
