package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.BookFineStrategy;
import library.CDFineStrategy;
import library.FineStrategy;

/**
 * Unit tests for all {@link FineStrategy} implementations used in Sprint 5.
 *
 * <p>This test class verifies correct fine calculation for different media types,
 * including Books and CDs, as well as boundary cases such as zero overdue days.</p>
 *
 * <h2>What is tested:</h2>
 * <ul>
 *     <li>Book fine calculation using {@link BookFineStrategy}</li>
 *     <li>CD fine calculation using {@link CDFineStrategy}</li>
 *     <li>Zero-overdue behavior for both strategies</li>
 * </ul>
 *
 * <p>Fine rules:</p>
 * <ul>
 *     <li>Book → 10 NIS per overdue day</li>
 *     <li>CD → 20 NIS per overdue day</li>
 * </ul>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class FineStrategyTest {

    /**
     * Tests book fine calculation: 3 days × 10 NIS = 30.
     */
    @Test
    public void testBookFine() {
        FineStrategy s = new BookFineStrategy();
        assertEquals(30, s.calculateFine(3));
    }

    /**
     * Tests CD fine calculation: 3 days × 20 NIS = 60.
     */
    @Test
    public void testCDFine() {
        FineStrategy s = new CDFineStrategy();
        assertEquals(60, s.calculateFine(3));
    }

    /**
     * Ensures both strategies return zero when overdueDays = 0.
     */
    @Test
    void testNoOverdue() {
        FineStrategy cdFine = new CDFineStrategy();
        FineStrategy bookFine = new BookFineStrategy();
        assertEquals(0, cdFine.calculateFine(0));
        assertEquals(0, bookFine.calculateFine(0));
    }
}
