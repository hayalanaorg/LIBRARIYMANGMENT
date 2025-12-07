package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CD} class.
 *
 * <p>This test suite validates:</p>
 * <ul>
 *     <li>Correct construction of CD objects</li>
 *     <li>Availability state transitions (borrow / return)</li>
 *     <li>Edge cases such as empty strings and multiple borrow cycles</li>
 *     <li>Proper functioning of {@code toString()}</li>
 *     <li>Correct CD instance type recognition</li>
 * </ul>
 *
 * <p>These tests ensure CD behavior remains stable and predictable
 * throughout Sprint 5 requirements.</p>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class CDTest {

    private CD cd;

    /**
     * Creates a sample CD instance before each test case.
     */
    @BeforeEach
    void setup() {
        cd = new CD("Best Hits", "Taylor Swift");
    }

    // ============================================
    // BASIC CONSTRUCTOR TESTS
    // ============================================

    /**
     * Verifies that a newly created CD stores title, artist,
     * and starts as available.
     */
    @Test
    void testConstructor() {
        assertEquals("Best Hits", cd.getTitle());
        assertEquals("Taylor Swift", cd.getArtist());
        assertTrue(cd.isAvailable());
    }

    /**
     * Ensures constructor values are not null.
     */
    @Test
    void testConstructorNotNull() {
        assertNotNull(cd.getTitle());
        assertNotNull(cd.getArtist());
    }

    /**
     * Tests constructor behavior with edge-case strings.
     */
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

    /**
     * Ensures CDs begin in an available state.
     */
    @Test
    void testInitialAvailability() {
        assertTrue(cd.isAvailable(), "CD should be available when created");
    }

    /**
     * Verifies that borrowing correctly marks CD unavailable.
     */
    @Test
    void testMarkBorrowed() {
        cd.markBorrowed();
        assertFalse(cd.isAvailable(), "CD should not be available after borrowing");
    }

    /**
     * Ensures borrowing multiple times doesn't break state.
     */
    @Test
    void testBorrowTwice() {
        cd.markBorrowed();
        cd.markBorrowed();
        assertFalse(cd.isAvailable());
    }

    /**
     * Ensures returning a borrowed CD restores availability.
     */
    @Test
    void testMarkReturned() {
        cd.markBorrowed();
        cd.markReturned();
        assertTrue(cd.isAvailable(), "CD should be available again after return");
    }

    /**
     * Returning before borrowing should leave CD available.
     */
    @Test
    void testReturnBeforeBorrow() {
        cd.markReturned();
        assertTrue(cd.isAvailable());
    }

    /**
     * Tests multiple borrow-return cycles.
     */
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

    /**
     * Ensures the string representation contains important information.
     */
    @Test
    void testToString() {
        String text = cd.toString();
        assertTrue(text.contains("Best Hits"));
        assertTrue(text.contains("Taylor Swift"));
        assertTrue(text.contains("CD"));
    }

    /**
     * Ensures {@code toString()} never returns null.
     */
    @Test
    void testToStringNotNull() {
        assertNotNull(cd.toString());
    }

    // ============================================
    // TYPE TEST / INSTANCE
    // ============================================

    /**
     * Ensures the tested object is a CD instance.
     */
    @Test
    void testInstanceType() {
        assertTrue(cd instanceof CD);
    }
}
