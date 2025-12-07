package service;

import library.Admin;
import library.Book;
import library.user;

/**
 * Defines all administrator-level operations supported by the library system.
 * <p>
 * This interface represents the use cases available to system administrators,
 * such as authentication, book management, user management, and sending
 * overdue reminders. It is implemented by {@link AdminServiceImpl}.
 * </p>
 *
 * <b>Main Responsibilities:</b>
 * <ul>
 *     <li>Admin login / logout management</li>
 *     <li>Book management (add / remove books)</li>
 *     <li>User management (unregistering members)</li>
 *     <li>Sending reminder notifications to overdue users</li>
 * </ul>
 *
 * <b>Related Sprints:</b>
 * <ul>
 *     <li><b>Sprint 1:</b> Book Management</li>
 *     <li><b>Sprint 3:</b> Observer Pattern – Send Reminders</li>
 *     <li><b>Sprint 4:</b> Unregister User</li>
 * </ul>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public interface AdminService {

    /**
     * Attempts to log in an administrator using their username and password.
     *
     * @param username admin username
     * @param password admin password
     * @return {@code true} if the credentials are valid, otherwise {@code false}
     */
    boolean login(String username, String password);

    /**
     * Logs out the currently logged-in administrator.
     */
    void logout();

    /**
     * Checks whether an administrator is currently logged in.
     *
     * @return {@code true} if logged in, otherwise {@code false}
     */
    boolean isLoggedIn();

    /**
     * Returns the currently authenticated admin.
     *
     * @return the active {@link Admin}, or {@code null} if not logged in
     */
    Admin getCurrentAdmin();

    // ============================================================
    // Sprint 1 – Book Management
    // ============================================================

    /**
     * Adds a new book to the library's collection.
     *
     * @param book the book to add
     */
    void addBook(Book book);

    /**
     * Removes a book from the library's collection.
     *
     * @param book the book to remove
     */
    void removeBook(Book book);

    // ============================================================
    // Sprint 4 – Unregister User
    // ============================================================

    /**
     * Unregisters a non-admin user from the system.
     * <p>
     * A user cannot be unregistered if:
     * <ul>
     *     <li>They are an administrator</li>
     *     <li>They have active loans</li>
     *     <li>They have unpaid fines</li>
     * </ul>
     * If any of these rules are violated, an {@link IllegalStateException} is thrown.
     * 
     *
     * @param user the user to remove
     * @throws IllegalStateException if removal conditions are not met
     */
    void unregisterUser(user user) throws IllegalStateException;

    // ============================================================
    // Sprint 3 – Observer Pattern / Send Reminders
    // ============================================================

    /**
     * Sends reminder notifications to all users who have overdue loans.
     * <p>
     * Observers (e.g., {@code EmailNotifier}) receive notifications for
     * each overdue member.
     * </p>
     */
    void sendReminders();
}
