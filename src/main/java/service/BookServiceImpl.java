package service;


import library.Book;
import library.loan;
import library.user;
import java.math.BigDecimal;
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
    
    @Override
    public loan borrowBook(Book book, user user) throws Exception {
        if (!book.isAvailable()) throw new Exception("Book not available");
        if (!user.canBorrow()) throw new Exception("User has unpaid fines");

        book.markBorrowed();
        loan loan = new loan(book, user);
        user.addLoan(loan);
        return loan;
    }

    @Override
    public void returnBook(loan loan) throws Exception {
        if (loan.isReturned()) throw new Exception("Loan already returned");

        loan.setReturned(true);
        loan.getBook().markReturned();

        long overdueDays = loan.overdueDays();
        if (overdueDays > 0) {
            BigDecimal fine = BigDecimal.valueOf(overdueDays * 10); // 10 NIS per day
            loan.getUser().addFine(fine);
        }
    }    
    
}

