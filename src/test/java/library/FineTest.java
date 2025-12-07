package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link fine} class.
 *
 * <p>This test suite verifies proper fine behavior, including:</p>
 * <ul>
 *     <li>Correct initialization of fine values</li>
 *     <li>Partial payments</li>
 *     <li>Full payments</li>
 *     <li>Overpayment handling</li>
 *     <li>Ignoring zero or negative payments</li>
 * </ul>
 *
 * <p>The tests ensure that fine behavior remains stable and follows the
 * project rules regarding fee reductions and paid status.</p>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class FineTest {

    private user user;
    private fine fine;

    /**
     * Sets up a sample user and fine before each test.
     */
    @BeforeEach
    void setUp() {
        user = new user("u1", "Alice", "pass", "Alice vatial", true);
        fine = new fine(user, 100.0);
    }

    /**
     * Tests correct initialization of fine data.
     */
    @Test
    void testFineInitialization() {
        assertEquals(user, fine.getUser(), "Fine should be linked to the correct user");
        assertEquals(100.0, fine.getAmount(), 0.001, "Initial fine amount should be 100");
        assertFalse(fine.isPaid(), "Fine should not be marked as paid initially");
    }

    /**
     * Tests partial payment and ensures the fine is reduced but not cleared.
     */
    @Test
    void testPayPartial() {
        fine.pay(30.0);
        assertEquals(70.0, fine.getAmount(), 0.001, "Fine should decrease after partial payment");
        assertFalse(fine.isPaid(), "Fine should not be marked as paid yet");
    }

    /**
     * Tests full payment, ensuring the fine becomes zero and marked as paid.
     */
    @Test
    void testPayFull() {
        fine.pay(100.0);
        assertEquals(0.0, fine.getAmount(), 0.001, "Fine should be zero after full payment");
        assertTrue(fine.isPaid(), "Fine should be marked as paid");
    }

    /**
     * Ensures that overpayment does not produce a negative fine amount.
     */
    @Test
    void testPayOverpayment() {
        fine.pay(150.0);
        assertEquals(0.0, fine.getAmount(), 0.001, "Fine should not go negative after overpayment");
        assertTrue(fine.isPaid(), "Fine should be marked as paid");
    }

    /**
     * Ensures zero or negative payments do not affect the fine.
     */
    @Test
    void testPayZeroOrNegative() {
        fine.pay(0.0);
        assertEquals(100.0, fine.getAmount(), 0.001, "Paying zero should not change amount");

        fine.pay(-50.0);
        assertEquals(100.0, fine.getAmount(), 0.001, "Paying negative should not change amount");
    }
}
