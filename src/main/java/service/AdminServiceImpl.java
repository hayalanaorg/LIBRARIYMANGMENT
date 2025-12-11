package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import library.Admin;
import library.Book;
import library.Member;
import library.Observer;
import library.loan;
import library.user;

/**
 * In-memory implementation of {@link AdminService}.
 * <p>
 * This service handles all administrator operations such as:
 * authentication, managing books and users, and sending reminder
 * notifications using the Observer design pattern.
 * </p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *     <li>Admin login/logout management</li>
 *     <li>Book collection management</li>
 *     <li>User registration and removal</li>
 *     <li>Observer-based overdue reminder notifications</li>
 * </ul>
 *
 * <h3>Related Sprints:</h3>
 * <ul>
 *     <li><b>Sprint 1:</b> Book management</li>
 *     <li><b>Sprint 3:</b> Reminder notifications (Observer pattern)</li>
 *     <li><b>Sprint 4:</b> Unregister user rules</li>
 * </ul>
 *
 * @author
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class AdminServiceImpl implements AdminService {

    /** Currently logged-in administrator. */
    private Admin currentAdmin;

    /** List of all books in the library. */
    private final List<Book> books = new ArrayList<>();

    /** List of all registered users (admins + members). */
    private final List<user> users = new ArrayList<>();

    /** List of observers that handle reminder notifications. */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Creates a new instance of the admin service using
     * an in-memory data store.
     */
    public AdminServiceImpl() {
        // Empty constructor - uses default initialization.
        // Books, users, and observers lists are already initialized inline.
    }

    // USER MANAGEMENT

    /**
     * Registers a new user in the system (used mainly for tests/setup).
     *
     * @param user the user to add
     */
    public void addUser(user user) {
        if (user != null) {
            users.add(user);
        }
    }

    /**
     * Returns a copy of all registered users.
     *
     * @return list of users
     */
    public List<user> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Helper method used to find a user by username.
     *
     * @param username the username to search for
     * @return matching user or {@code null} if not found
     */
    public user findUser(String username) {
        if (username == null) return null;

        for (user u : users) {
            if (username.equals(u.getUsername())) {
                return u;
            }
        }
        return null;
    }

    // ============================================================
    // BOOK MANAGEMENT
    // ============================================================

    /**
     * Returns a copy of the list of books.
     *
     * @return list of {@link Book} objects
     */
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Registers a new book in the library.
     *
     * @param book the book to add
     */
    @Override
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    /**
     * Removes a book from the library's collection.
     *
     * @param book the book to remove
     */
    @Override
    public void removeBook(Book book) {
        books.remove(book);
    }

    // ============================================================
    // OBSERVER MANAGEMENT (Sprint 3)
    // ============================================================

    /**
     * Adds an observer that will be notified of overdue reminders.
     *
     * @param observer observer instance
     */
    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the notification list.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // ============================================================
    // AUTHENTICATION
    // ============================================================

    /**
     * Attempts a simple login using fixed credentials ("admin", "1234").
     *
     * @param username admin username
     * @param password admin password
     * @return {@code true} if authenticated, otherwise {@code false}
     */
    @Override
    public boolean login(String username, String password) {
        if ("admin".equals(username) && "1234".equals(password)) {
            currentAdmin = new Admin("admin-id", username, password, "Library Administrator");
            return true;
        }
        return false;
    }

    /**
     * Logs out the currently active admin.
     */
    @Override
    public void logout() {
        currentAdmin = null;
    }

    /**
     * Checks whether an administrator is currently logged in.
     *
     * @return {@code true} if logged in, otherwise {@code false}
     */
    @Override
    public boolean isLoggedIn() {
        return currentAdmin != null;
    }

    /**
     * Returns the currently logged-in administrator.
     *
     * @return active {@link Admin} or {@code null}
     */
    @Override
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    // ============================================================
    // UNREGISTER USER (Sprint 4)
    // ============================================================

    /**
     * Unregisters a user from the system.
     * <p>
     * A user cannot be unregistered if:
     * <ul>
     *     <li>Admin is not logged in</li>
     *     <li>User does not exist</li>
     *     <li>User is an admin</li>
     *     <li>User has active loans</li>
     *     <li>User has unpaid fines</li>
     * </ul>
     * Violations result in an {@link IllegalStateException}.
     *
     *
     * @param u the user to unregister
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

        if (u instanceof Member m) {
            boolean hasActiveLoans = m.getLoans().stream()
                    .anyMatch(l -> !l.isReturned());

            boolean hasUnpaidFines = m.getFineBalance().compareTo(BigDecimal.ZERO) > 0;

            if (hasActiveLoans || hasUnpaidFines) {
                throw new IllegalStateException("User has active loans or unpaid fines");
            }
        }

        users.remove(u);
        u.deactivate();
    }

    // ============================================================
    // SEND REMINDERS (Sprint 3 – Observer Pattern)
    // ============================================================

    /**
     * Sends overdue reminder notifications to all members who have
     * at least one unreturned and overdue loan.
     * <p>
     * Observers such as {@code EmailNotifier} receive notifications
     * for each overdue member.
     * </p>
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
