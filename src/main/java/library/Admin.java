package library;

public class Admin extends user {

    public Admin(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    public boolean authenticate(String password) {
        return getPassword().equals(password);
    }
}
