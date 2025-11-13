package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CD class (Sprint 5 – US5.1)
 */
public class CDTest {

    private CD cd;

    @BeforeEach
    void setUp() {
        cd = new CD("Top Hits", "DJ Mix", "CD01");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Top Hits", cd.getTitle());
        assertEquals("DJ Mix", cd.getArtist());
        assertEquals("CD01", cd.getId());
        assertTrue(cd.isAvailable());
    }

    @Test
    void testMarkBorrowed() {
        cd.markBorrowed();
        assertFalse(cd.isAvailable(), "CD should be marked as borrowed");
    }

    @Test
    void testMarkReturned() {
        cd.markBorrowed();
        cd.markReturned();
        assertTrue(cd.isAvailable(), "CD should be marked as available after return");
    }

    @Test
    void testToStringIncludesDetails() {
        String result = cd.toString();
        assertTrue(result.contains("Top Hits"));
        assertTrue(result.contains("DJ Mix"));
        assertTrue(result.contains("CD01"));
        assertTrue(result.contains("Available"));
    }
}
