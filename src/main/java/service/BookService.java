package service;
import library.Book;
import library.loan;
import library.user;

import java.util.List;

/**
 * Defines operations for managing books in the library.
 */
public interface BookService {
    void addBook(Book book);
    List<Book> searchBook(String keyword);
    List<Book> getAllBooks();
    
    loan borrowBook(Book book, user user) throws Exception;
    void returnBook(loan loan) throws Exception;
    
    
}

