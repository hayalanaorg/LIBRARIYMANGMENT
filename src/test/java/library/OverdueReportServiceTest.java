package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import service.OverdueReportService;

public class OverdueReportServiceTest {

    // ===================================================================
    // 1) Mixed Media Test (Book + CD) — calculateTotalFine()
    // ===================================================================


    @Test
    void testMixedMediaFineCalculation() {

        LocalDate today = LocalDate.now();

        // Book: overdue 7 days
        Book book = new Book("Java Programming", "Author A", "B001");
        MediaLoan bookLoan = new MediaLoan(book, today.minusDays(35));

        // CD: overdue 28 days
        CD cd = new CD("Top 40 Hits", "DJ Mix");
        MediaLoan cdLoan = new MediaLoan(cd, today.minusDays(35));

        List<MediaLoan> loans = new ArrayList<>();
        loans.add(bookLoan);
        loans.add(cdLoan);

        OverdueReportService service = new OverdueReportService();
        int totalFine = service.calculateTotalFine(loans, today);

        assertEquals(630, totalFine);
    }

    // ===================================================================
    // 2) Test calculateTotalFine with no overdue
    // ===================================================================


    @Test
    void testTotalFineNoOverdue() {

        LocalDate today = LocalDate.now();

        Book book = new Book("Clean Code", "Bob", "B002");
        MediaLoan loan = new MediaLoan(book, today.minusDays(10)); // not overdue

        List<MediaLoan> loans = new ArrayList<>();
        loans.add(loan);

        OverdueReportService service = new OverdueReportService();
        assertEquals(0, service.calculateTotalFine(loans, today));
    }

    // ===================================================================
    // 3) Null input handling
    // ===================================================================


    @Test
    void testCalculateTotalFineNullInputs() {
        OverdueReportService service = new OverdueReportService();
        assertEquals(0, service.calculateTotalFine(null, LocalDate.now()));
        assertEquals(0, service.calculateTotalFine(new ArrayList<>(), null));
    }

    // ===================================================================
    // 4) calculateForUser() – Only Books
    // ===================================================================


    @Test
    void testCalculateForUserBooksOnly() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        Book b = new Book("Mips", "Hennessy", "B100");
        loan ln = new loan(b, m);

        // Make overdue 5 days
        var f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(5));

        m.addLoan(ln);

        OverdueReportService s = new OverdueReportService();
        assertEquals(50, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 5) Returned loans should be ignored
    // ===================================================================


    @Test
    void testCalculateForUserReturnedLoanIgnored() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
        Book b = new Book("Networks", "Kurose", "B200");
        loan ln = new loan(b, m);

        var f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(4));

        ln.isReturned2(); // should be ignored
        m.addLoan(ln);

        OverdueReportService s = new OverdueReportService();
        assertEquals(0, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 6) Mixed: Books + CDs
    // ===================================================================


    @Test
    void testCalculateForUserMixedBooksAndCDs() throws Exception {

        LocalDate today = LocalDate.now();

        Member m = new Member("2", "sara", "123", "Sara", "sara@mail.com");

        // Book overdue 3 days
        Book book = new Book("AI", "Author X", "B300");
        loan ln = new loan(book, m);

        var fb = loan.class.getDeclaredField("dueDate");
        fb.setAccessible(true);
        fb.set(ln, today.minusDays(3));

        m.addLoan(ln);

        // CD overdue 13 days
        CD cd = new CD("Pop Mix", "DJ");
        MediaLoan cdLoan = new MediaLoan(cd, today.minusDays(20));

        m.addMediaLoan(cdLoan);

        OverdueReportService s = new OverdueReportService();

        int expected = (3 * 10) + (13 * 20);
        assertEquals(expected, s.calculateForUser(m, today));
    }

    // ===================================================================
    // 7) Null inputs for calculateForUser()
    // ===================================================================

   
    @Test
    void testCalculateForUserNullCases() {
        OverdueReportService s = new OverdueReportService();
        assertEquals(0, s.calculateForUser(null, LocalDate.now()));
        assertEquals(0, s.calculateForUser(new Member("1","a","b","c","x"), null));
    }

    @Test
    void testFineCalculationForNonCDMediaUsesBookStrategy() {

        OverdueReportService service = new OverdueReportService();

        Member m = new Member("1", "lana", "pass", "Lana", "lana@mail.com");

        // Use a fixed date to avoid timing differences
        LocalDate today = LocalDate.of(2025, 1, 1);
        LocalDate borrowDate = today.minusDays(40);

        Book book = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(book, borrowDate);
        m.addMediaLoan(ml);

        int result = service.calculateForUser(m, today);

        assertEquals(120, result, "Non-CD media should use BookFineStrategy");
    }


 
}
