package service;

import java.util.List;

import library.Book;
import library.loan;
import library.Member;

/**
 * Defines operations for managing books in the library.
 */
public interface BookService {

    void addBook(Book book);

    List<Book> searchBook(String keyword);

    List<Book> getAllBooks();

    /**
     * Borrow a book for a member for 28 days.
     */
    loan borrowBook(Book book, Member member) throws Exception;

    /**
     * Return an existing loan and apply any overdue fines.
     */
    void returnBook(loan loan) throws Exception;
}
