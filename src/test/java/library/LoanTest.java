package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LoanTest {

    @Test
    public void testNotOverdueInitially() {
        Book book = new Book("Test", "Author", "ISBN");
        user user = new user("u1", "pass", "Alice");
        loan loan = new loan(book, user);

        loan.setReturned(false);
        assertFalse(loan.isOverdue(), "Loan should not be overdue initially");
        assertEquals(0, loan.overdueDays(), "Overdue days should be 0 initially");
    }

    @Test
    public void testReturnedBookNotOverdue() {
        Book book = new Book("Test", "Author", "ISBN");
        user user = new user("u1", "pass", "Alice");
        loan loan = new loan(book, user);

        loan.setReturned(true);
        assertFalse(loan.isOverdue(), "Returned book should not be overdue");
        assertEquals(0, loan.overdueDays(), "Overdue days should be 0 for returned book");
    }

    @Test
    public void testOverdueBook() {
        Book book = new Book("Test", "Author", "ISBN");
        user user = new user("u1", "pass", "Alice");

        // استخدمنا constructor الجديد لتحديد borrowDate و dueDate في الماضي
        LocalDate borrowDate = LocalDate.now().minusDays(10);
        LocalDate dueDate = LocalDate.now().minusDays(5);
        loan loan = new loan(book, user, borrowDate, dueDate);

        loan.setReturned(false);

        assertTrue(loan.isOverdue(), "Book should be overdue");
        assertEquals(5, loan.overdueDays(), "Overdue days should be 5");
    }

    @Test
    public void testGetters() {
        Book book = new Book("Test", "Author", "ISBN");
        user user = new user("u1", "pass", "Alice");

        loan loan = new loan(book, user);

        assertEquals(book, loan.getBook(), "getBook should return the correct book");
        assertEquals(user, loan.getUser(), "getUser should return the correct user");
        assertNotNull(loan.getBorrowDate(), "Borrow date should not be null");
        assertNotNull(loan.getDueDate(), "Due date should not be null");
        assertFalse(loan.isReturned(), "Loan should not be returned initially");
    }
}
