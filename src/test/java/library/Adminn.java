package library;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Adminn {

    @Test
    public void testLoginSuccess() {
        Admin admin = new Admin("admin", "1234");

        boolean result = admin.login("admin", "1234");

        assertTrue(result, "Login should succeed with correct credentials");
        assertTrue(admin.isLoggedIn(), "Admin should be marked as logged in");
    }

    @Test
    public void testLoginFail() {
        Admin admin = new Admin("admin", "1234");

        boolean result = admin.login("wrongUser", "wrongPass");

        assertFalse(result, "Login should fail with incorrect credentials");
        assertFalse(admin.isLoggedIn(), "Admin should not be logged in");
    }

    @Test
    public void testLogout() {
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");
        assertTrue(admin.isLoggedIn(), "Should be logged in before logout");

        admin.logout();

        assertFalse(admin.isLoggedIn(), "Should not be logged in after logout");
    }
}

