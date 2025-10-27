package library;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	    void testGetAllBooks() {
	        Book book1 = new Book("Book 1", "Author 1", "001");
	        Book book2 = new Book("Book 2", "Author 2", "002");
	        bookService.addBook(book1);
	        bookService.addBook(book2);

	        List<Book> allBooks = bookService.getAllBooks();
	        assertEquals(2, allBooks.size(), "Should return all 2 books");
	    }
}
