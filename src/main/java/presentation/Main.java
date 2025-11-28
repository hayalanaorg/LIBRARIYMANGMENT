package presentation;

import library.*;
import service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    // Scanner واحد
    static Scanner input = new Scanner(System.in);

    // ===== Arrays =====
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

    // ========== MAIN ==========
    public static void main(String[] args) throws Exception {
    	
        AdminServiceImpl adminService = new AdminServiceImpl();
        BookServiceImpl bookService = new BookServiceImpl();

     // ============ REAL EMAIL NOTIFIER FOR PRODUCTION ============

        Dotenv dotenv = Dotenv.load();
        String emailUser = dotenv.get("EMAIL_USERNAME");
        String emailPass = dotenv.get("EMAIL_PASSWORD");

        // Create real email service
        EmailService realEmailService = new EmailService(emailUser, emailPass);

        // Create notifier that sends real emails
        Observer realEmailNotifier = new EmailNotifier(realEmailService);//////////////////////////////////////////////////////////////////////////////

        // Register it in admin service
        adminService.addObserver(realEmailNotifier);

        // ============================================================


        while (true) {
            printMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1 : adminLogin(adminService);break;
                case 2 : adminLogout(adminService);break;
                case 3 : addMember(adminService);break;
                case 4 : addBook(adminService, bookService);break;
                case 5 : searchBooks(bookService);break;
                case 6 : borrowBook(bookService);break;
                case 7 : returnBook(bookService);break;
                case 8 : payFine();break;
                case 9 : sendReminders(adminService);break;
                case 10: unregisterMember(adminService);break;
                case 11: showMembers();break;
                case 12: showBooks();break;

                // Sprint 5
                case 13 : addCD();break;
                case 14 : borrowCD();break;
                case 15 : returnCD();break;
                case 16 : showMixedMediaFineReport();break;
                case 17: showUserBorrowedBooks(); break;

                case 0 : {
                    System.out.println("Bye bye ");
                    return;
                }

                default : System.out.println("Invalid choice!");
            }
        }
    }


	// ========== MENU ==========
    private static void printMenu() {
        System.out.println("\n=========== Library Management ===========");
        System.out.println("1) Admin Login");
        System.out.println("2) Admin Logout");
        System.out.println("3) Add Member");
        System.out.println("4) Add Book");
        System.out.println("5) Search Book");
        System.out.println("6) Borrow Book");
        System.out.println("7) Return Book");
        System.out.println("8) Pay Fine");
        System.out.println("9) Send Reminders");
        System.out.println("10) Unregister Member");
        System.out.println("11) Show Members");
        System.out.println("12) Show Books");
        System.out.println("------------ Sprint 5 ------------");
        System.out.println("13) Add CD");
        System.out.println("14) Borrow CD");
        System.out.println("15) Return CD");
        System.out.println("16) Mixed Media Fine Report");
        System.out.println("17) Show User Borrowed Books");
        System.out.println("0) Exit");
        System.out.println("==========================================");
    }

    // ========== ADMIN ==========
    private static void adminLogin(AdminServiceImpl admin) {
        String username = readLine("Admin username: ");
        String password = readLine("Admin password: ");

        if (admin.login(username, password))
            System.out.println("✔ Logged in");
        else
            System.out.println("✖ Wrong credentials");
    }

    private static void adminLogout(AdminServiceImpl admin) {
        admin.logout();
        System.out.println("✔ Logged out");
    }

    // ========== MEMBERS ==========
    private static void addMember(AdminServiceImpl admin) {
        if (!admin.isLoggedIn()) {
            System.out.println("✖ Admin must login");
            return;
        }

        String id = readLine("Member ID: ");
        String username = readLine("Username: ");
        String password = readLine("Password: ");
        String name = readLine("Full Name: ");
        String email = readLine("Email: ");

        Member m = new Member(id, username, password, name, email);
        members[memberCount++] = m;
        admin.addUser(m);

        System.out.println("✔ Member added");
    }

    private static Member findMember(String username) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getUsername().equals(username))
                return members[i];
        }
        return null;
    }

    // ========== BOOKS ==========
    private static void addBook(AdminServiceImpl admin, BookServiceImpl bookService) {
        if (!admin.isLoggedIn()) {
            System.out.println("✖ Admin must login");
            return;
        }

        String title = readLine("Book title: ");
        String author = readLine("Author: ");
        String isbn = readLine("ISBN: ");

        Book b = new Book(title, author, isbn);
        books[bookCount++] = b;

        admin.addBook(b);
        bookService.addBook(b);

        System.out.println("✔ Book added");
    }

    private static Book findBook(String isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn().equals(isbn))
                return books[i];
        }
        return null;
    }

    private static void searchBooks(BookServiceImpl bookService) {
        String keyword = readLine("Search: ");

        var results = bookService.searchBook(keyword);

        if (results.isEmpty()) {
            System.out.println("No results.");
            return;
        }

        System.out.println("Results:");
        results.forEach(b ->
                System.out.println("- " + b.getTitle() + " | " + b.getAuthor()));
    }

    // ========== BORROW BOOK ==========
    private static void borrowBook(BookServiceImpl bookService) {
        String username = readLine("Member username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        String isbn = readLine("Book ISBN: ");
        Book b = findBook(isbn);

        if (b == null) {
            System.out.println("✖ Book not found");
            return;
        }

        try {
            loan ln = bookService.borrowBook(b, m);
            loans[loanCount++] = ln;
            loans[0].setDueDate(LocalDate.now().minusDays(3));//////////////////////////////////////////////////////////////////

            System.out.println("✔ Borrowed. Due = " + ln.getDueDate());
        } catch (Exception e) {
            System.out.println("✖ " + e.getMessage());
        }
    }

    // ========== RETURN BOOK ==========
    private static void returnBook(BookServiceImpl bookService) throws Exception {
        String username = readLine("Member username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        String isbn = readLine("Book ISBN: ");

        loan target = null;
        for (int i = 0; i < loanCount; i++) {
            if (loans[i] != null &&
                    loans[i].getUser().equals(m) &&
                    loans[i].getBook().getIsbn().equals(isbn) &&
                    !loans[i].isReturned()) {

                target = loans[i];
                break;
            }
        }

        if (target == null) {
            System.out.println("✖ No active loan found");
            return;
        }

        bookService.returnBook(target);
        System.out.println("✔ Returned");
        System.out.println("Fine balance: " + m.getFineBalance());
    }

    // ========== PAY FINE ==========
    private static void payFine() {
        String username = readLine("Member username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        BigDecimal amt = new BigDecimal(readLine("Amount: "));
        m.payFine(amt);

        System.out.println("✔ Fine paid");
    }

    // ========== REMINDERS ==========
    private static void sendReminders(AdminServiceImpl admin) {
        admin.sendReminders();
        System.out.println("✔ Reminders sent");
    }

    // ========== UNREGISTER ==========
    private static void unregisterMember(AdminServiceImpl admin) {
        String username = readLine("Username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        try {
            admin.unregisterUser(m);
            System.out.println("✔ Member unregistered");
        } catch (Exception e) {
            System.out.println("✖ " + e.getMessage());
        }
    }

    // ========== SHOW ==========
    private static void showMembers() {
        System.out.println("---- Members ----");
        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            System.out.println("- " + m.getUsername() + " | Fine: " + m.getFineBalance());
        }
    }

    private static void showBooks() {
        System.out.println("---- Books ----");

        for (int i = 0; i < bookCount; i++) {
            Book b = books[i];

            String status = b.isAvailable() ? "Available" : "Borrowed";

            // ابحث من المستعير
            String borrower = "";
            if (!b.isAvailable()) {
                for (int j = 0; j < loanCount; j++) {
                    loan ln = loans[j];
                    if (ln != null &&
                        ln.getBook().equals(b) &&
                        !ln.isReturned()) 
                    {
                        borrower = ln.getUser().getUsername();
                        break;
                    }
                }
            }

            if (borrower.isEmpty()) {
                System.out.println("- " + b.getTitle() + " (" + b.getIsbn() + ") [" + status + "]");
            } else {
                System.out.println("- " + b.getTitle() + " (" + b.getIsbn() + ") [Borrowed by: " + borrower + "]");
            }
        }
    }


    // ========== SPRINT 5 ==========
    private static void addCD() {
        String title = readLine("CD Title: ");
        String artist = readLine("Artist: ");

        CD cd = new CD(title, artist);
        cds[cdCount++] = cd;

        System.out.println("🎵 CD added");
    }

    private static void borrowCD() {
        String username = readLine("Member username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        String title = readLine("CD title: ");

        CD target = null;
        for (int i = 0; i < cdCount; i++) {
            if (cds[i].getTitle().equalsIgnoreCase(title)) {
                target = cds[i];
                break;
            }
        }

        if (target == null) {
            System.out.println("✖ CD not found");
            return;
        }
        if (!target.isAvailable()) {
            System.out.println("✖ CD already borrowed");
            return;
        }

        MediaLoan ml = new MediaLoan(target, LocalDate.now());
        mediaLoans[mediaLoanCount++] = ml;

        target.markBorrowed();
        System.out.println("🎵 Borrowed CD. Due = " + ml.getDueDate());
    }

    private static void returnCD() {
        String username = readLine("Member username: ");
        Member m = findMember(username);

        if (m == null) {
            System.out.println("✖ Member not found");
            return;
        }

        String title = readLine("CD title: ");

        MediaLoan target = null;
        for (int i = 0; i < mediaLoanCount; i++) {
            if (mediaLoans[i] != null &&
                    mediaLoans[i].getMedia() instanceof CD cd &&
                    cd.getTitle().equalsIgnoreCase(title) &&
                    !mediaLoans[i].isReturned()) {
                target = mediaLoans[i];
                break;
            }
        }

        if (target == null) {
            System.out.println("✖ No active CD loan");
            return;
        }

        target.markReturned();

        long overdueDays = target.getOverdueDays(LocalDate.now());
        if (overdueDays > 0) {
            int fine = new CDFineStrategy().calculateFine((int) overdueDays);
            m.addFine(BigDecimal.valueOf(fine));
            System.out.println("💸 Fine added: " + fine + " NIS");
        }

        System.out.println("🎵 CD returned!");
    }

    private static void showMixedMediaFineReport() {
        System.out.println("---- Mixed Media Fine Report ----");
        OverdueReportService report = new OverdueReportService();

        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            int totalFine = report.calculateForUser(m, LocalDate.now());
            System.out.println("- " + m.getUsername() + ": " + totalFine + " NIS");
        }
    }

    // ========== HELPERS ==========

    private static String readLine(String msg) {
        System.out.print(msg);
        return input.nextLine();
    }

    private static int readInt(String msg) {
        try {
            System.out.print(msg);
            return Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    // Console Observer for reminders
    static class ConsoleNotifier implements Observer {
        @Override
        public void notify(Member m, String message) {
            System.out.println("[REMINDER to " + m.getUsername() + "]: " + message);
        }
    }
    
    private static void showUserBorrowedBooks() {
        System.out.println("---- Borrowed Books per Member ----");

        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            System.out.println("\nUser: " + m.getUsername());
            System.out.println("Borrowed Books:");

            boolean found = false;

            for (int j = 0; j < loanCount; j++) {
                loan ln = loans[j];

                if (ln != null && ln.getUser().equals(m) && !ln.isReturned()) {
                    System.out.println(" - " + ln.getBook().getTitle() + " | ISBN: " + ln.getBook().getIsbn());
                    found = true;
                }
            }

            if (!found) {
                System.out.println("   No active loans.");
            }
        }
    }

    
    
    
}
