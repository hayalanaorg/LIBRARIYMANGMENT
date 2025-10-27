package service;

import library.Admin;

public class AdminServiceImpl implements AdminService {
    private Admin currentAdmin;

    @Override
    public boolean login(String username, String password) {
        // في هذه المرحلة نستخدم بيانات ثابتة
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
}
