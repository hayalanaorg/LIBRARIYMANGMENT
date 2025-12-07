package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FineTest {

    private user user;
    private fine fine;

   
    @BeforeEach
    void setUp() {
        user = new user("u1", "Alice", "pass", "Alice vatial", true);
        fine = new fine(user, 100.0);
    }

   
    @Test
    void testFineInitialization() {
        assertEquals(user, fine.getUser(), "Fine should be linked to the correct user");
        assertEquals(100.0, fine.getAmount(), 0.001, "Initial fine amount should be 100");
        assertFalse(fine.isPaid(), "Fine should not be marked as paid initially");
    }


    @Test
    void testPayPartial() {
        fine.pay(30.0);
        assertEquals(70.0, fine.getAmount(), 0.001, "Fine should decrease after partial payment");
        assertFalse(fine.isPaid(), "Fine should not be marked as paid yet");
    }

    @Test
    void testPayFull() {
        fine.pay(100.0);
        assertEquals(0.0, fine.getAmount(), 0.001, "Fine should be zero after full payment");
        assertTrue(fine.isPaid(), "Fine should be marked as paid");
    }

    
    @Test
    void testPayOverpayment() {
        fine.pay(150.0);
        assertEquals(0.0, fine.getAmount(), 0.001, "Fine should not go negative after overpayment");
        assertTrue(fine.isPaid(), "Fine should be marked as paid");
    }

    @Test
    void testPayZeroOrNegative() {
        fine.pay(0.0);
        assertEquals(100.0, fine.getAmount(), 0.001, "Paying zero should not change amount");

        fine.pay(-50.0);
        assertEquals(100.0, fine.getAmount(), 0.001, "Paying negative should not change amount");
    }
}
