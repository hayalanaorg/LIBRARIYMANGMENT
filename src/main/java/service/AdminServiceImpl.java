package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import library.Admin;
import library.Book;
import library.Member;
import library.Observer;
import library.loan;
import library.user;

/**
 * In-memory implementation of AdminService.
 */
public class AdminServiceImpl implements AdminService {

    private Admin currentAdmin;
    private final List<Book> books = new ArrayList<>();
    private final List<user> users = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    public AdminServiceImpl() {
    }

    /**
     * Allow tests / setup code to pre-register users.
     */
    public void addUser(user user) {
        if (user != null) {
            users.add(user);
        }
    }

    public List<user> getUsers() {
        return new ArrayList<>(users);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public boolean login(String username, String password) {
        // Simple fixed admin for phase 1
        if ("admin".equals(username) && "1234".equals(password)) {
            currentAdmin = new Admin("admin-id", username, password, "Library Administrator");
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
        if (book != null) {
            books.add(book);
        }
    }

    @Override
    public void removeBook(Book book) {
        books.remove(book);
    }

    /**
     * Find a user by username (helper for tests).
     */
    public user findUser(String username) {
        if (username == null) {
            return null;
        }
        for (user u : users) {
            if (username.equals(u.getUsername())) {
                return u;
            }
        }
        return null;
    }

    /**
     * Sprint 4 – Unregister user.
     * Only admins can unregister users.
     * Users with active loans or unpaid fines cannot be unregistered.
     */
    @Override
    public void unregisterUser(user u) throws IllegalStateException {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Admin must be logged in");
        }
        if (u == null || !users.contains(u)) {
            throw new IllegalStateException("User not found");
        }
        if (u instanceof Admin) {
            throw new IllegalStateException("Cannot unregister admin users");
        }

        if (u instanceof Member) {
            Member m = (Member) u;
            boolean hasActiveLoans = m.getLoans().stream()
                    .anyMatch(loan -> !loan.isReturned());
            boolean hasUnpaidFines = m.getFineBalance().compareTo(BigDecimal.ZERO) > 0;

            if (hasActiveLoans || hasUnpaidFines) {
                throw new IllegalStateException("User has active loans or unpaid fines");
            }
        }

        users.remove(u);
        u.deactivate();
    }

    /**
     * Sprint 3 – Send reminder notifications to users with overdue books.
     * Uses the Observer pattern to allow different notification channels.
     */
    @Override
    public void sendReminders() {
    	
        for (user u : users) {
            if (u instanceof Member m) {

                long overdueCount = m.getLoans().stream()
                        .filter(l -> !l.isReturned() && l.isOverdue())
                        .count();

                if (overdueCount > 0) {
                    String msg = "You have " + overdueCount + " overdue book(s).";

                    for (Observer o : observers) {
                        o.notify(m, msg);
                    }
                }
            }
        }
    }





}
