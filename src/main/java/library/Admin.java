package library;

public class Admin extends user {

    // NEW ➜ نوع الأدمن
    private String role = "ADMIN";   // default → أدمن صغير

    public Admin(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, true);
    }

    // NEW ➜ setter لتحديد نوعه بعد الإنشاء
    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Admin: " + getFullName();
    }
}
