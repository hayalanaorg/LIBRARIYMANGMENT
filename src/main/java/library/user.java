package library;

/**
 * Base class for all system users (Admin or Member).
 */
public class user {

    private String id;
    private String username;
    private String password;
    private String fullName;
    private boolean admin;
    private boolean active;

    public user(String id, String username, String password, String fullName, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.admin = admin;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", fullName, username);
    }

	
}
