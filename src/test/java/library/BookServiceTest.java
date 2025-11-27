package library;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.BookServiceImpl;

public class BookServiceTest {

    private BookServiceImpl bookService;

    @BeforeEach
    void setup() {
        bookService = new BookServiceImpl();
    }

    @Test
    void testAddBook() {
        Book b = new Book("Java", "Oracle", "111");
        bookService.addBook(b);

        assertEquals(1, bookService.getAllBooks().size());
    }

    @Test
    void testSearchBookByTitle() {
        Book b1 = new Book("Java Programming", "Oracle", "111");
        Book b2 = new Book("Python Guide", "Google", "222");
        bookService.addBook(b1);
        bookService.addBook(b2);

        assertEquals(1, bookService.searchBook("java").size());
    }

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

        // create a loan that is overdue
        loan ln = new loan(b, m);
        m.addLoan(ln);

        // force overdue by modifying dueDate
        try {
            Field dueField = loan.class.getDeclaredField("dueDate");
            dueField.setAccessible(true);
            dueField.set(ln, LocalDate.now().minusDays(5));
        } catch (Exception ignored) {}

        assertThrows(Exception.class, () -> bookService.borrowBook(b, m),
                "User with overdue loans cannot borrow");
    }


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

        // نعمل overdue حقيقي بإنزال dueDate للخلف 3 أيام
        Field dueField = loan.class.getDeclaredField("dueDate");
        dueField.setAccessible(true);
        dueField.set(ln, LocalDate.now().minusDays(3));

        bookService.returnBook(ln);

        assertFalse(m.getFineBalance().compareTo(BigDecimal.ZERO) > 0,
                "Fine should be added for overdue loan");
    }
    @Test
    void testBookToString() {
        Book book = new Book("Networks", "Kurose", "B200");

        String text = book.toString();

        // 1) not null
        assertNotNull(text);

        // 2) يحتوي كل القيم
        assertTrue(text.contains("Networks"));
        assertTrue(text.contains("Kurose"));
        assertTrue(text.contains("B200"));

        // 3) التنسيق الصحيح 1:1
        assertEquals("Networks by Kurose (ISBN: B200)", text);

        // 4) يبدأ بالعنوان
        assertTrue(text.startsWith("Networks"));

        // 5) يتضمن كلمة "by"
        assertTrue(text.contains(" by "));
    }

}
