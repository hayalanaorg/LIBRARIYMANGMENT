package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Role} enum.
 *
 * <p>This test class ensures that the {@code Role} enumeration
 * is correctly defined, contains the expected values, and produces
 * the correct string representations.</p>
 *
 * <h2>Covered Scenarios:</h2>
 * <ul>
 *     <li>Enum contains exactly two values</li>
 *     <li>ADMIN value exists</li>
 *     <li>SUPER_ADMIN value exists</li>
 *     <li>toString() returns the correct name</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-12-07
 */
public class RoleTest {

    /**
     * Ensures the Role enum contains exactly 2 values.
     */
    @Test
    void testRoleValuesCount() {
        Role[] roles = Role.values();
        assertEquals(2, roles.length, "Role enum must contain exactly 2 values");
    }

    /**
     * Verifies that the enum includes the ADMIN role.
     */
    @Test
    void testRoleContainsAdmin() {
        assertNotNull(Role.valueOf("ADMIN"), "Role enum should contain ADMIN");
    }

    /**
     * Verifies that the enum includes the SUPER_ADMIN role.
     */
    @Test
    void testRoleContainsSuperAdmin() {
        assertNotNull(Role.valueOf("SUPER_ADMIN"), "Role enum should contain SUPER_ADMIN");
    }

    /**
     * Ensures that toString() correctly returns the exact enum name.
     */
    @Test
    void testRoleToString() {
        assertEquals("ADMIN", Role.ADMIN.toString());
        assertEquals("SUPER_ADMIN", Role.SUPER_ADMIN.toString());
    }
}
