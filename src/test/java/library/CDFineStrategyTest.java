package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CDFineStrategyTest {

    @Test
    void testCalculateFineInt() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(100, s.calculateFine(5));
        assertEquals(0, s.calculateFine(0));
    }

    @Test
    void testCalculateFineLong() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(200, s.calculateFine(10L));
        assertEquals(0, s.calculateFine(0L));
    }

    @Test
    void testLargeValues() {
        CDFineStrategy s = new CDFineStrategy();
        assertEquals(20000, s.calculateFine(1000L));
    }
}
