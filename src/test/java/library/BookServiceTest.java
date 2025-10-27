package library;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.BookService;
import service.BookServiceImpl;

public class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl();
    }

    @Test
    void testAddBook() {
        Book book = new Book("Java Programming", "John Doe", "12345");
        bookService.addBook(book);

        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(1, allBooks.size(), "Should have 1 book after adding");
        assertEquals("Java Programming", allBooks.get(0).getTitle());
    }

    @Test
    void testAddDuplicateBook() {
        Book book1 = new Book("Java Basics", "Alice", "111");
        Book book2 = new Book("Java Basics", "Alice", "111");
        bookService.addBook(book1);
        bookService.addBook(book2); // depending on impl, this may be allowed

        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(2, allBooks.size(), "Should allow duplicate books unless restricted");
    }

    @Test
    void testSearchBookByTitle() {
        Book book1 = new Book("Java Basics", "Alice", "111");
        Book book2 = new Book("Advanced Java", "Bob", "222");
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> results = bookService.searchBook("Java");
        assertEquals(2, results.size(), "Should find 2 books with 'Java' in title");
    }

    @Test
    void testSearchBookByAuthor() {
        Book book1 = new Book("Java Basics", "Alice", "111");
        Book book2 = new Book("Python Basics", "Alice", "222");
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> results = bookService.searchBook("Alice");
        assertEquals(2, results.size(), "Should find 2 books by author Alice");
    }

    @Test
    void testSearchBookByISBN() {
        Book book = new Book("Java Basics", "Alice", "111");
        bookService.addBook(book);

        List<Book> results = bookService.searchBook("111");
        assertEquals(1, results.size(), "Should find 1 book with ISBN 111");
        assertEquals("Java Basics", results.get(0).getTitle());
    }

    @Test
    void testSearchBookNoResults() {
        Book book = new Book("Java", "Alice", "123");
        bookService.addBook(book);

        List<Book> results = bookService.searchBook("Python");
        assertEquals(0, results.size(), "Should return empty list if no match");
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Book 1", "Author 1", "001");
        Book book2 = new Book("Book 2", "Author 2", "002");
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(2, allBooks.size(), "Should return all 2 books");
    }

    @Test
    void testBookAvailability() {
        Book book = new Book("Test Book", "Author", "999");
        assertTrue(book.isAvailable(), "New book should be available");

        book.markBorrowed();
        assertFalse(book.isAvailable(), "Book should be borrowed");

        book.markReturned();
        assertTrue(book.isAvailable(), "Book should be available after return");
    }

    @Test
    void testEmptyBookListSearch() {
        List<Book> results = bookService.searchBook("Any");
        assertTrue(results.isEmpty(), "Search on empty list should return empty list");
    }
    
    @Test
    void testSetAvailableDirectly() {
        Book book = new Book("Direct Test", "Author", "555");
        book.setAvailable(false);
        assertFalse(book.isAvailable());

        book.setAvailable(true);
        assertTrue(book.isAvailable());
    }

    @Test
    void testToString() {
        Book book = new Book("String Test", "Author", "666");
        book.markBorrowed();
        String str = book.toString();
        assertTrue(str.contains("Borrowed"));
        assertTrue(str.contains("666"));
    }

    @Test
    void testBorrowBookSuccess() throws Exception {
        Book book = new Book("Test Book", "Author", "001");
        user user = new user("u1", "pass", "Alice");

        loan loan = bookService.borrowBook(book, user);

        assertNotNull(loan);
        assertFalse(book.isAvailable());
        assertEquals(loan, user.getLoans().get(0));
    }

    @Test
    void testBorrowBookNotAvailable() {
        Book book = new Book("Test Book", "Author", "002");
        user user = new user("u1", "pass", "Alice");
        book.markBorrowed();

        Exception ex = assertThrows(Exception.class, () -> bookService.borrowBook(book, user));
        assertEquals("Book not available", ex.getMessage());
    }

    @Test
    void testBorrowBookUserHasFine() {
        Book book = new Book("Test Book", "Author", "003");
        user user = new user("u1", "pass", "Alice");
        user.addFine(BigDecimal.valueOf(20));

        Exception ex = assertThrows(Exception.class, () -> bookService.borrowBook(book, user));
        assertEquals("User has unpaid fines", ex.getMessage());
    }

    @Test
    void testReturnBookNoOverdue() throws Exception {
        Book book = new Book("Book 1", "Author", "004");
        user user = new user("u1", "pass", "Alice");
        loan loan = new loan(book, user);

        book.markBorrowed();
        user.addLoan(loan);

        bookService.returnBook(loan);

        assertTrue(loan.isReturned());
        assertTrue(book.isAvailable());
        assertEquals(BigDecimal.ZERO, user.getFineBalance());
    }

 




    @Test
    void testReturnBookAlreadyReturned() throws Exception {
        Book book = new Book("Book 3", "Author", "006");
        user user = new user("u1", "pass", "Alice");
        loan loan = new loan(book, user);
        loan.setReturned(true);

        Exception ex = assertThrows(Exception.class, () -> bookService.returnBook(loan));
        assertEquals("Loan already returned", ex.getMessage());
    }

    
}
