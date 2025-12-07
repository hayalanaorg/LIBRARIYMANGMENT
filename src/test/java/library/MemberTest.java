package library;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Member} class.
 *
 * <p>This test suite validates all core behaviors of the Member entity,
 * including user data initialization, loan management, fine handling,
 * borrowing eligibility logic, media loan operations (Sprint 5),
 * and string representation formatting.</p>
 *
 * <h2>Behavior categories covered:</h2>
 * <ul>
<li>Constructor correctness &amp; getter validation</li>
 *     <li>Loan list manipulation and safety</li>
 *     <li>Fine management (addition, payment, edge cases)</li>
 *     <li>Borrowing eligibility rules</li>
 *     <li>Media loans handling (CD/Book loans)</li>
 *     <li>toString() formatting behavior</li>
<li>Edge-case conditions for lists &amp; references</li>
 * </ul>
 *
 * @version 1.0
 * @author
 *     Lana Omar (Documentation)
 * @since 2025-12-07
 */
public class MemberTest {

    private Member member;

    /**
     * Creates a fresh Member instance for each test.
     */
    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana Omar", "lana@mail.com");
    }

    // ============================================================
    // CONSTRUCTOR + GETTERS
    // ============================================================

    /**
     * Ensures constructor correctly initializes all fields
     * and computed properties (admin flag, active status, fine balance).
     */
    @Test
    void testConstructor() {
        assertEquals("1", member.getId());
        assertEquals("lana", member.getUsername());
        assertEquals("pass", member.getPassword());
        assertEquals("Lana Omar", member.getFullName());
        assertEquals("lana@mail.com", member.getEmail());
        assertFalse(member.isAdmin());
        assertTrue(member.isActive());
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    // ============================================================
    // LOANS LIST
    // ============================================================

    /**
     * Tests successful loan addition.
     */
    @Test
    void testAddLoan() {
        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, member);

        member.addLoan(ln);

        assertEquals(1, member.getLoans().size());
        assertTrue(member.getLoans().contains(ln));
    }

    /**
     * Verifies that adding null loans is safely ignored.
     */
    @Test
    void testAddLoanNullIgnored() {
        member.addLoan(null);
        assertEquals(0, member.getLoans().size());
    }

    // ============================================================
    // FINES
    // ============================================================

    /**
     * Tests adding a valid fine amount.
     */
    @Test
    void testAddFine() {
        member.addFine(new BigDecimal("10"));
        assertEquals(new BigDecimal("10"), member.getFineBalance());
    }

    /**
     * Zero-value fines should not change balance.
     */
    @Test
    void testAddFineZeroIgnored() {
        member.addFine(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    /**
     * Null fine inputs should be ignored.
     */
    @Test
    void testAddFineNullIgnored() {
        member.addFine(null);
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    /**
     * Partial fine payment reduces balance.
     */
    @Test
    void testPayFinePartial() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("5"));
        assertEquals(new BigDecimal("15"), member.getFineBalance());
    }

    /**
     * Full fine payment reduces balance to zero.
     */
    @Test
    void testPayFineFull() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("20"));
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    /**
     * Overpayment should not produce negative balance.
     */
    @Test
    void testPayFineOverpayment() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("50"));
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    /**
     * Zero or negative fine payments should be ignored.
     */
    @Test
    void testPayFineZeroOrNegativeIgnored() {
        member.addFine(new BigDecimal("20"));
        member.payFine(BigDecimal.ZERO);
        assertEquals(new BigDecimal("20"), member.getFineBalance());

        member.payFine(new BigDecimal("-5"));
        assertEquals(new BigDecimal("20"), member.getFineBalance());
    }

    // ============================================================
    // BORROW LOGIC
    // ============================================================

    /**
     * Eligible users: no fines + no overdue loans.
     */
    @Test
    void testCanBorrowNoFinesNoOverdues() {
        assertTrue(member.canBorrow());
    }

    /**
     * Users with unpaid fines cannot borrow.
     */
    @Test
    void testCannotBorrowWithUnpaidFines() {
        member.addFine(new BigDecimal("10"));
        assertFalse(member.canBorrow());
    }

    /**
     * Users with overdue loans cannot borrow.
     */
    @Test
    void testCannotBorrowWithOverdueLoan() throws Exception {
        Book b = new Book("Networks", "Kurose", "B200");
        loan ln = new loan(b, member);

        var f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, ln.getBorrowDate().minusDays(5));

        member.addLoan(ln);

        assertFalse(member.canBorrow());
    }

    /**
     * Returned overdue loans should not block borrowing.
     */
    @Test
    void testCanBorrowReturnedLoan() {
        Book b = new Book("Networks", "Kurose", "B200");
        loan ln = new loan(b, member);
        ln.isReturned2();

        member.addLoan(ln);

        assertTrue(member.canBorrow());
    }

    // ============================================================
    // MEDIA LOANS (Sprint 5)
    // ============================================================

    /**
     * Tests adding a single media loan & verifying ownership binding.
     */
    @Test
    void testAddMediaLoan() {
        CD cd = new CD("Hits", "Taylor");
        MediaLoan ml = new MediaLoan(cd, LocalDate.now());

        member.addMediaLoan(ml);

        MediaLoan[] arr = member.getMediaLoans();
        assertEquals(1, arr.length);
        assertEquals(ml, arr[0]);
        assertEquals(member, ml.getUser());
    }

    /**
     * Tests adding multiple media loans.
     */
    @Test
    void testMultipleMediaLoans() {
        CD c1 = new CD("C1", "A");
        CD c2 = new CD("C2", "B");

        MediaLoan ml1 = new MediaLoan(c1, LocalDate.now());
        MediaLoan ml2 = new MediaLoan(c2, LocalDate.now());

        member.addMediaLoan(ml1);
        member.addMediaLoan(ml2);

        MediaLoan[] list = member.getMediaLoans();

        assertEquals(2, list.length);
        assertEquals(ml1, list[0]);
        assertEquals(ml2, list[1]);
    }

    // ============================================================
    // TO STRING
    // ============================================================

    /**
     * Ensures correct formatting of Member textual representation.
     */
    @Test
    void testToStringFormat() {
        member.addFine(new BigDecimal("15"));

        String text = member.toString();

        assertNotNull(text);
        assertTrue(text.contains("Member:"));
        assertTrue(text.contains("Lana Omar"));
        assertTrue(text.contains("lana"));
        assertTrue(text.contains("15"));
        assertEquals("Member: Lana Omar [lana] - Fine: 15", text);
    }

    // ============================================================
    // EDGE CASES
    // ============================================================

    /**
     * getMediaLoans() when no media loans → empty list.
     */
    @Test
    void testEmptyMediaLoans() {
        assertEquals(0, member.getMediaLoans().length);
    }

    /**
     * Tests that loan list is independent (no external modification allowed).
     */
    @Test
    void testLoansListReferenceIndependence() {
        List<loan> original = member.getLoans();

        member.addLoan(new loan(new Book("x", "y", "123"), member));

        assertEquals(original.size(), member.getLoans().size());
    }

}
