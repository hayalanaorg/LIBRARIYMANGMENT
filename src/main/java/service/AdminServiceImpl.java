package service;

import library.Admin;
import library.Book;
import library.EmailMessage;
import library.user;

import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl implements AdminService {
	
    private EmailMessage emailService; // حقن الخدمة عبر الكونستركتور

	
    private Admin currentAdmin;
    private List<Book> books = new ArrayList<>();
    private List<user> users = new ArrayList<>();

    @Override
    public boolean login(String username, String password) {
        if ("admin".equals(username) && "1234".equals(password)) {
            currentAdmin = new Admin(username, password, "Library Administrator");
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        currentAdmin = null;
    }

    @Override
    public boolean isLoggedIn() {
        return currentAdmin != null;
    }

    @Override
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public void unregisterUser(user user) throws IllegalStateException {
        if (!users.contains(user)) throw new IllegalStateException("User not found");
        users.remove(user);
    }

    // مساعدات للتست
    public List<Book> getBooks() { return books; }
    public void addUser(user u) { users.add(u); }
    public List<user> getUsers() { return users; }
    
    public AdminServiceImpl( ) {
        this.users = new ArrayList<>();
    }
    
    public AdminServiceImpl(EmailMessage emailService) {
        this.emailService = emailService;
        this.users = new ArrayList<>();
    }
    @Override
    public void sendReminders() {
        for (user u : users) {
            long overdueBooks = u.getLoans().stream().filter(l -> l.isOverdue()).count();
            if (overdueBooks > 0) {
                String message = "You have " + overdueBooks + " overdue book(s).";
                emailService.sendEmail(u, message);
            }
        }
    }

}
