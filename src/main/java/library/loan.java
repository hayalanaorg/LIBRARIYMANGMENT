package library;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a loan transaction where a {@link Member} borrows a single
 * {@link Book} for a fixed duration of 28 days.
 * <p>
 * This class keeps track of:
 * <ul>
 *     <li>The borrowed book</li>
 *     <li>The borrowing member</li>
 *     <li>The borrowing date</li>
 *     <li>The due date</li>
 *     <li>Whether the book has been returned</li>
 * </ul>
 * 
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class loan {

    /** The book being borrowed. */
    private Book book;

    /** The member who borrowed the book. */
    private Member user;

    /** The date the book was borrowed. */
    private LocalDate borrowDate;

    /** The due date (borrow date + 28 days). */
    private LocalDate dueDate;

    /** Indicates whether the book has been returned. */
    private boolean returned = false;

    /**
     * Creates a new loan for a given book and member.
     * The due date is automatically set to 28 days after the borrow date.
     *
     * @param book the book being borrowed
     * @param user the member who is borrowing the book
     */
    public loan(Book book, Member user) {
        this.book = book;
        this.user = user;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(28);
    }

    /**
     * Returns the borrowed book.
     *
     * @return the book in this loan
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the member who borrowed the book.
     *
     * @return the borrowing member
     */
    public Member getUser() {
        return user;
    }

    /**
     * Returns the date the book was borrowed.
     *
     * @return the borrow date
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Returns the due date of the loan.
     *
     * @return the due date (borrow date + 28 days)
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Checks whether the book has been returned.
     *
     * @return {@code true} if returned, otherwise {@code false}
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Sets the returned status of the loan.
     *
     * @param returned boolean indicating if the book is returned
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Determines whether the loan is overdue.
     * A loan is overdue if the book is not returned and the current date is after the due date.
     *
     * @return {@code true} if overdue, otherwise {@code false}
     */
    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates how many days the book is overdue.
     * Returns 0 if the book has been returned or is not overdue.
     *
     * @return the number of overdue days
     */
    public long overdueDays() {
        if (returned) {
            return 0;
        }

        LocalDate now = LocalDate.now();
        if (now.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, now);
        }

        return 0;
    }

    /**
     * Returns the number of overdue days based on a given date.
     * Useful for testing or custom date evaluation.
     *
     * @param now the date to compare with
     * @return overdue days relative to the provided date
     */
    public long getOverdueDays(LocalDate now) {
        if (now == null || returned) {
            return 0;
        }

        if (now.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, now);
        }

        return 0;
    }

    /**
     * Marks the loan as returned and updates the book's availability status.
     * Equivalent to completing the loan.
     */
    public void isReturned2() {
        this.returned = true;

        if (book != null) {
            book.markReturned();
        }
    }

    /**
     * Sets the due date manually using reflection.
     * <p>
     * NOTE: This method exists primarily for testing purposes.
     * </p>
     *
     * @param newDate the new due date
     * @throws RuntimeException if the due date cannot be modified
     */
    public void setDueDate(LocalDate newDate) {
        try {
            Field f = this.getClass().getDeclaredField("dueDate");
            f.setAccessible(true);
            f.set(this, newDate);
        } catch (Exception e) {
            throw new RuntimeException("Cannot modify due date", e);
        }
    }
}
