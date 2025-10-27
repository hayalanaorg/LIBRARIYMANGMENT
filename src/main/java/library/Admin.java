package library;
public class Admin extends user {

    public Admin(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    public void addBook(Book book) {
        System.out.println("Admin added: " + book);
    }

    public void removeBook(Book book) {
        System.out.println("Admin removed: " + book);
    }
}
