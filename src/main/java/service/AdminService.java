package service;

import library.Admin;
import library.Book;
import library.user;

public interface AdminService {
    boolean login(String username, String password);
    void logout();
    boolean isLoggedIn();
    Admin getCurrentAdmin();

    // Sprint 1+2
    void addBook(Book book);
    void removeBook(Book book);
    void unregisterUser(user user) throws IllegalStateException;
	void sendReminders();
	
	
}
