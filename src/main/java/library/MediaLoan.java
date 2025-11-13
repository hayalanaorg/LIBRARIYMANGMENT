package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Sprint 5 - Polymorphic loan class for media (books, CDs)
 */
public class MediaLoan {
    private Object media; // ممكن يكون Book أو CD
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public MediaLoan(Object media, LocalDate borrowDate) {
        this.media = media;
        this.borrowDate = borrowDate;

        if (media instanceof CD) {
            this.dueDate = borrowDate.plusDays(7); // CD: 7 أيام
        } else {
            this.dueDate = borrowDate.plusDays(28); // Book: 28 يوم
        }
    }

    public Object getMedia() { return media; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public int getOverdueDays() {
        if (!isOverdue()) return 0;
        return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}
