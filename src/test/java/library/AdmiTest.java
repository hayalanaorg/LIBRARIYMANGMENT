package library;
import service.AdminService;
import service.AdminServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;

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
    
    @Test
    void testAddAndRemoveBook() {
        adminService.login("admin", "1234");
        Book book = new Book("Test Book", "Author", "001");
        adminService.addBook(book);
        assertEquals(1, ((AdminServiceImpl) adminService).getBooks().size());

        adminService.removeBook(book);
        assertEquals(0, ((AdminServiceImpl) adminService).getBooks().size());
    }

    @Test
    void testUnregisterUser() {
        adminService.login("admin", "1234");
        user u = new user("u1", "pass", "Alice");
        ((AdminServiceImpl) adminService).addUser(u);

        adminService.unregisterUser(u);
        assertEquals(0, ((AdminServiceImpl) adminService).getUsers().size());
    }

    @Test
    void testUnregisterNonExistingUser() {
        adminService.login("admin", "1234");
        user u = new user("u2", "pass", "Bob");

        assertThrows(IllegalStateException.class, () -> adminService.unregisterUser(u));
    }
    @Test
    void testAuthenticate() {
        Admin admin = new Admin("admin", "1234", "Library Admin");

        // كلمة المرور صحيحة
        assertTrue(admin.authenticate("1234"));

        // كلمة المرور خاطئة
        assertFalse(admin.authenticate("wrongpass"));
    }

    @Test
    void testSendReminder() {
        // Arrange
    	EmailMessage emailMock = mock(EmailMessage.class);
        AdminServiceImpl adminService = new AdminServiceImpl(emailMock);

        user u1 = new user("u1", "123", "Alice");
        Book b = new Book("Java", "Author", "001");
        loan l = new loan(b, u1, LocalDate.now().minusDays(30), LocalDate.now().minusDays(5));
        u1.addLoan(l);
        adminService.addUser(u1);

        // Act
        adminService.sendReminders();

        // Assert
        verify(emailMock).sendEmail(u1, "You have 1 overdue book(s).");
        verifyNoMoreInteractions(emailMock);
    }
    
}
