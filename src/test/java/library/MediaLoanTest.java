package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link MediaLoan} class.
 *
 * <p>This test suite verifies loan behavior for both Books and CDs,
 * including due date calculation, overdue detection, return logic,
 * availability restoration, and deterministic overdue-day computation.</p>
 *
 * <h2>Behaviors tested:</h2>
 * <ul>
 *     <li>Correct due dates for books (+28 days) and CDs (+7 days)</li>
 *     <li>Overdue detection for different borrow dates</li>
 *     <li>Accurate overdueDays(today) calculation</li>
 *     <li>Returned status changes correctly</li>
 *     <li>Book and CD availability resets when returned</li>
 *     <li>Borrow date correctness</li>
 *     <li>Direct overdueDays() evaluation</li>
 * </ul>
 *
 * @version 1.0
 * @author
 *     Lana Omar (Documentation)
 * @since 2025-12-07
 */
public class MediaLoanTest {

    private Member member;

    /**
     * Prepares a reusable Member object for all tests.
     */
    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
    }

    // --------------------------------------------------------
    // 1) BOOK → due = +28 days
    // --------------------------------------------------------

    /**
     * Tests that a book loan assigns a correct 28-day due date.
     */
    @Test
    void testBookLoanDueDate() {
        Book b = new Book("Java", "Oracle", "111");
        LocalDate today = LocalDate.now();

        MediaLoan ml = new MediaLoan(b, today);
        ml.setUser(member);

        assertEquals(today.plusDays(28), ml.getDueDate());
        assertEquals(b, ml.getMedia());
        assertEquals(member, ml.getUser());
    }

    // --------------------------------------------------------
    // 2) CD → due = +7 days
    // --------------------------------------------------------

    /**
     * Tests that a CD loan assigns a correct 7-day due date.
     */
    @Test
    void testCDLoanDueDate() {
        CD cd = new CD("Hits", "Artist");
        LocalDate today = LocalDate.now();

        MediaLoan ml = new MediaLoan(cd, today);
        ml.setUser(member);

        assertEquals(today.plusDays(7), ml.getDueDate());
        assertEquals(cd, ml.getMedia());
    }

    // --------------------------------------------------------
    // 3) Overdue detection
    // --------------------------------------------------------

    /**
     * Ensures overdue loans are detected correctly.
     */
    @Test
    void testIsOverdue() {
        Book b = new Book("Java", "Oracle", "111");

        MediaLoan ml = new MediaLoan(b, LocalDate.now().minusDays(29));
        ml.setUser(member);

        assertTrue(ml.isOverdue());
    }

    // --------------------------------------------------------
    // 4) Not overdue
    // --------------------------------------------------------

    /**
     * Loans with due dates not passed should not be marked overdue.
     */
    @Test
    void testIsNotOverdue() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isOverdue());
    }

    // --------------------------------------------------------
    // 5) Deterministic overdueDays(today)
    // --------------------------------------------------------

    /**
     * Tests precise overdue-day calculation using a supplied date.
     */
    @Test
    void testOverdueDaysWithDate() {
        Book b = new Book("Java", "Oracle", "111");

        LocalDate borrow = LocalDate.of(2024, 1, 1);
        MediaLoan ml = new MediaLoan(b, borrow);

        long days = ml.getOverdueDays(LocalDate.of(2024, 2, 1)); // 3 days late
        assertEquals(3, days);
    }

    // --------------------------------------------------------
    // 6) Returned flag update
    // --------------------------------------------------------

    /**
     * Ensures markReturned() sets returned = true.
     */
    @Test
    void testMarkReturned() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isReturned());
        ml.markReturned();
        assertTrue(ml.isReturned());
    }

    // --------------------------------------------------------
    // 7) Book availability reset
    // --------------------------------------------------------

    /**
     * Returned books must become available again.
     */
    @Test
    void testMarkReturnedBookAvailable() {
        Book b = new Book("Java", "Oracle", "111");
        b.markBorrowed();

        MediaLoan ml = new MediaLoan(b, LocalDate.now());
        ml.markReturned();

        assertTrue(b.isAvailable());
    }

    // --------------------------------------------------------
    // 8) CD availability reset
    // --------------------------------------------------------

    /**
     * Returned CDs must become available again.
     */
    @Test
    void testMarkReturnedCDAvailable() {
        CD cd = new CD("Hits", "Artist");
        cd.markBorrowed();

        MediaLoan ml = new MediaLoan(cd, LocalDate.now());
        ml.markReturned();

        assertTrue(cd.isAvailable());
    }

    // --------------------------------------------------------
    // Additional behavior tests
    // --------------------------------------------------------

    /**
     * Tests that borrow date is stored correctly.
     */
    @Test
    void testGetBorrowDate() {
        LocalDate today = LocalDate.now();
        Book b = new Book("Java", "Oracle", "111");

        MediaLoan ml = new MediaLoan(b, today);

        assertEquals(today, ml.getBorrowDate());
    }

    /**
     * Ensures due date is calculated correctly for fixed borrow dates.
     */
    @Test
    void testGetDueDateFixed() {
        Book b = new Book("Java", "Oracle", "111");

        LocalDate borrow = LocalDate.of(2024, 1, 1);
        MediaLoan ml = new MediaLoan(b, borrow);

        assertEquals(borrow.plusDays(28), ml.getDueDate());
    }

    /**
     * Verifies returned flag updates directly through markReturned().
     */
    @Test
    void testIsReturnedDirectly() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isReturned());
        ml.markReturned();
        assertTrue(ml.isReturned());
    }

    /**
     * Tests getOverdueDays() for non-overdue loans (should return 0).
     */
    @Test
    void testGetOverdueDaysNotOverdue() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertEquals(0, ml.getOverdueDays());
    }

    /**
     * Tests overdue-day computation for overdue media.
     */
    @Test
    void testGetOverdueDaysOverdue() {
        Book b = new Book("Java", "Oracle", "111");

        LocalDate borrow = LocalDate.now().minusDays(30);
        MediaLoan ml = new MediaLoan(b, borrow);

        int days = ml.getOverdueDays();

        assertEquals(2, days);  // 30 - 28 = 2
    }
}
