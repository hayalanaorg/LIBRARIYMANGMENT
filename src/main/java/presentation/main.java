package presentation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import library.Book;
import library.CD;
import library.MediaLoan;
import library.ConsoleEmailService;
import library.EmailMessage;
import library.loan;
import library.user;
import service.AdminServiceImpl;
import service.OverdueReportService;

/**
 * Main presentation class for the Library Management System.
 * Supports admin features, user management, loans, fines, and mixed-media reports (Sprint 5).
 */

public class main {

    private static final Scanner sc = new Scanner(System.in);
    private static final EmailMessage emailService = new ConsoleEmailService();
    private static final AdminServiceImpl adminService = new AdminServiceImpl(emailService);
    private static final OverdueReportService reportService = new OverdueReportService();

    public static void main(String[] args) {
        seedDemoData();
        runMenu();
    }

    // -----------------------------------------------
    // 1. DEMO DATA
    // -----------------------------------------------
    private static void seedDemoData() {
        user u1 = new user("alice", "pwd", "Alice Wonderland");
        Book b1 = new Book("Java Programming", "Author A", "B001");
        loan overdueLoan = new loan(b1, u1, LocalDate.now().minusDays(40), LocalDate.now().minusDays(12));
        u1.addLoan(overdueLoan);

        user u2 = new user("bob", "pwd", "Bob Builder");
        Book b2 = new Book("Python Basics", "Author B", "B002");
        loan activeLoan = new loan(b2, u2, LocalDate.now().minusDays(2), LocalDate.now().plusDays(26));
        u2.addLoan(activeLoan);

        adminService.addUser(u1);
        adminService.addUser(u2);
    }

    // -----------------------------------------------
    // 2. MENU LOOP
    // -----------------------------------------------
    private static void runMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== 📚 Library Admin Menu =====");
            System.out.println("1. Show all users and loans");
            System.out.println("2. Send reminders for overdue books");
            System.out.println("3. Mark a loan as returned");
            System.out.println("4. Pay fine for a user");
            System.out.println("5. Add new user");
            System.out.println("6. Add new book and assign loan");
            System.out.println("7. Show mixed-media overdue report (Sprint 5)");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" :{ showAllUsersAndLoans();break;}
                case "2" :{sendReminders();break;}
                case "3" : {markLoanReturned();break;}
                case "4" : {payFine();break;}
                case "5" : {addNewUser();break;}
                case "6" : {addBookAndLoan();break;}
                case "7" : {showMixedMediaReport();break;}
                case "8" : {
                    exit = true;
                    System.out.println(" Exiting Library System...");
                    break;
                }
                default : System.out.println(" Invalid choice. Please try again.");
            }
        }

        sc.close();
    }

    // -----------------------------------------------
    // 3. FEATURES
    // -----------------------------------------------
    private static void showAllUsersAndLoans() {
        System.out.println("\n--- Users and their loans ---");
        for (user u : adminService.getUsers()) {
            System.out.println("👤 " + u);
            u.getLoans().forEach(l -> System.out.println("   - Loan: " + l.getBook().getTitle()
                    + " | Due: " + l.getDueDate()
                    + " | Overdue: " + l.isOverdue()));
        }
    }

    private static void sendReminders() {
        System.out.println("\n Sending reminders...");
        adminService.sendReminders();
    }

    private static void markLoanReturned() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        user u = adminService.findUser(username);

        if (u == null) {
            System.out.println(" User not found.");
            return;
        }

        System.out.println("User's loans:");
        for (int i = 0; i < u.getLoans().size(); i++) {
            loan l = u.getLoans().get(i);
            System.out.println((i + 1) + ". " + l.getBook().getTitle()
                    + " | Due: " + l.getDueDate()
                    + " | Returned: " + l.isReturned());
        }

        System.out.print("Choose loan number to mark as returned: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;

        if (index >= 0 && index < u.getLoans().size()) {
            u.getLoans().get(index).setReturned(true);
            System.out.println(" Loan marked as returned.");
        } else {
            System.out.println(" Invalid selection.");
        }
    }

    private static void payFine() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        user u = adminService.findUser(username);

        if (u == null) {
            System.out.println(" User not found.");
            return;
        }

        System.out.println("Current fine balance: " + u.getFineBalance());
        System.out.print("Enter amount to pay: ");
        double amount = Double.parseDouble(sc.nextLine());
        u.payFine(java.math.BigDecimal.valueOf(amount));
        System.out.println(" New fine balance: " + u.getFineBalance());
    }

    private static void addNewUser() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter full name: ");
        String fullName = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        user newUser = new user(username, password, fullName);
        adminService.addUser(newUser);
        System.out.println(" User added successfully.");
    }

    private static void addBookAndLoan() {
        System.out.print("Enter book title: ");
        String title = sc.nextLine();
        System.out.print("Enter book author: ");
        String author = sc.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = sc.nextLine();

        Book newBook = new Book(title, author, isbn);

        System.out.print("Assign to username (leave blank to skip): ");
        String username = sc.nextLine();

        if (username.isEmpty()) {
            System.out.println(" Book created but not assigned.");
            return;
        }

        user u = adminService.findUser(username);
        if (u == null) {
            System.out.println(" User not found.");
            return;
        }

        loan newLoan = new loan(newBook, u);
        u.addLoan(newLoan);
        System.out.println(" Book assigned successfully.");
    }

    private static void showMixedMediaReport() {
        System.out.println("\n --- Mixed Media Fine Report (Sprint 5) ---");

        List<MediaLoan> loans = new ArrayList<>();

        // Book example: borrowed 35 days ago → 7 days overdue (10×7=70)
        Book book = new Book("Java Advanced", "Author A", "B001");
        loans.add(new MediaLoan(book, LocalDate.now().minusDays(35)));

        // CD example: borrowed 35 days ago → 28 days overdue (20×28=560)
        CD cd = new CD("Top 40", "DJ Mix", "CD01");
        loans.add(new MediaLoan(cd, LocalDate.now().minusDays(35)));

        int total = reportService.calculateTotalFine(loans, LocalDate.now());
        System.out.println(" Total Fine = " + total + " NIS");
    }
}
