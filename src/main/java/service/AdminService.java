package service;

import library.Admin;
import library.Book;
import library.user;

/**
 * Service for administrator use cases such as login, managing books
 * and managing users.
 */
public interface AdminService {

    boolean login(String username, String password);

    void logout();

    boolean isLoggedIn();

    Admin getCurrentAdmin();

    // Sprint 1 – Core book management
    void addBook(Book book);

    void removeBook(Book book);

    // Sprint 4 – Unregister user
    void unregisterUser(user user) throws IllegalStateException;

    // Sprint 3 – Send reminder notifications for overdue users
    void sendReminders();
}
