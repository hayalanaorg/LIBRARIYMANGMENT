package service;

import java.util.List;

import library.Book;
import library.loan;
import library.Member;

/**
 * Service interface defining all book-related operations in the library system.
 * <p>
 * This abstraction isolates book management logic, such as:
 * searching, borrowing, returning, and applying fines for overdue items.
 * </p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *     <li>Adding books to the collection</li>
 *     <li>Searching books by keywords</li>
 *     <li>Borrowing books (creates a {@link loan})</li>
 *     <li>Returning books and applying overdue fines</li>
 * </ul>
 *
 * <h3>Borrowing Rules:</h3>
 * <ul>
 *     <li>Borrow duration: <b>28 days</b></li>
 *     <li>A member cannot borrow if they have overdue or unpaid fines</li>
 * </ul>
 *
 * <h3>Throws:</h3>
 * <ul>
 *     <li>{@link Exception} when borrowing or returning fails</li>
 * </ul>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public interface BookService {

    /**
     * Adds a new book to the library's collection.
     *
     * @param book the book to add
     */
    void addBook(Book book);

    /**
     * Searches for books using the given keyword.
     * <p>
     * Keyword may match the title, author, or ISBN depending on implementation.
     * </p>
     *
     * @param keyword the text to search for
     * @return list of matching {@link Book}s
     */
    List<Book> searchBook(String keyword);

    /**
     * Returns all available books in the library.
     *
     * @return list of all {@link Book} objects
     */
    List<Book> getAllBooks();

    /**
     * Borrows a book for a member.
     * <p>
     * This operation:
     * <ul>
     *     <li>Creates a new {@link loan}</li>
     *     <li>Marks the book as borrowed</li>
     *     <li>Applies borrowing duration (28 days)</li>
     *     <li>Checks permissions (no fines, no overdue loans)</li>
     * </ul>
     * 
     *
     * @param book    the book to borrow
     * @param member  the member borrowing the book
     * @return the created {@link loan}
     * @throws Exception if borrowing fails (e.g., unavailable book or member restrictions)
     */
    loan borrowBook(Book book, Member member) throws Exception;

    /**
     * Returns a previously borrowed book.
     * <p>
     * This operation:
     * <ul>
     *     <li>Marks book as returned</li>
     *     <li>Marks the loan as complete</li>
     *     <li>Applies overdue fines if necessary</li>
     * </ul>
     * 
     *
     * @param loan the loan being returned
     * @throws Exception if the return operation fails
     */
    void returnBook(loan loan) throws Exception;
}
