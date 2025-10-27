package library;


public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("[%s] %s by %s (%s)",
                isbn, title, author, available ? "Available" : "Borrowed");
    }
}


