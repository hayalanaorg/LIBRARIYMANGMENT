package library;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.BookServiceImpl;

public class BookServiceTest {

    private BookServiceImpl bookService;

    @BeforeEach
    void setup() {
        bookService = new BookServiceImpl();
    }

    // ============================================================
    // ADD BOOK TESTS
    // ============================================================

    @Test
    void testAddBook() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertEquals(1, bookService.getAllBooks().size());
    }

    // ============================================================
    // SEARCH BOOK TESTS
    // ============================================================

    @Test
    void testSearchBookByTitle() {
        Book b1 = new Book("Java Programming", "Oracle", "111");
        Book b2 = new Book("Python Guide", "Google", "222");
        bookService.addBook(b1);
        bookService.addBook(b2);

        assertEquals(1, bookService.searchBook("java").size());
    }

    @Test
    void testSearchBookByAuthor() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertEquals(1, bookService.searchBook("oracle").size());
    }

    @Test
    void testSearchBookByIsbn() {
        Book b = new Book("Networks", "Kurose", "B200");
        bookService.addBook(b);

        assertEquals(1, bookService.searchBook("b200").size());
    }

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

    @Test
    void testBorrowBookUserHasUnpaidFine() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        m.addFine(new BigDecimal("20"));

        assertThrows(Exception.class, () -> bookService.borrowBook(b, m));
    }

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

    @Test
    void testBorrowBookNullBook() {
        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        assertThrows(IllegalArgumentException.class,
                () -> bookService.borrowBook(null, m));
    }

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

    @Test
    void testReturnBookNullLoan() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.returnBook(null);
        });
    }

    // ============================================================
    // BOOK toString TEST
    // ============================================================

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
