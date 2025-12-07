package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CDTest {

    private CD cd;

    
    @BeforeEach
    void setup() {
        cd = new CD("Best Hits", "Taylor Swift");
    }

    // ============================================
    // BASIC CONSTRUCTOR TESTS
    // ============================================

    
    @Test
    void testConstructor() {
        assertEquals("Best Hits", cd.getTitle());
        assertEquals("Taylor Swift", cd.getArtist());
        assertTrue(cd.isAvailable());
    }

   
    @Test
    void testConstructorNotNull() {
        assertNotNull(cd.getTitle());
        assertNotNull(cd.getArtist());
    }

    @Test
    void testConstructorEdgeCases() {
        CD empty = new CD("", "");
        assertEquals("", empty.getTitle());
        assertEquals("", empty.getArtist());

        CD spaces = new CD("   ", "   ");
        assertEquals("   ", spaces.getTitle());
        assertEquals("   ", spaces.getArtist());
    }

    // ============================================
    // AVAILABILITY LOGIC
    // ============================================

   
    @Test
    void testInitialAvailability() {
        assertTrue(cd.isAvailable(), "CD should be available when created");
    }

   
    @Test
    void testMarkBorrowed() {
        cd.markBorrowed();
        assertFalse(cd.isAvailable(), "CD should not be available after borrowing");
    }

    @Test
    void testBorrowTwice() {
        cd.markBorrowed();
        cd.markBorrowed();
        assertFalse(cd.isAvailable());
    }

    @Test
    void testMarkReturned() {
        cd.markBorrowed();
        cd.markReturned();
        assertTrue(cd.isAvailable(), "CD should be available again after return");
    }

    @Test
    void testReturnBeforeBorrow() {
        cd.markReturned();
        assertTrue(cd.isAvailable());
    }


    @Test
    void testBorrowReturnBorrowCycle() {
        cd.markBorrowed();
        cd.markReturned();
        cd.markBorrowed();
        assertFalse(cd.isAvailable());
    }

    // ============================================
    // TO STRING
    // ============================================


    @Test
    void testToString() {
        String text = cd.toString();
        assertTrue(text.contains("Best Hits"));
        assertTrue(text.contains("Taylor Swift"));
        assertTrue(text.contains("CD"));
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(cd.toString());
    }

    // ============================================
    // TYPE TEST / INSTANCE
    // ============================================

  
    @Test
    void testInstanceType() {
        assertTrue(cd instanceof CD);
    }
}
