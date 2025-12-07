package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CDFineStrategy}.
 *
 * <p>This test class validates:</p>
 * <ul>
 *     <li>Correct fine calculation for int input</li>
 *     <li>Overloaded calculation for long input</li>
 *     <li>Handling of zero overdue days</li>
 *     <li>Handling of very large overdue values</li>
 * </ul>
 *
 * <p>According to project rules, CD fines = 20 NIS per overdue day.</p>
 *
 * @author
 *      Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class CDFineStrategyTest {

    /**
     * Tests the fine calculation using the int-based method.
     */
    @Test
    void testCalculateFineInt() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(100, s.calculateFine(5));
        assertEquals(0, s.calculateFine(0));
    }

    /**
     * Tests the fine calculation using the long-based method.
     */
    @Test
    void testCalculateFineLong() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(200, s.calculateFine(10L));
        assertEquals(0, s.calculateFine(0L));
    }

    /**
     * Ensures very large values do not break the logic.
     * Example: 1000 overdue days → 1000 * 20 = 20000
     */
    @Test
    void testLargeValues() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(20000, s.calculateFine(1000L));
    }
}
