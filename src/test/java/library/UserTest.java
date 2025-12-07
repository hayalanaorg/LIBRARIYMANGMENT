package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link user} class.
 *
 * <p>This test suite verifies all core behaviors of the {@code user} class,
 * including construction, getters, admin flag logic, activation status,
 * and string formatting. Edge cases and multiple-user independence are
 * also evaluated to ensure correctness and reliability.</p>
 *
 * <h2>Test Coverage:</h2>
 * <ul>
 *     <li>Constructor values and getter methods</li>
 *     <li>Admin vs non-admin users</li>
 *     <li>User activation and deactivation behavior</li>
 *     <li>toString() format correctness</li>
 *     <li>Handling of empty-string edge cases</li>
 *     <li>Independence of multiple user objects</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-12-07
 */
public class UserTest {

    /**
     * Ensures that constructor parameters are stored correctly and getters return
     * expected values for a normal user.
     */
    @Test
    void testConstructorAndGetters() {
        user u = new user("10", "lana", "1234", "Lana Omar", false);

        assertEquals("10", u.getId());
        assertEquals("lana", u.getUsername());
        assertEquals("1234", u.getPassword());
        assertEquals("Lana Omar", u.getFullName());
        assertFalse(u.isAdmin(), "User should not be admin");
        assertTrue(u.isActive(), "User should be active when created");
    }

    /**
     * Verifies that admin users are correctly marked as admin and retain their data.
     */
    @Test
    void testAdminUser() {
        user admin = new user("1", "admin", "pass", "System Admin", true);

        assertTrue(admin.isAdmin());
        assertEquals("System Admin", admin.getFullName());
    }

    /**
     * Tests the deactivate() method, ensuring the user becomes inactive afterward.
     */
    @Test
    void testDeactivate() {
        user u = new user("3", "test", "pass", "Test User", false);

        assertTrue(u.isActive());
        u.deactivate();
        assertFalse(u.isActive(), "User should be inactive after deactivate()");
    }

    /**
     * Ensures toString() follows the proper format and includes username
     * and full name correctly.
     */
    @Test
    void testToStringFormat() {
        user u = new user("4", "noor", "pass", "Noor Ahmad", false);

        String text = u.toString();

        assertNotNull(text);
        assertTrue(text.contains("Noor Ahmad"));
        assertTrue(text.contains("noor"));
        assertEquals("Noor Ahmad (noor)", text);
    }

    /**
     * Tests edge-case behavior when constructor fields are empty strings.
     */
    @Test
    void testEdgeCases() {
        user u = new user("", "", "", "", false);

        assertEquals("", u.getId());
        assertEquals("", u.getUsername());
        assertEquals("", u.getPassword());
        assertEquals("", u.getFullName());
        assertFalse(u.isAdmin());
        assertTrue(u.isActive());
        assertEquals(" ()", u.toString());
    }

    /**
     * Verifies that two distinct user objects maintain separate state
     * and do not interfere with each other.
     */
    @Test
    void testMultipleUsersIndependence() {
        user u1 = new user("1", "lana", "pass1", "Lana", false);
        user u2 = new user("2", "moh", "pass2", "Mohammed", false);

        assertNotEquals(u1.getUsername(), u2.getUsername());
        assertNotEquals(u1.getId(), u2.getId());
    }
}
