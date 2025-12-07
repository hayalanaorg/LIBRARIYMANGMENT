package library;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.BookServiceImpl;

/**
 * Test suite for {@link BookServiceImpl}, covering:
 * <ul>
 *     <li>Book registration and searching</li>
 *     <li>Borrowing logic and business rules</li>
 *     <li>Return logic and overdue fine calculation</li>
 *     <li>Error handling for invalid inputs</li>
 * </ul>
 *
 * <p>
 * These tests validate Sprint 1 (book management),
 * Sprint 2–3 (borrowing rules),
 * and Sprint 4 (fine application).
 * </p>
 *
 * @author
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class BookServiceTest {

    private BookServiceImpl bookService;

    /**
     * Initializes a fresh {@link BookServiceImpl} before each test.
     */
    @BeforeEach
    void setup() {
        bookService = new BookServiceImpl();
    }

    // ============================================================
    // ADD BOOK TESTS
    // ============================================================

    /** Ensures books are correctly added to the internal list. */
    @Test
    void testAddBook() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertEquals(1, bookService.getAllBooks().size());
    }

    // ============================================================
    // SEARCH BOOK TESTS
    // ============================================================

    /** Searches by title and verifies matching results. */
    @Test
    void testSearchBookByTitle() {
        Book b1 = new Book("Java Programming", "Oracle", "111");
        Book b2 = new Book("Python Guide", "Google", "222");
        bookService.addBook(b1);
        bookService.addBook(b2);

        assertEquals(1, bookService.searchBook("java").size());
    }

    /** Searches by author name. */
    @Test
    void testSearchBookByAuthor() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertEquals(1, bookService.searchBook("oracle").size());
    }

    /** Searches by ISBN number. */
    @Test
    void testSearchBookByIsbn() {
        Book b = new Book("Networks", "Kurose", "B200");
        bookService.addBook(b);

        assertEquals(1, bookService.searchBook("b200").size());
    }

    /** Ensures null keyword behaves like empty search term. */
    @Test
    void testSearchBookWithNullKeyword() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        List<Book> result = bookService.searchBook(null);

        assertEquals(1, result.size());
    }

    // ============================================================
    // BORROW BOOK TESTS
    // ============================================================

    /** Tests a valid borrowing operation. */
    @Test
    void testBorrowBookSuccess() throws Exception {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        loan ln = bookService.borrowBook(b, m);

        assertNotNull(ln);
        assertEquals(m, ln.getUser());
        assertFalse(b.isAvailable());
    }

    /** Ensures a book cannot be borrowed twice. */
    @Test
    void testBorrowBookNotAvailable() throws Exception {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m1 = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        Member m2 = new Member("2", "sara", "pass", "Sara", "sara@mail.com");

        bookService.borrowBook(b, m1);

        assertThrows(Exception.class, () -> {
            bookService.borrowBook(b, m2);
        });
    }

    /** Ensures a user with unpaid fines cannot borrow. */
    @Test
    void testBorrowBookUserHasUnpaidFine() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        m.addFine(new BigDecimal("20"));

        assertThrows(Exception.class, () -> bookService.borrowBook(b, m));
    }

    /** Ensures a user with overdue loans cannot borrow new books. */
    @Test
    void testBorrowBookUserHasOverdueLoan() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        loan ln = new loan(b, m);
        m.addLoan(ln);

        try {
            Field dueField = loan.class.getDeclaredField("dueDate");
            dueField.setAccessible(true);
            dueField.set(ln, LocalDate.now().minusDays(5));
        } catch (Exception ignored) {}

        assertThrows(Exception.class, () -> bookService.borrowBook(b, m),
                "User with overdue loans cannot borrow");
    }

    /** Ensures null book parameter throws an exception. */
    @Test
    void testBorrowBookNullBook() {
        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        assertThrows(IllegalArgumentException.class,
                () -> bookService.borrowBook(null, m));
    }

    /** Ensures null member parameter throws an exception. */
    @Test
    void testBorrowBookNullMember() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertThrows(IllegalArgumentException.class,
                () -> bookService.borrowBook(b, null));
    }

    // ============================================================
    // RETURN BOOK TESTS
    // ============================================================

    /** Verifies returning a book restores availability and marks loan returned. */
    @Test
    void testReturnBookSuccess() throws Exception {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        loan ln = bookService.borrowBook(b, m);

        assertFalse(ln.isReturned());

        bookService.returnBook(ln);

        assertTrue(ln.isReturned());
        assertTrue(b.isAvailable());
    }

    /** Ensures overdue returns correctly add a fine. */
    @Test
    void testReturnBookOverdueAddsFine() throws Exception {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        loan ln = bookService.borrowBook(b, m);

        Field dueField = loan.class.getDeclaredField("dueDate");
        dueField.setAccessible(true);
        dueField.set(ln, LocalDate.now().minusDays(3)); // 3 days overdue

        bookService.returnBook(ln);

        assertTrue(m.getFineBalance().compareTo(BigDecimal.ZERO) > 0,
                "Fine should apply for overdue loans");
    }

    /** Ensures returning a loan twice throws an exception. */
    @Test
    void testReturnBookAlreadyReturned() throws Exception {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        loan ln = bookService.borrowBook(b, m);

        ln.setReturned(true);

        assertThrows(Exception.class, () -> {
            bookService.returnBook(ln);
        });
    }

    /** Ensures null loan parameter throws an exception. */
    @Test
    void testReturnBookNullLoan() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.returnBook(null);
        });
    }

    // ============================================================
    // BOOK toString TEST
    // ============================================================

    /** Tests the format of Book.toString() for correctness. */
    @Test
    void testBookToString() {
        Book book = new Book("Networks", "Kurose", "B200");

        String text = book.toString();

        assertNotNull(text);
        assertTrue(text.contains("Networks"));
        assertTrue(text.contains("Kurose"));
        assertTrue(text.contains("B200"));
        assertEquals("Networks by Kurose (ISBN: B200)", text);
    }
}
