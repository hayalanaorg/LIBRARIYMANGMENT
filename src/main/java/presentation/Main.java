/*package presentation;

import library.*;
import service.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;


public class Main {

    static Scanner input = new Scanner(System.in);

    static final int MAX_MEMBERS = 100;
    static final int MAX_BOOKS = 200;
    static final int MAX_LOANS = 300;
    static final int MAX_CDS = 100;
    static final int MAX_MEDIA_LOANS = 200;

    static Member[] members = new Member[MAX_MEMBERS];
    static int memberCount = 0;

    static Book[] books = new Book[MAX_BOOKS];
    static int bookCount = 0;

    static loan[] loans = new loan[MAX_LOANS];
    static int loanCount = 0;

    static CD[] cds = new CD[MAX_CDS];
    static int cdCount = 0;

    static MediaLoan[] mediaLoans = new MediaLoan[MAX_MEDIA_LOANS];
    static int mediaLoanCount = 0;

    static AdminServiceImpl adminService = new AdminServiceImpl();
    static BookServiceImpl bookService = new BookServiceImpl();
    
    static Admin currentAdmin = null;
    
    static EmailService emailService;
    static Observer emailNotifier;

    public static void main(String[] args) {
        
    	 try {
    	        Dotenv dotenv = Dotenv.load();
    	        EmailService service = new EmailService(
    	            dotenv.get("EMAIL_USERNAME"),
    	            dotenv.get("EMAIL_PASSWORD")
    	        );
    	        
    	        service.sendEmail("test@example.com", "Test", "Hello!");
    	        System.out.println("✔ Email sent successfully!");
    	    } catch (Exception e) {
    	        System.err.println("✖ Failed: " + e.getMessage());
    	    }

        Admin superAdmin = new Admin("1", "superadmin", "1234", "Super Admin");
        superAdmin.setRole("SUPER_ADMIN");
        Admin smallAdmin = new Admin("2", "smalladmin", "abcd", "Small Admin");
        smallAdmin.setRole("SMALL_ADMIN");

        adminService.addUser(superAdmin);
        adminService.addUser(smallAdmin);

        while (true) {
            System.out.println("\n=== Welcome to Library Management ===");
            System.out.println("1) User Login (Admin or Member)");
            System.out.println("0) Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    user u = login();
                    if (u instanceof Admin a) {
                        currentAdmin = a;
                        if (a.getRole().equalsIgnoreCase("SUPER_ADMIN")) superAdminMenu(a);
                        else if (a.getRole().equalsIgnoreCase("SMALL_ADMIN")) smallAdminMenu(a);
                        else System.out.println("✖ Unknown admin role");
                        currentAdmin = null;
                    } else if (u instanceof Member m) {
                        memberMenu(m);
                    }
                    break;
                case 0:
                    System.out.println("Bye bye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ===================== LOGIN =====================
    private static user login() {
        String username = readLine("Username: ");
        String password = readLine("Password: ");

        user u = adminService.findUser(username);

        if (u instanceof Admin a) {
            if (a.getPassword().equals(password)) {
                adminService.login(username, password);
                System.out.println("✔ Logged in as admin (" + a.getRole() + ")");
                return a;
            } else {
                System.out.println("✖ Wrong password");
                return null;
            }
        } else if (u instanceof Member m) {
            System.out.println("✔ Member logged in: " + m.getUsername());
            return m;
        }

        System.out.println("✖ User not found");
        return null;
    }

    // ===================== SUPER ADMIN MENU =====================
    private static void superAdminMenu(Admin a) {
        while (true) {
            System.out.println("\n--- Super Admin Menu ---");
            System.out.println("1) Add Member");
            System.out.println("2) Unregister Member");
            System.out.println("3) Add Book");
            System.out.println("4) Show Members");
            System.out.println("5) Show Books");
            System.out.println("6) Borrow Book");
            System.out.println("7) Return Book");
            System.out.println("8) Pay Fine");
            System.out.println("9) Borrow CD");
            System.out.println("10) Return CD");
            System.out.println("11) Show User Borrowed Books");
            System.out.println("12) Send Overdue Reminders");
            System.out.println("0) Logout");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: addMember(); break;
                case 2: unregisterMember(); break;
                case 3: addBook(); break;
                case 4: showMembers(); break;
                case 5: showBooks(); break;
                case 6: borrowBook(); break;
                case 7: returnBook(); break;
                case 8: payFine(); break;
                case 9: borrowCD(); break;
                case 10: returnCD(); break;
                case 11: showUserBorrowedBooks(); break;
                case 12: sendOverdueReminders(); break;
                case 0:
                    adminService.logout();
                    currentAdmin = null;
                    return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ===================== SMALL ADMIN MENU =====================
    private static void smallAdminMenu(Admin a) {
        while (true) {
            System.out.println("\n--- Small Admin Menu ---");
            System.out.println("1) Add Book");
            System.out.println("2) Show Books");
            System.out.println("0) Logout");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: addBook(); break;
                case 2: showBooks(); break;
                case 0:
                    adminService.logout();
                    currentAdmin = null;
                    return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ===================== MEMBER MENU =====================
    private static void memberMenu(Member m) {
        while (true) {
            System.out.println("\n--- Member Menu ---");
            System.out.println("1) Borrow Book");
            System.out.println("2) Return Book");
            System.out.println("3) Show Borrowed Books");
            System.out.println("4) Borrow CD");
            System.out.println("5) Return CD");
            System.out.println("6) Pay Fine");
            System.out.println("0) Logout");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: borrowBook(m); break;
                case 2: returnBook(m); break;
                case 3: showUserBorrowedBooks(m); break;
                case 4: borrowCD(m); break;
                case 5: returnCD(m); break;
                case 6: payFine(m); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ===================== HELPERS =====================
    private static String readLine(String msg) {
        System.out.print(msg);
        return input.nextLine();
    }

    private static int readInt(String msg) {
        try { 
            System.out.print(msg); 
            return Integer.parseInt(input.nextLine()); 
        }
        catch (Exception e) { 
            return -1; 
        }
    }

    // ===================== MEMBER/ADMIN FUNCTIONS =====================
    private static void addMember() {
        if (currentAdmin == null) {
            System.out.println("✖ Admin must login first!");
            return;
        }

        String id = readLine("Member ID: ");
        String username = readLine("Username: ");
        String password = readLine("Password: ");
        String name = readLine("Full Name: ");
        String email = readLine("Email: ");

        Member m = new Member(id, username, password, name, email);
        members[memberCount++] = m;
        adminService.addUser(m);
        System.out.println("✔ Member added successfully by " + currentAdmin.getUsername());
    }

    private static void unregisterMember() {
        if (currentAdmin == null) {
            System.out.println("✖ Admin must login first!");
            return;
        }
        
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }

        try { 
            adminService.unregisterUser(m); 
            System.out.println("✔ Member unregistered successfully"); 
        }
        catch (Exception e) { 
            System.out.println("✖ " + e.getMessage()); 
        }
    }

    private static void addBook() {
        if (currentAdmin == null) {
            System.out.println("✖ Admin must login first!");
            return;
        }
        
        String title = readLine("Book title: ");
        String author = readLine("Author: ");
        String isbn = readLine("ISBN: ");

        Book b = new Book(title, author, isbn);
        books[bookCount++] = b;
        adminService.addBook(b);
        System.out.println("✔ Book added successfully by " + currentAdmin.getUsername());
    }

    private static Member findMember(String username) {
        for (int i = 0; i < memberCount; i++)
            if (members[i].getUsername().equals(username))
                return members[i];
        return null;
    }

    private static Book findBook(String isbn) {
        for (int i = 0; i < bookCount; i++)
            if (books[i].getIsbn().equals(isbn))
                return books[i];
        return null;
    }

    // ===================== BORROW / RETURN BOOK =====================
    private static void borrowBook() {
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }
        borrowBook(m);
    }

    private static void borrowBook(Member m) {
        String isbn = readLine("Book ISBN: ");
        Book b = findBook(isbn);
        if (b == null) { 
            System.out.println("✖ Book not found"); 
            return; 
        }
        if (!b.isAvailable()) { 
            System.out.println("✖ Book already borrowed"); 
            return; 
        }

        loan ln = new loan(b, m);
        loans[loanCount++] = ln;
        loans[0].setDueDate(LocalDate.now().minusDays(3));

        m.getLoans().add(ln);
        b.markBorrowed();
        System.out.println("✔ Book borrowed successfully!");
        System.out.println("📅 Due date: " + ln.getDueDate());
        
        // Check for overdue books and send reminder automatically
        checkAndSendOverdueReminder(m);
    }

    private static void returnBook() {
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }
        returnBook(m);
    }

    private static void returnBook(Member m) {
        String isbn = readLine("Book ISBN: ");
        Book b = findBook(isbn);
        if (b == null) { 
            System.out.println("✖ Book not found"); 
            return; 
        }

        loan ln = null;
        for (int i = 0; i < loanCount; i++) {
            if (loans[i] != null && loans[i].getUser().equals(m)
                    && loans[i].getBook().equals(b) && !loans[i].isReturned()) {
                ln = loans[i];
                break;
            }
        }

        if (ln == null) { 
            System.out.println("✖ No active loan found for this book"); 
            return; 
        }

        // Check if book was overdue and send email before returning
        if (ln.isOverdue()) {
            sendOverdueEmailForLoan(m, ln);
        }

        ln.isReturned2();
        System.out.println("✔ Book returned successfully!");
        System.out.println("💰 Current fine balance: " + m.getFineBalance() + " NIS");
    }

    // ===================== PAY FINE =====================
    private static void payFine() {
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }
        payFine(m);
    }

    private static void payFine(Member m) {
        BigDecimal totalFine = calculateCurrentFine(m);
        BigDecimal registeredFine = m.getFineBalance();
        BigDecimal overdueFine = totalFine.subtract(registeredFine);
        
        System.out.println("💰 Registered fine: " + registeredFine + " NIS");
        System.out.println("⚠  Overdue fine: " + overdueFine + " NIS");
        System.out.println("📊 Total fine: " + totalFine + " NIS");
        System.out.println();
        
        if (totalFine.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("✔ No fines to pay!");
            return;
        }
        
        BigDecimal amt = new BigDecimal(readLine("Amount to pay: "));
        
        if (amt.compareTo(totalFine) > 0) {
            System.out.println("⚠  Amount exceeds total fine. Paying full amount: " + totalFine + " NIS");
            amt = totalFine;
        }
        
        m.payFine(amt);
        BigDecimal remaining = calculateCurrentFine(m);
        
        System.out.println("✔ Fine paid successfully!");
        System.out.println("💰 Remaining balance: " + remaining + " NIS");
    }

    // ===================== SHOW =====================
    private static void showMembers() {
        System.out.println("\n==== Members List ====");
        if (memberCount == 0) {
            System.out.println("No members registered yet.");
            return;
        }
        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            BigDecimal totalFine = calculateCurrentFine(m);
            String overdueBooks = getOverdueBookCount(m);
            System.out.println("- " + m.getUsername() + " | Fine: " + totalFine + " NIS" + overdueBooks);
        }
    }
    
   
    private static BigDecimal calculateCurrentFine(Member m) {
        BigDecimal total = m.getFineBalance();
        
        for (int i = 0; i < loanCount; i++) {
            loan ln = loans[i];
            if (ln != null && ln.getUser().equals(m) && !ln.isReturned()) {
                if (ln.isOverdue()) {
                    int overdueDays = (int) ln.getOverdueDays(LocalDate.now());
                    total = total.add(new BigDecimal(overdueDays * 10));
                }
            }
        }
        
        return total;
    }
    
   
    private static String getOverdueBookCount(Member m) {
        int overdueCount = 0;
        for (int i = 0; i < loanCount; i++) {
            loan ln = loans[i];
            if (ln != null && ln.getUser().equals(m) && !ln.isReturned() && ln.isOverdue()) {
                overdueCount++;
            }
        }
        return overdueCount > 0 ? " [" + overdueCount + " overdue book(s)]" : "";
    }

    private static void showBooks() {
        System.out.println("\n==== Books List ====");
        if (bookCount == 0) {
            System.out.println("No books in library yet.");
            return;
        }
        for (int i = 0; i < bookCount; i++) {
            Book b = books[i];
            String status = b.isAvailable() ? "✓ Available" : "✗ Borrowed";
            System.out.println("- " + b.getTitle() + " by " + b.getAuthor() + " (ISBN: " + b.getIsbn() + ") [" + status + "]");
        }
    }

    private static void showUserBorrowedBooks() {
        System.out.println("\n==== All Borrowed Books ====");
        for (int i = 0; i < memberCount; i++)
            showUserBorrowedBooks(members[i]);
    }

    private static void showUserBorrowedBooks(Member m) {
        System.out.println("\n📚 Borrowed books for " + m.getUsername() + ":");
        boolean found = false;
        for (int i = 0; i < loanCount; i++) {
            loan ln = loans[i];
            if (ln != null && ln.getUser().equals(m) && !ln.isReturned()) {
                String overdueInfo = "";
                if (ln.isOverdue()) {
                    int days = (int) ln.getOverdueDays(LocalDate.now());
                    int fine = days * 10;
                    overdueInfo = " ⚠ OVERDUE by " + days + " days (Fine: " + fine + " NIS)";
                }
                System.out.println("  - " + ln.getBook().getTitle() + " | ISBN: " + ln.getBook().getIsbn() + " | Due: " + ln.getDueDate() + overdueInfo);
                found = true;
            }
        }
        if (!found) System.out.println("  No active loans.");
    }

    // ===================== BORROW / RETURN CD =====================
    private static void borrowCD() {
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }
        borrowCD(m);
    }

    private static void borrowCD(Member m) {
        String title = readLine("CD Title: ");
        CD cd = null;
        for (int i = 0; i < cdCount; i++)
            if (cds[i].getTitle().equalsIgnoreCase(title)) { 
                cd = cds[i]; 
                break; 
            }
        if (cd == null) { 
            System.out.println("✖ CD not found"); 
            return; 
        }
        if (!cd.isAvailable()) { 
            System.out.println("✖ CD already borrowed"); 
            return; 
        }

        MediaLoan ml = new MediaLoan(cd, LocalDate.now());
        mediaLoans[mediaLoanCount++] = ml;
        cd.markBorrowed();
        System.out.println("✔ CD borrowed successfully! 🎵");
        System.out.println("📅 Due date: " + ml.getDueDate());
    }

    private static void returnCD() {
        String username = readLine("Member username: ");
        Member m = findMember(username);
        if (m == null) { 
            System.out.println("✖ Member not found"); 
            return; 
        }
        returnCD(m);
    }

    private static void returnCD(Member m) {
        String title = readLine("CD Title: ");
        MediaLoan ml = null;
        for (int i = 0; i < mediaLoanCount; i++) {
            if (mediaLoans[i] != null && mediaLoans[i].getMedia() instanceof CD cd &&
                cd.getTitle().equalsIgnoreCase(title) && !mediaLoans[i].isReturned()) {
                ml = mediaLoans[i]; 
                break;
            }
        }
        if (ml == null) { 
            System.out.println("✖ No active CD loan found"); 
            return; 
        }
        ml.markReturned();
        System.out.println("✔ CD returned successfully! 🎵");
    }
    

    private static void checkAndSendOverdueReminder(Member m) {
        if (emailService == null) {
            return; // Skip silently if email service not available
        }
        
        int overdueCount = 0;
        StringBuilder overdueBooks = new StringBuilder();
        
        // Check each loan for this member
        for (int j = 0; j < loanCount; j++) {
            loan ln = loans[j];
            if (ln != null && ln.getUser().equals(m) && !ln.isReturned() && ln.isOverdue()) {
                overdueCount++;
                int days = (int) ln.getOverdueDays(LocalDate.now());
                int fine = days * 10;
                overdueBooks.append("\n  - ").append(ln.getBook().getTitle())
                           .append(" (").append(days).append(" days overdue, Fine: ")
                           .append(fine).append(" NIS)");
            }
        }
        
        // If member has overdue books, send email automatically
        if (overdueCount > 0) {
            String subject = "⚠ Library Overdue Books Alert";
            String message = "Dear " + m.getUsername() + ",\n\n" +
                            "You have " + overdueCount + " overdue book(s):" +
                            overdueBooks.toString() + "\n\n" +
                            "Please return them as soon as possible to avoid additional fines.\n\n" +
                            "Thank you,\nLibrary Management System";
            
            try {
                emailService.sendEmail(m.getEmail(), subject, message);
                System.out.println("📧 Automatic reminder sent to: " + m.getEmail());
            } catch (Exception e) {
                System.out.println("⚠ Email send failed: " + e.getMessage());
            }
        }
    }
    
   
    private static void sendOverdueEmailForLoan(Member m, loan ln) {
        if (emailService == null) {
            return;
        }
        
        int days = (int) ln.getOverdueDays(LocalDate.now());
        int fine = days * 10;
        
        String subject = "⚠ Late Return - Fine Applied";
        String message = "Dear " + m.getUsername() + ",\n\n" +
                        "You have returned the book '" + ln.getBook().getTitle() + 
                        "' late.\n\n" +
                        "Overdue: " + days + " days\n" +
                        "Fine: " + fine + " NIS\n\n" +
                        "Please pay your fine at the library.\n\n" +
                        "Thank you,\nLibrary Management System";
        
        try {
            emailService.sendEmail(m.getEmail(), subject, message);
            System.out.println("📧 Late return notification sent to: " + m.getEmail());
        } catch (Exception e) {
            System.out.println("⚠ Email send failed: " + e.getMessage());
        }
    }
    
  
    private static void sendOverdueReminders() {
        if (currentAdmin == null) {
            System.out.println("✖ Admin must login first!");
            return;
        }
        
        if (emailService == null) {
            System.out.println("✖ Email service not initialized!");
            return;
        }
        
        System.out.println("\n📧 Checking for overdue books...");
        int remindersSent = 0;
        
        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            int overdueCount = 0;
            StringBuilder overdueBooks = new StringBuilder();
            
            // Check each loan for this member
            for (int j = 0; j < loanCount; j++) {
                loan ln = loans[j];
                if (ln != null && ln.getUser().equals(m) && !ln.isReturned() && ln.isOverdue()) {
                    overdueCount++;
                    int days = (int) ln.getOverdueDays(LocalDate.now());
                    int fine = days * 10;
                    overdueBooks.append("\n  - ").append(ln.getBook().getTitle())
                               .append(" (").append(days).append(" days overdue, Fine: ")
                               .append(fine).append(" NIS)");
                }
            }
            
            // If member has overdue books, send email directly
            if (overdueCount > 0) {
                String subject = "Library Overdue Books Reminder";
                String message = "Dear " + m.getUsername() + ",\n\n" +
                                "You have " + overdueCount + " overdue book(s):" +
                                overdueBooks.toString() + "\n\n" +
                                "Please return them as soon as possible to avoid additional fines.\n\n" +
                                "Thank you,\nLibrary Management System";
                
                try {
                    // Send email directly using EmailService
                    emailService.sendEmail(m.getEmail(), subject, message);
                    System.out.println("✔ Email sent to: " + m.getUsername() + " (" + m.getEmail() + ") - " + overdueCount + " overdue book(s)");
                    remindersSent++;
                } catch (Exception e) {
                    System.out.println("✖ Failed to send email to " + m.getUsername() + ": " + e.getMessage());
                }
            }
        }
        
        if (remindersSent == 0) {
            System.out.println("✔ No overdue books found. No reminders sent.");
        } else {
            System.out.println("\n✔ Total reminders sent: " + remindersSent);
        }
    }
}*/