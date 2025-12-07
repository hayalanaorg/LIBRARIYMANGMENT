package library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class UserTest {


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



    @Test
    void testAdminUser() {
        user admin = new user("1", "admin", "pass", "System Admin", true);

        assertTrue(admin.isAdmin());
        assertEquals("System Admin", admin.getFullName());
    }

  
    @Test
    void testDeactivate() {
        user u = new user("3", "test", "pass", "Test User", false);

        assertTrue(u.isActive());
        u.deactivate();
        assertFalse(u.isActive(), "User should be inactive after deactivate()");
    }


    @Test
    void testToStringFormat() {
        user u = new user("4", "noor", "pass", "Noor Ahmad", false);

        String text = u.toString();

        assertNotNull(text);
        assertTrue(text.contains("Noor Ahmad"));
        assertTrue(text.contains("noor"));
        assertEquals("Noor Ahmad (noor)", text);
    }


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


    @Test
    void testMultipleUsersIndependence() {
        user u1 = new user("1", "lana", "pass1", "Lana", false);
        user u2 = new user("2", "moh", "pass2", "Mohammed", false);

        assertNotEquals(u1.getUsername(), u2.getUsername());
        assertNotEquals(u1.getId(), u2.getId());
    }
}
