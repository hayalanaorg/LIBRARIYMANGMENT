package library;

/**
 * Represents a book in the library system.
 * Contains information about the book and its availability status.
 * 
 * @author Haya Wadah Abdat
 * @version 1.0
 * @since 2025-12-06
 */
public class Book {

    private String title;
    private String author;
    private String isbn;
    private boolean available = true;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
    /**
     * Checks if the book is available for borrowing.
     * 
     * @return true if available, false if currently borrowed
     */
    public boolean isAvailable() {
        return available;
    }

    public void markBorrowed() {
        this.available = false;
    }

    public void markReturned() {
        this.available = true;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }
}
