package library;

import java.util.ArrayList;
import java.util.List;

public class libraryy {
    private List<Book> books; 

    public libraryy() {
        books = new ArrayList<>();
    }

    // 🔹 US1.3 – Add Book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    // 🔹 US1.4 – Search Book
    public List<Book> searchBook(String keyword) {
        List<Book> result = new ArrayList<>();

        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                b.getIsbn().equalsIgnoreCase(keyword)) {
                result.add(b);
            }
        }
        return result;
    }

    //
    public List<Book> getAllBooks() {
        return books;
    }
}
