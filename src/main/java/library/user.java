package library;

/**
 * Base class for all system users in the library application.
 * <p>
 * This class is extended by both {@link Admin} and {@link Member},
 * providing shared attributes such as ID, username, password, full name,
 * and account status. It also distinguishes whether the user is an admin.
 * </p>
 *
 * <h2>Main Features:</h2>
 * <ul>
 *     <li>User identity information (ID, username, full name)</li>
 *     <li>Authentication data (password)</li>
 *     <li>Role identification (admin or not)</li>
 *     <li>Account status (active or deactivated)</li>
 * </ul>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class user {

    /** Unique system identifier for the user. */
    private String id;

    /** Username used for login. */
    private String username;

    /** Password for account authentication. */
    private String password;

    /** The user's real full name. */
    private String fullName;

    /** Indicates whether this user is an administrator. */
    private boolean admin;

    /** Indicates whether this account is active. */
    private boolean active;

    /**
     * Constructs a new system user with the specified details.
     *
     * @param id        unique identifier for the user
     * @param username  account login name
     * @param password  account password
     * @param fullName  user's real full name
     * @param admin     {@code true} if the user is an administrator
     */
    public user(String id, String username, String password, String fullName, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.admin = admin;
        this.active = true;
    }

    /**
     * Returns the user's unique ID.
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the username used for login.
     *
     * @return login username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's password.
     * <p><b>Note:</b> Passwords should not be exposed in real systems.</p>
     *
     * @return account password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's full name.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Determines whether the user has administrator privileges.
     *
     * @return {@code true} if admin, otherwise {@code false}
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Indicates whether the user's account is active.
     *
     * @return {@code true} if active, otherwise {@code false}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Deactivates the user's account.
     * <p>
     * This is typically used when unregistering users.
     * </p>
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Returns a readable representation of the user.
     *
     * @return formatted string "{fullName} ({username})"
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", fullName, username);
    }
}
