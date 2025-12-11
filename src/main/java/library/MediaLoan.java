package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a polymorphic loan for different types of media,
 * supporting both {@link Book} and {@link CD}.
 * <p>
 * This class was introduced in Sprint 5 to generalize the loan model so that
 * a single object can represent a loan of either type of media.
 * </p>
 *
 * <h2>Loan Duration Rules:</h2>
 * <ul>
 *     <li><b>Book:</b> 28 days</li>
 *     <li><b>CD:</b> 7 days</li>
 * </ul>
 *
 * <p>Return state and overdue calculations are included to support higher-level
 * operations such as penalties and reminders.</p>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class MediaLoan {

    /** The media item being borrowed (either {@link Book} or {@link CD}). */
    private Object media;

    /** The member who borrowed the media item. */
    private Member user;

    /** The date when the media was borrowed. */
    private LocalDate borrowDate;

    /** The calculated due date based on media type. */
    private LocalDate dueDate;

    /** Indicates whether the media item has been returned. */
    private boolean returned = false;

    /**
     * Creates a new media loan.
     * <p>
     * The due date depends on the type of media:
     * <ul>
     *     <li>{@link Book}: borrow date + 28 days</li>
     *     <li>{@link CD}: borrow date + 7 days</li>
     * </ul>
     * 
     *
     * @param media       the borrowed media item (Book or CD)
     * @param borrowDate  the date the media was borrowed
     */
    public MediaLoan(Object media, LocalDate borrowDate) {
        this.media = media;
        this.borrowDate = borrowDate;

        if (media instanceof CD) {
            this.dueDate = borrowDate.plusDays(7);
        } else {
            this.dueDate = borrowDate.plusDays(28);
        }
    }

    // GETTERS / SETTERS

    /**
     * Returns the media item (Book or CD).
     *
     * @return the borrowed media item
     */
    public Object getMedia() {
        return media;
    }

    /**
     * Returns the member who borrowed the media.
     *
     * @return the borrowing member
     */
    public Member getUser() {
        return user;
    }

    /**
     * Assigns the borrowing member to this loan.
     *
     * @param user the member borrowing the media
     */
    public void setUser(Member user) {
        this.user = user;
    }

    /**
     * Returns the date the media was borrowed.
     *
     * @return the borrow date
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Returns the due date for returning the media.
     *
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Indicates whether the media has been returned.
     *
     * @return true if returned, false otherwise
     */
    public boolean isReturned() {
        return returned;
    }

    // RETURN FUNCTION

    /**
     * Marks the media item as returned and updates its availability:
     * <ul>
     *     <li>If media is a {@link CD}, calls {@link CD#markReturned()}</li>
     *     <li>If media is a {@link Book}, calls {@link Book#markReturned()}</li>
     * </ul>
     */
    public void markReturned() {
        this.returned = true;

        if (media instanceof CD cd) {
            cd.markReturned();
        }

        if (media instanceof Book book) {
            book.markReturned();
        }
    }

    // OVERDUE CALCULATION

    /**
     * Determines whether the loan is overdue based on today's date.
     *
     * @return true if overdue and not returned, otherwise false
     */
    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates the overdue days using the current system date.
     *
     * @return number of overdue days, or 0 if not overdue
     */
    public int getOverdueDays() {
        if (!isOverdue()) return 0;
        return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Deterministic version of overdue-days calculation used for testing.
     *
     * @param today the simulated "current" date
     * @return overdue days based on the provided date, or 0 if not overdue
     */
    public long getOverdueDays(LocalDate today) {
        if (today == null || !today.isAfter(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, today);
    }
}
