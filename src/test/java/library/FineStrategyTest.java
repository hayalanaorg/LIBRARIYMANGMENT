package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.BookFineStrategy;
import library.CDFineStrategy;
import library.FineStrategy;

/**
 * Sprint 5 – Fine calculation testing
 */
public class FineStrategyTest {

    @Test
    void testBookFine() {
        FineStrategy bookFine = new BookFineStrategy();
        assertEquals(30, bookFine.calculateFine(3)); // 3 أيام × 10 = 30
    }

    @Test
    void testCDFine() {
        FineStrategy cdFine = new CDFineStrategy();
        assertEquals(60, cdFine.calculateFine(3)); // 3 أيام × 20 = 60
    }

    @Test
    void testNoOverdue() {
        FineStrategy cdFine = new CDFineStrategy();
        FineStrategy bookFine = new BookFineStrategy();
        assertEquals(0, cdFine.calculateFine(0));
        assertEquals(0, bookFine.calculateFine(0));
    }
}

