package library;

/**
 * Represents an administrator user.
 */
public class Admin extends user {

    public Admin(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, true);
    }

    @Override
    public String toString() {
        return "Admin: " + getFullName();
    }
}
