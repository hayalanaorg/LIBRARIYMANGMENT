package library;



import java.time.LocalDate;

public class loan {

    private Book book;
    private user user;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;

    public loan(Book book, user user) {
        this.book = book;
        this.user = user;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(28); // 28 يوم استعارة
        this.returned = false;
    }

    public Book getBook() {
        return book;
    }

    public user getUser() {
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
}
