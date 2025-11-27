package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import service.OverdueReportService;

public class OverdueReportServiceTest {

    // ===================================================================
    // 1) Mixed Media Test (Book + CD)  — يغطي calculateTotalFine()
    // ===================================================================
    @Test
    void testMixedMediaFineCalculation() {

        LocalDate today = LocalDate.now();

        // Book → due after 28 days → now 35 days → 7 overdue
        Book book = new Book("Java Programming", "Author A", "B001");
        MediaLoan bookLoan = new MediaLoan(book, today.minusDays(35));

        // CD → due after 7 days → now 35 days → 28 overdue
        CD cd = new CD("Top 40 Hits", "DJ Mix");
        MediaLoan cdLoan = new MediaLoan(cd, today.minusDays(35));

        List<MediaLoan> loans = new ArrayList<>();
        loans.add(bookLoan);
        loans.add(cdLoan);

        OverdueReportService service = new OverdueReportService();
        int totalFine = service.calculateTotalFine(loans, today);

        // book = 7 × 10 = 70
        // cd   = 28 × 20 = 560
        assertEquals(630, totalFine);
    }

    // ===================================================================
    // 2) Test calculateTotalFine with no overdue
    // ===================================================================
    @Test
    void testTotalFineNoOverdue() {

        LocalDate today = LocalDate.now();

        Book book = new Book("Clean Code", "Bob", "B002");
        MediaLoan loan = new MediaLoan(book, today.minusDays(10)); // not overdue (10 < 28)

        List<MediaLoan> loans = new ArrayList<>();
        loans.add(loan);

        OverdueReportService service = new OverdueReportService();
        assertEquals(0, service.calculateTotalFine(loans, today));
    }

    // ===================================================================
    // 3) Test calculateTotalFine with null cases
    // ===================================================================
    @Test
    void testCalculateTotalFineNullInputs() {
        OverdueReportService service = new OverdueReportService();
        assertEquals(0, service.calculateTotalFine(null, LocalDate.now()));
        assertEquals(0, service.calculateTotalFine(new ArrayList<>(), null));
    }

    // ===================================================================
    // 4) Test calculateForUser() – Only Books
    // ===================================================================
    @Test
    void testCalculateForUserBooksOnly() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        Book b = new Book("Mips", "Hennessy", "B100");
        loan ln = new loan(b, m);

        // Force overdue → 5 days overdue
        var f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(5));

        m.addLoan(ln);

        OverdueReportService s = new OverdueReportService();

        // book = 5 × 10 = 50
        assertEquals(50, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 5) Test calculateForUser() – Returned loans ignored
    // ===================================================================
    @Test
    void testCalculateForUserReturnedLoanIgnored() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        Book b = new Book("Networks", "Kurose", "B200");
        loan ln = new loan(b, m);

        // overdue
        var f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(4));

        ln.isReturned2();    // returned → should not count
        m.addLoan(ln);

        OverdueReportService s = new OverdueReportService();

        assertEquals(0, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 6) Test calculateForUser() – Mixed: Books + CDs
    // ===================================================================
    @Test
    void testCalculateForUserMixedBooksAndCDs() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("2", "sara", "123", "Sara", "sara@mail.com");

        // ===== Book overdue 3 days =====
        Book book = new Book("AI", "Author X", "B300");
        loan ln = new loan(book, m);

        var fb = loan.class.getDeclaredField("dueDate");
        fb.setAccessible(true);
        fb.set(ln, today.minusDays(3));

        m.addLoan(ln);

        // ===== CD overdue 10 days =====
        CD cd = new CD("Pop Mix", "DJ");
        MediaLoan cdLoan = new MediaLoan(cd, today.minusDays(20)); // due after 7 days → overdue 13

        // add MediaLoan to member
        m.addMediaLoan(cdLoan);

        OverdueReportService s = new OverdueReportService();

        // book = 3 × 10 = 30
        // cd   = 13 × 20 = 260
        int expected = 30 + 260;

        assertEquals(expected, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 7) Test calculateForUser null cases
    // ===================================================================
    @Test
    void testCalculateForUserNullCases() {
        OverdueReportService s = new OverdueReportService();
        assertEquals(0, s.calculateForUser(null, LocalDate.now()));
        assertEquals(0, s.calculateForUser(new Member("1","a","b","c","x"), null));
    }
}
