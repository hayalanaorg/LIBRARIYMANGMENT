package library;

import service.AdminServiceImpl;
import service.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test suite for {@link AdminServiceImpl}, verifying:
 * <ul>
 *     <li>Login and logout behavior</li>
 *     <li>User registration and unregistration rules</li>
 *     <li>Book management</li>
 *     <li>Observer (notification) logic</li>
 *     <li>Admin role customization</li>
 * </ul>
 *
 * <p>
 * These tests ensure correct system behavior across Sprints 1–5
 * (authentication, book management, observer pattern, user removal rules).
 * </p>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class AdmiTest {

    private AdminServiceImpl admin;

    /**
     * Initializes a fresh {@link AdminServiceImpl} before each test.
     */
    @BeforeEach
    void setUp() {
        admin = new AdminServiceImpl();
    }

    // ============================================================
    // LOGIN / LOGOUT
    // ============================================================

    /** Verifies successful admin login with correct credentials. */
    @Test
    void testLoginSuccess() {
        assertTrue(admin.login("admin", "1234"));
        assertNotNull(admin.getCurrentAdmin());
    }

    /** Ensures login fails with invalid credentials. */
    @Test
    void testLoginFail() {
        assertFalse(admin.login("x", "y"));
        assertFalse(admin.isLoggedIn());
    }

    /** Ensures logout clears the admin session. */
    @Test
    void testLogout() {
        admin.login("admin", "1234");
        admin.logout();
        assertFalse(admin.isLoggedIn());
    }

    // ============================================================
    // ADD / FIND USERS
    // ============================================================

    /** Verifies that added users can be retrieved using findUser(). */
    @Test
    void testAddUserAndFindUser() {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        assertEquals(m, admin.findUser("lana"));
    }

    /** Ensures searching for an unknown user returns null. */
    @Test
    void testFindUserNotFound() {
        assertNull(admin.findUser("unknown"));
    }

    // ============================================================
    // ADD / REMOVE BOOKS
    // ============================================================

    /** Ensures books are successfully added to the collection. */
    @Test
    void testAddBook() {
        Book b = new Book("Java", "Oracle", "111");
        admin.addBook(b);

        assertTrue(admin.getBooks().contains(b));
    }

    /** Ensures books can be removed. */
    @Test
    void testRemoveBook() {
        Book b = new Book("Java", "Oracle", "111");
        admin.addBook(b);
        admin.removeBook(b);

        assertFalse(admin.getBooks().contains(b));
    }

    // ============================================================
    // UNREGISTER USER CASES
    // ============================================================

    /** Verifies successful unregistration of a normal member. */
    @Test
    void testUnregisterUserSuccess() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        admin.unregisterUser(m);

        assertFalse(m.isActive());
        assertNull(admin.findUser("lana"));
    }

    /** Ensures unregistration fails when admin is not logged in. */
    @Test
    void testUnregisterUserFailsNotLoggedIn() {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    /** Ensures unregistration fails if user is not registered. */
    @Test
    void testUnregisterUserFailsNotFound() {
        admin.login("admin", "1234");
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    /** Ensures admin users cannot be unregistered. */
    @Test
    void testUnregisterUserFailsAdminUser() {
        admin.login("admin", "1234");
        Admin a = new Admin("A1", "admin2", "pass", "Admin Two");
        admin.addUser(a);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(a));
    }

    /** Ensures unregistration fails if member has active loans. */
    @Test
    void testUnregisterUserFailsActiveLoan() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);
        m.addLoan(ln);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    /** Ensures unregistration fails if member has unpaid fines. */
    @Test
    void testUnregisterUserFailsUnpaidFine() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        m.addFine(new BigDecimal("10"));

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    /** Ensures unregistration works if loan is already returned. */
    @Test
    void testUnregisterUserReturnedLoanAllowed() throws Exception {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);

        ln.isReturned2();  // Mark as returned

        m.addLoan(ln);

        admin.unregisterUser(m);

        assertFalse(m.isActive());
    }

    // ============================================================
    // SEND REMINDERS — OBSERVER PATTERN
    // ============================================================

    /** Ensures observers are notified when member has overdue loans. */
    @Test
    void testSendRemindersTriggersObserver() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);
        m.addLoan(ln);

        // Force overdue
        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, ln.getBorrowDate().minusDays(1));

        Observer mockObserver = Mockito.mock(Observer.class);
        admin.addObserver(mockObserver);

        admin.sendReminders();

        Mockito.verify(mockObserver, Mockito.times(1))
                .notify(eq(m), anyString());
    }

    /** Ensures observers are not notified if there is no overdue. */
    @Test
    void testSendRemindersNoOverdue() {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Observer obs = Mockito.mock(Observer.class);
        admin.addObserver(obs);

        admin.sendReminders();

        Mockito.verify(obs, Mockito.never()).notify(Mockito.any(), anyString());
    }

    /** Ensures all registered observers receive reminder notifications. */
    @Test
    void testMultipleObservers() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);
        m.addLoan(ln);

        // overdue
        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, ln.getBorrowDate().minusDays(1));

        Observer obs1 = Mockito.mock(Observer.class);
        Observer obs2 = Mockito.mock(Observer.class);

        admin.addObserver(obs1);
        admin.addObserver(obs2);

        admin.sendReminders();

        Mockito.verify(obs1, Mockito.times(1)).notify(eq(m), anyString());
        Mockito.verify(obs2, Mockito.times(1)).notify(eq(m), anyString());
    }

    /** Ensures removed observers do not receive notifications. */
    @Test
    void testRemoveObserver() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "s12218543@stu.najah.edu");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);
        m.addLoan(ln);

        // overdue
        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, ln.getBorrowDate().minusDays(1));

        Observer obs = Mockito.mock(Observer.class);

        admin.addObserver(obs);
        admin.removeObserver(obs);

        admin.sendReminders();

        Mockito.verify(obs, Mockito.never()).notify(Mockito.any(), anyString());
    }

    // ============================================================
    // ADMIN toString()
    // ============================================================

    /** Tests Admin.toString() format. */
    @Test
    void testToStringFormat() {
        Admin admin = new Admin("1", "admin", "1234", "Lana Admin");

        String text = admin.toString();

        assertNotNull(text);
        assertTrue(text.startsWith("Admin: "));
        assertTrue(text.contains("Lana Admin"));
        assertEquals("Admin: Lana Admin", text);
    }

    // ============================================================
    // ADMIN ROLE TESTS
    // ============================================================

    /** Ensures default admin role = ADMIN. */
    @Test
    void testDefaultAdminRole() {
        Admin a = new Admin("A1", "admin", "1234", "Lana Admin");

        assertEquals("ADMIN", a.getRole());
    }

    /** Ensures admin role can be changed to SUPER_ADMIN. */
    @Test
    void testSetAdminRole() {
        Admin a = new Admin("A1", "admin", "1234", "Lana Admin");

        a.setRole("SUPER_ADMIN");

        assertEquals("SUPER_ADMIN", a.getRole());
    }

    /** Ensures custom role strings can be assigned. */
    @Test
    void testSetRoleToCustomValue() {
        Admin a = new Admin("A1", "admin", "1234", "Lana Admin");

        a.setRole("MANAGER");

        assertEquals("MANAGER", a.getRole());
    }
}
