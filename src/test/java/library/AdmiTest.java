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

public class AdmiTest {

    private AdminServiceImpl admin;

    @BeforeEach
    void setUp() {
        admin = new AdminServiceImpl();
    }

    // ============================================================
    // LOGIN / LOGOUT
    // ============================================================

    @Test
    void testLoginSuccess() {
        assertTrue(admin.login("admin", "1234"));
        assertNotNull(admin.getCurrentAdmin());
    }

    @Test
    void testLoginFail() {
        assertFalse(admin.login("x", "y"));
        assertFalse(admin.isLoggedIn());
    }

    @Test
    void testLogout() {
        admin.login("admin", "1234");
        admin.logout();
        assertFalse(admin.isLoggedIn());
    }

    // ============================================================
    // ADD / FIND USERS
    // ============================================================

    @Test
    void testAddUserAndFindUser() {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        assertEquals(m, admin.findUser("lana"));
    }

    @Test
    void testFindUserNotFound() {
        assertNull(admin.findUser("unknown"));
    }

    // ============================================================
    // ADD / REMOVE BOOKS
    // ============================================================

    @Test
    void testAddBook() {
        Book b = new Book("Java", "Oracle", "111");
        admin.addBook(b);

        assertTrue(admin.getBooks().contains(b));
    }

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

    @Test
    void testUnregisterUserSuccess() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        admin.unregisterUser(m);

        assertFalse(m.isActive());
        assertNull(admin.findUser("lana"));
    }

    @Test
    void testUnregisterUserFailsNotLoggedIn() {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    @Test
    void testUnregisterUserFailsNotFound() {
        admin.login("admin", "1234");
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    @Test
    void testUnregisterUserFailsAdminUser() {
        admin.login("admin", "1234");
        Admin a = new Admin("A1", "admin2", "pass", "Admin Two");
        admin.addUser(a);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(a));
    }

    @Test
    void testUnregisterUserFailsActiveLoan() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);
        m.addLoan(ln);

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    @Test
    void testUnregisterUserFailsUnpaidFine() {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        m.addFine(new BigDecimal("10"));

        assertThrows(IllegalStateException.class, () -> admin.unregisterUser(m));
    }

    @Test
    void testUnregisterUserReturnedLoanAllowed() throws Exception {
        admin.login("admin", "1234");

        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, m);

     
        ln.isReturned2();  

        m.addLoan(ln);

        admin.unregisterUser(m);

        assertFalse(m.isActive());
    }


    // ============================================================
    // SEND REMINDERS — OBSERVER PATTERN
    // ============================================================

    @Test
    void testSendRemindersTriggersObserver() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
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

    @Test
    void testSendRemindersNoOverdue() {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        admin.addUser(m);

        Observer obs = Mockito.mock(Observer.class);
        admin.addObserver(obs);

        admin.sendReminders();

        Mockito.verify(obs, Mockito.never()).notify(Mockito.any(), anyString());
    }

    @Test
    void testMultipleObservers() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
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

    @Test
    void testRemoveObserver() throws Exception {
        Member m = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
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
    
    @Test
    void testToStringFormat() {
        Admin admin = new Admin("1", "admin", "1234", "Lana Admin");

        String text = admin.toString();

        assertNotNull(text, "toString() should not return null");
        assertTrue(text.startsWith("Admin: "), "toString() must start with 'Admin: '");
        assertTrue(text.contains("Lana Admin"), "toString() should contain the admin's full name");
        assertEquals("Admin: Lana Admin", text);
    }
}
