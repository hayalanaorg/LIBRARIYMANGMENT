package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.BookFineStrategy;
import library.CDFineStrategy;
import library.FineStrategy;


public class FineStrategyTest {

    
    @Test
    public void testBookFine() {
        FineStrategy s = new BookFineStrategy();
        assertEquals(30, s.calculateFine(3));
    }

   
    @Test
    public void testCDFine() {
        FineStrategy s = new CDFineStrategy();
        assertEquals(60, s.calculateFine(3));
    }

    
    @Test
    void testNoOverdue() {
        FineStrategy cdFine = new CDFineStrategy();
        FineStrategy bookFine = new BookFineStrategy();
        assertEquals(0, cdFine.calculateFine(0));
        assertEquals(0, bookFine.calculateFine(0));
    }
}
