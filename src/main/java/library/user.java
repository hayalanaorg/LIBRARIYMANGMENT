package library;


public class user {
    private String username;
    private String password;
    private String fullName;

    public user(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return String.format("User: %s (%s)", fullName, username);
    }
}
