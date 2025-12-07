package library;

/**
 * Represents an administrator user in the library system.
 * <p>
 * The {@code Admin} class extends the {@link user} class and adds
 * an additional property to identify the admin's role (e.g., "ADMIN", "SUPER_ADMIN").
 * </p>
 *
 * @author Lana Omar
 * @version 1.0
 */
public class Admin extends user {

    /**
     * The role of the admin within the system. 
     * Defaults to {@code "ADMIN"} unless changed using {@link #setRole(String)}.
     */
    private String role = "ADMIN";

    /**
     * Constructs a new {@code Admin} object with the given parameters.
     *
     * @param id         unique identifier for the admin
     * @param username   the admin's username used for login
     * @param password   the admin's account password
     * @param fullName   the admin's real full name
     */
    public Admin(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, true);
    }

    /**
     * Sets the role of the admin.
     * <p>
     * Common roles may include:
     * <ul>
     *     <li>"ADMIN" – regular admin</li>
     *     <li>"SUPER_ADMIN" – full control</li>
     * </ul>
     * 
     *
     * @param role a string representing the admin’s role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the current role of the admin.
     *
     * @return the admin's role as a string
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns a readable textual representation of the admin.
     *
     * @return a string in the form: {@code "Admin: Full Name"}
     */
    @Override
    public String toString() {
        return "Admin: " + getFullName();
    }
}
