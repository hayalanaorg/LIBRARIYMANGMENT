package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import library.Book;
import library.Member;
import library.loan;

/**
 * In-memory implementation of the {@link BookService} interface.
 * <p>
 * This class manages book-related operations such as adding books,
 * searching for books, borrowing books, and returning them.
 * It also handles overdue fine calculations when books are returned late.
 * </p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *     <li>Maintaining an internal list of books</li>
 *     <li>Searching books by title, author, or ISBN</li>
 *     <li>Processing book borrowing requests</li>
 *     <li>Returning books and applying overdue fines</li>
 * </ul>
 *
 * <h3>Borrowing Rules (Sprint 4):</h3>
 * <ul>
 *     <li>Book must be available</li>
 *     <li>Member must have no unpaid fines</li>
 *     <li>Member must have no overdue loans</li>
 *     <li>Borrow duration is fixed at 28 days (handled in {@link loan})</li>
 * </ul>
 *
 * @author
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class BookServiceImpl implements BookService {

    /** Internal list that stores all books in memory. */
    private final List<Book> books = new ArrayList<>();

    /**
     * Adds a book to the library collection.
     *
     * @param book the book to add
     */
    @Override
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    /**
     * Searches for books using a keyword.
     * <p>
     * The keyword is matched (case-insensitively) against:
     * <ul>
     *     <li>title</li>
     *     <li>author</li>
     *     <li>ISBN</li>
     * </ul>
     *
     *
     * @param keyword the search text; if null, treated as an empty string
     * @return list of matching {@link Book} objects
     */
    @Override
    public List<Book> searchBook(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        final String lower = keyword.toLowerCase();

        return books.stream()
                .filter(b ->
                        b.getTitle().toLowerCase().contains(lower) ||
                        b.getAuthor().toLowerCase().contains(lower) ||
                        b.getIsbn().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /**
     * Returns all books currently in the collection.
     *
     * @return a list containing all {@link Book} objects
     */
    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Processes a book borrowing request.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Validates the book and member</li>
     *     <li>Ensures the book is available</li>
     *     <li>Ensures the member has no unpaid fines or overdue loans</li>
     *     <li>Marks the book as borrowed</li>
     *     <li>Creates a new {@link loan} and attaches it to the member</li>
     * </ul>
     * 
     *
     * @param book   the book to borrow
     * @param member the member borrowing the book
     * @return the created {@link loan}
     *
     * @throws IllegalArgumentException if book or member is null
     * @throws Exception if borrowing rules are violated
     */
    @Override
    public loan borrowBook(Book book, Member member) throws Exception {

        if (book == null || member == null) {
            throw new IllegalArgumentException("Book and member must not be null");
        }

        if (!book.isAvailable()) {
            throw new Exception("Book not available");
        }

        // Sprint 4 — prevent borrowing if user has unpaid fines or overdue loans
        if (!member.canBorrow()) {
            throw new Exception("User has overdue books or unpaid fines");
        }

        // Mark book as borrowed
        book.markBorrowed();

        // Create loan and attach to member
        loan loan = new loan(book, member);
        member.addLoan(loan);

        return loan;
    }

    /**
     * Returns a borrowed book and applies overdue fines if necessary.
     * <p>
     * Steps:
     * <ul>
     *     <li>Validates the loan object</li>
     *     <li>Checks if already returned</li>
     *     <li>Computes overdue days</li>
     *     <li>Marks book &amp; loan as returned</li>
     *     <li>Applies fine (10 NIS per overdue day)</li>
     * </ul>
     * 
     *
     * @param loan the loan to return
     *
     * @throws IllegalArgumentException if loan is null
     * @throws Exception if the loan was already returned
     */
    @Override
    public void returnBook(loan loan) throws Exception {

        if (loan == null) {
            throw new IllegalArgumentException("Loan must not be null");
        }

        if (loan.isReturned()) {
            throw new Exception("Loan already returned");
        }

        // Calculate overdue days before marking returned
        long overdueDays = loan.overdueDays();

        // Mark both loan and book as returned
        loan.setReturned(true);
        loan.getBook().markReturned();

        // Apply fine if overdue
        if (overdueDays > 0) {
            BigDecimal fineAmount = BigDecimal.valueOf(overdueDays * 10);
            loan.getUser().addFine(fineAmount);
        }
    }
}
