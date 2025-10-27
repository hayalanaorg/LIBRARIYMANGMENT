package service;


import library.Book;              // تأكد أن مسار Book صحيح
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class BookServiceImpl implements BookService {
    private List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public List<Book> searchBook(String keyword) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || b.getAuthor().toLowerCase().contains(keyword.toLowerCase())
                        || b.getIsbn().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}

