package library;

/**
 * Represents a book in the library system.
 * <p>
 * This class stores the book's metadata, such as title, author, and ISBN,
 * in addition to maintaining the availability status (whether the book
 * is currently available for borrowing or already loaned out).
 * </p>
 *
 * @author 
 *      Haya Wadah Abdat 
 *      (Documented &amp; revised by Lana Omar)
 * @version 1.1
 * @since 2025-12-06
 */
public class Book {

    /** The title of the book. */
    private String title;

    /** The author of the book. */
    private String author;

    /** The unique ISBN identifier for the book. */
    private String isbn;

    /** Indicates whether the book is available to borrow. */
    private boolean available = true;

    /**
     * Creates a new {@code Book} instance with the specified details.
     *
     * @param title  the title of the book
     * @param author the author who wrote the book
     * @param isbn   the unique ISBN code of the book
     */
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Returns the title of the book.
     *
     * @return the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the book.
     *
     * @return the book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the unique ISBN of the book.
     *
     * @return the book's ISBN identifier
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Indicates whether the book is available for borrowing.
     *
     * @return {@code true} if the book is available, {@code false} if it is borrowed
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Marks this book as borrowed.
     * After invoking this method, {@link #isAvailable()} will return {@code false}.
     */
    public void markBorrowed() {
        this.available = false;
    }

    /**
     * Marks this book as returned and makes it available again.
     * After invoking this method, {@link #isAvailable()} will return {@code true}.
     */
    public void markReturned() {
        this.available = true;
    }

    /**
     * Returns a human-readable representation of the book.
     *
     * @return a formatted string containing title, author, and ISBN
     */
    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }
}
