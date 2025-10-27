package service;

import library.Admin;

public interface AdminService {
    boolean login(String username, String password);
    void logout();
    boolean isLoggedIn();
    Admin getCurrentAdmin();
}

