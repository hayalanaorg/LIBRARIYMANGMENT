package library;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the {@link loan} class.
 *
 * <p>This test suite validates all behaviors of the loan system used in
 * the library project, including:</p>
 *
 * <ul>
 *     <li>Constructor initialization and getter correctness</li>
 *     <li>Return operations and book re-availability</li>
 *     <li>Overdue detection logic</li>
 *     <li>Overdue day calculations</li>
 *     <li>Setting a custom due date via reflection</li>
 *     <li>Error handling when reflection fails</li>
 * </ul>
 *
 * <p>These tests ensure compatibility with Sprint 4 and Sprint 5 features.</p>
 *
 * @author
 *     Lana Omar (Documentation)
 * @version 1.0
 * @since 2025-12-07
 */
public class LoanTest {

    private Member member;
    private Book book;
    private loan ln;

    /**
     * Initializes a sample loan before each test.
     */
    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        book = new Book("Java", "Oracle", "111");
        ln = new loan(book, member);
    }

    // ============================================================
    // CONSTRUCTOR + GETTERS
    // ============================================================

    /**
     * Ensures constructor assigns book, user, dates, and returned flag correctly.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals(book, ln.getBook());
        assertEquals(member, ln.getUser());
        assertNotNull(ln.getBorrowDate());
        assertEquals(ln.getBorrowDate().plusDays(28), ln.getDueDate());
        assertFalse(ln.isReturned());
    }

    // ============================================================
    // RETURN LOGIC
    // ============================================================

    /**
     * Verifies that the returned flag changes correctly.
     */
    @Test
    void testSetReturned() {
        assertFalse(ln.isReturned());
        ln.setReturned(true);
        assertTrue(ln.isReturned());
    }

    /**
     * Ensures returned flag can be toggled back.
     */
    @Test
    void testSetReturnedFalse() {
        ln.setReturned(true);
        ln.setReturned(false);
        assertFalse(ln.isReturned());
    }

    /**
     * Ensures {@code isReturned2()} marks the loan as returned
     * and restores book availability.
     */
    @Test
    void testIsReturned2() {
        book.markBorrowed();
        assertFalse(book.isAvailable());

        ln.isReturned2();

        assertTrue(ln.isReturned());
        assertTrue(book.isAvailable());
    }

    // ============================================================
    // OVERDUE LOGIC
    // ============================================================

    /**
     * Forces an overdue loan and verifies the overdue detection logic.
     */
    @Test
    void testIsOverdueTrue() throws Exception {
        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, LocalDate.now().minusDays(3));

        assertTrue(ln.isOverdue());
        assertTrue(ln.overdueDays() >= 3);
    }

    /**
     * A loan is not overdue when its due date is not passed.
     */
    @Test
    void testIsOverdueFalse_NotPastDue() {
        assertFalse(ln.isOverdue());
    }

    /**
     * A returned loan is never overdue.
     */
    @Test
    void testIsOverdueFalse_Returned() {
        ln.setReturned(true);
        assertFalse(ln.isOverdue());
    }

    // ============================================================
    // overdueDays()
    // ============================================================

    /**
     * Ensures non-overdue loans return zero overdue days.
     */
    @Test
    void testOverdueDaysZeroWhenNotOverdue() {
        assertEquals(0, ln.overdueDays());
    }

    /**
     * Returned loans always return zero overdue days.
     */
    @Test
    void testOverdueDaysWhenReturned() throws Exception {
        ln.setReturned(true);

        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, LocalDate.now().minusDays(5));

        assertEquals(0, ln.overdueDays());
    }

    // ============================================================
    // getOverdueDays(now)
    // ============================================================

    /**
     * Null date returns zero.
     */
    @Test
    void testGetOverdueDaysNowNull() {
        assertEquals(0, ln.getOverdueDays(null));
    }

    /**
     * Returned loans give zero overdue days regardless of date.
     */
    @Test
    void testGetOverdueDaysReturned() {
        ln.setReturned(true);
        assertEquals(0, ln.getOverdueDays(LocalDate.now()));
    }

    /**
     * Not overdue → zero overdue days.
     */
    @Test
    void testGetOverdueDaysNotOverdue() {
        assertEquals(0, ln.getOverdueDays(LocalDate.now()));
    }

    /**
     * Overdue by a specific number of days.
     */
    @Test
    void testGetOverdueDaysOverdue() throws Exception {
        LocalDate today = LocalDate.now();

        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(7));

        assertEquals(7, ln.getOverdueDays(today));
    }

    /**
     * Overdue multiple days scenario.
     */
    @Test
    void testGetOverdueDaysAfterMultipleDays() throws Exception {
        LocalDate today = LocalDate.now();

        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(12));

        assertEquals(12, ln.getOverdueDays(today));
    }

    // ============================================================
    // setDueDate()
    // ============================================================

    /**
     * Ensures due date can be changed via reflection.
     */
    @Test
    void testSetDueDate() {
        LocalDate newDate = LocalDate.now().plusDays(10);
        ln.setDueDate(newDate);
        assertEquals(newDate, ln.getDueDate());
    }

    /**
     * Allows setting a past due date.
     */
    @Test
    void testSetDueDatePast() {
        LocalDate past = LocalDate.now().minusDays(10);
        ln.setDueDate(past);
        assertEquals(past, ln.getDueDate());
    }

    /**
     * Ensures reflection does not throw unexpected exceptions.
     */
    @Test
    void testSetDueDateDoesNotBreak() {
        assertDoesNotThrow(() -> ln.setDueDate(LocalDate.now()));
    }

    /**
     * Ensures setDueDate() throws RuntimeException when reflection fails
     * by using a subclass that does NOT contain the 'dueDate' field.
     */
    @Test
    void testSetDueDateReflectionFailureThrowsRuntimeException() {

        // Subclass WITHOUT the dueDate field → reflection will fail
        class FaultyLoan extends loan {
            public FaultyLoan(Book b, Member m) {
                super(b, m);
            }
        }

        FaultyLoan faulty = new FaultyLoan(book, member);

        // Act + Assert → must throw RuntimeException from the catch block
        assertThrows(RuntimeException.class,
                () -> faulty.setDueDate(LocalDate.now()),
                "Expected RuntimeException when reflection fails");
    }

}
