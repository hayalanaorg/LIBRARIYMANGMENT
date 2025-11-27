package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Sprint 5 - Polymorphic loan class for media (Book or CD)
 */
public class MediaLoan {

    private Object media;            // Book or CD
    private Member user;             // مين استعار
    private LocalDate borrowDate;
    private LocalDate dueDate;
    
    private boolean returned = false; // مهم لسبرنت 5

    public MediaLoan(Object media, LocalDate borrowDate) {
        this.media = media;
        this.borrowDate = borrowDate;

        // Book = 28 days, CD = 7 days
        if (media instanceof CD) {
            this.dueDate = borrowDate.plusDays(7);
        } else {
            this.dueDate = borrowDate.plusDays(28);
        }
    }

    // ===================================
    // GETTERS / SETTERS
    // ===================================
    public Object getMedia() {
        return media;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    // ===================================
    // RETURN FUNCTION
    // ===================================
    public void markReturned() {
        this.returned = true;

        // إذا كان CD → رجّعيه available
        if (media instanceof CD cd) {
            cd.markReturned();
        }

        // إذا كان Book وفيه حد مستعمل BookService يرجّعه برضو
        if (media instanceof Book book) {
            book.markReturned();
        }
    }

    // ===================================
    // OVERDUE CALCULATION
    // ===================================
    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    public int getOverdueDays() {
        if (!isOverdue()) return 0;
        return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Deterministic version for tests.
     */
    public long getOverdueDays(LocalDate today) {
        if (today == null || !today.isAfter(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, today);
    }
    
    
}
