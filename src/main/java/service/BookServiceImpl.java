package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import library.Book;
import library.Member;
import library.loan;

/**
 * In-memory implementation of the BookService.
 */
public class BookServiceImpl implements BookService {

    private final List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    @Override
    public List<Book> searchBook(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        final String lower = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower)
                        || b.getAuthor().toLowerCase().contains(lower)
                        || b.getIsbn().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public loan borrowBook(Book book, Member member) throws Exception {
        if (book == null || member == null) {
            throw new IllegalArgumentException("Book and member must not be null");
        }
        if (!book.isAvailable()) {
            throw new Exception("Book not available");
        }
        // US4.1 – لا يستعير لو عنده overdue أو unpaid fines
        if (!member.canBorrow()) {
            throw new Exception("User has overdue books or unpaid fines");
        }

        book.markBorrowed();
        loan loan = new loan(book, member);
        member.addLoan(loan);
        return loan;
    }

    @Override
    public void returnBook(loan loan) throws Exception {
        if (loan == null) {
            throw new IllegalArgumentException("Loan must not be null");
        }
        if (loan.isReturned()) {
            throw new Exception("Loan already returned");
        }

        loan.setReturned(true);
        loan.getBook().markReturned();

        long overdueDays = loan.overdueDays();
        if (overdueDays > 0) {
            BigDecimal fineAmount = BigDecimal.valueOf(overdueDays * 10);
            if (loan.getUser() instanceof Member) {
                Member member = (Member) loan.getUser();
                member.addFine(fineAmount);
            }
        }
    }
}
