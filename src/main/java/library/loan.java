package library;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a loan of a single Book by a Member.
 * Duration = 28 days.
 */
public class loan {

    private Book book;
    private Member user;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned = false;

    public loan(Book book, Member user) {
        this.book = book;
        this.user = user;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(28);
    }

    public Book getBook() {
        return book;
    }

    public Member getUser() {
        return user;
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

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }


    public long overdueDays() {
        // إذا الكتاب مُعاد → نرجّع 0
        if (returned) {
            return 0;
        }

        LocalDate now = LocalDate.now();
        if (now.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, now);
        }

        return 0;
    }



    public long getOverdueDays(LocalDate now) {
        if (now == null || returned) {
            return 0;
        }
        

        if (now.isAfter(dueDate)) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, now);
        }

        return 0;
    }

    public void isReturned2() {
        this.returned = true;

        // نرجّع حالة الكتاب
        if (book != null) {
            book.markReturned();
        }
    }

    
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
