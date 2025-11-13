package library;

public class ConsoleEmailService implements EmailMessage {
    @Override
    public void sendEmail(user u, String message) {
        System.out.println("=== Sending email ===");
        System.out.println("To: " + u.getFullName() + " <" + u.getUsername() + ">");
        System.out.println("Body: " + message);
        System.out.println("=====================");
    }
}
//  لازم نعملله تيست