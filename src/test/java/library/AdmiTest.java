package library;
import service.AdminService;
import service.AdminServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AdmiTest {

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminServiceImpl();
    }

    @Test
    void testValidLogin() {
        boolean result = adminService.login("admin", "1234");
        assertTrue(result, "Admin should be able to login with correct credentials");
        assertNotNull(adminService.getCurrentAdmin(), "Current admin should not be null after login");
    }

    @Test
    void testInvalidLogin() {
        boolean result = adminService.login("wrongUser", "wrongPass");
        assertFalse(result, "Admin should not login with incorrect credentials");
        assertNull(adminService.getCurrentAdmin(), "Current admin should be null after failed login");
    }

    @Test
    void testLogout() {
        adminService.login("admin", "1234");
        assertTrue(adminService.isLoggedIn(), "Admin should be logged in after successful login");

        adminService.logout();
        assertFalse(adminService.isLoggedIn(), "Admin should be logged out after logout");
        assertNull(adminService.getCurrentAdmin(), "Current admin should be null after logout");
    }
}
