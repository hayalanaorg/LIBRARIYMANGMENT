package presentation;

import java.time.LocalDate;
import java.util.Scanner;

import library.Book;
import library.ConsoleEmailService;
import library.EmailMessage;
import library.loan;
import library.user;
import service.AdminServiceImpl;

public class main {
    public static void main(String[] args) {
        EmailMessage emailService = new ConsoleEmailService();
        AdminServiceImpl adminService = new AdminServiceImpl(emailService);

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        // ---- مثال أولي: إضافة بعض المستخدمين والكتب ----
        user u1 = new user("alice", "pwd", "Alice Wonderland");
        Book b1 = new Book("Java Programming", "Author A", "B001");
        loan overdue1 = new loan(b1, u1, LocalDate.now().minusDays(40), LocalDate.now().minusDays(12));
        u1.addLoan(overdue1);

        user u2 = new user("bob", "pwd", "Bob Builder");
        Book b2 = new Book("Python Basics", "Author B", "B002");
        loan active = new loan(b2, u2, LocalDate.now().minusDays(2), LocalDate.now().plusDays(26));
        u2.addLoan(active);

        adminService.addUser(u1);
        adminService.addUser(u2);

        while (!exit) {
            System.out.println("\n--- Library Admin Menu ---");
            System.out.println("1. Show all users and loans");
            System.out.println("2. Send reminders for overdue books");
            System.out.println("3. Mark a loan as returned");
            System.out.println("4. Pay fine for a user");
            System.out.println("5. Add new user");
            System.out.println("6. Add new book and assign loan");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // تنظيف buffer

            switch (choice) {
                case 1: {
                    System.out.println("\n--- Users and their loans ---");
                    for (user u : adminService.getUsers()) {
                        System.out.println(u);
                        u.getLoans().forEach(l -> System.out.println("   Loan: " + l.getBook().getTitle() +
                                ", Due: " + l.getDueDate() + ", Overdue: " + l.isOverdue()));
                    }
                }
                break;
                case 2 : {
                    System.out.println("\n--- Sending reminders ---");
                    adminService.sendReminders();
                }
                break;
                case 3 : {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    user u = adminService.findUser(username); // <--- تصحيح هنا
                    if (u != null) {
                        System.out.println("User's loans:");
                        int index = 1;
                        for (loan l : u.getLoans()) {
                            System.out.println(index + ". " + l.getBook().getTitle() +
                                    ", Due: " + l.getDueDate() + ", Returned: " + l.isReturned());
                            index++;
                        }
                        System.out.print("Choose loan to mark as returned: ");
                        int loanIndex = sc.nextInt() - 1;
                        sc.nextLine();
                        if (loanIndex >= 0 && loanIndex < u.getLoans().size()) {
                            u.getLoans().get(loanIndex).setReturned(true);
                            System.out.println("Loan marked as returned.");
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                }
                break;
                case 4 : {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    user u = adminService.findUser(username); // <--- تصحيح هنا
                    if (u != null) {
                        System.out.println("Current fine balance: " + u.getFineBalance());
                        System.out.print("Enter amount to pay: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();
                        u.payFine(java.math.BigDecimal.valueOf(amount));
                        System.out.println("New fine balance: " + u.getFineBalance());
                    } else {
                        System.out.println("User not found.");
                    }
                }
                break;
                case 5 : {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter full name: ");
                    String fullName = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    user newUser = new user(username, password, fullName);
                    adminService.addUser(newUser);
                    System.out.println("User added.");
                }
                break;
                case 6 : {
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter book author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbn = sc.nextLine();
                    Book newBook = new Book(title, author, isbn);

                    System.out.print("Assign to username (or leave blank for unassigned): ");
                    String username = sc.nextLine();
                    if (!username.isEmpty()) {
                        user u = adminService.findUser(username); // <--- تصحيح هنا
                        if (u != null) {
                            loan newLoan = new loan(newBook, u);
                            u.addLoan(newLoan);
                            System.out.println("Book loan assigned to user.");
                        } else {
                            System.out.println("User not found, book not assigned.");
                        }
                    } else {
                        System.out.println("Book added but not assigned.");
                    }
                }
                break;
                case 7 :{
                    exit = true;
                    System.out.println("Exiting...");
                }
                break;
                default : System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}
