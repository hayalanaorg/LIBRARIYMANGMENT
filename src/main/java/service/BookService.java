package service;
import library.Book;
import java.util.List;

/**
 * Defines operations for managing books in the library.
 */
public interface BookService {
    void addBook(Book book);
    List<Book> searchBook(String keyword);
    List<Book> getAllBooks();
}

