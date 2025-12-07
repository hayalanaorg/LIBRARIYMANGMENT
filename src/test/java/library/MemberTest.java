package library;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MemberTest {

    private Member member;


    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana Omar", "lana@mail.com");
    }

    // ============================================================
    // CONSTRUCTOR + GETTERS
    // ============================================================


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

    @Test
    void testAddLoan() {
        Book b = new Book("Java", "Oracle", "111");
        loan ln = new loan(b, member);

        member.addLoan(ln);

        assertEquals(1, member.getLoans().size());
        assertTrue(member.getLoans().contains(ln));
    }

    
    @Test
    void testAddLoanNullIgnored() {
        member.addLoan(null);
        assertEquals(0, member.getLoans().size());
    }

    // ============================================================
    // FINES
    // ============================================================


    @Test
    void testAddFine() {
        member.addFine(new BigDecimal("10"));
        assertEquals(new BigDecimal("10"), member.getFineBalance());
    }


    @Test
    void testAddFineZeroIgnored() {
        member.addFine(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

  
    @Test
    void testAddFineNullIgnored() {
        member.addFine(null);
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

    
    @Test
    void testPayFinePartial() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("5"));
        assertEquals(new BigDecimal("15"), member.getFineBalance());
    }


    @Test
    void testPayFineFull() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("20"));
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }



    @Test
    void testPayFineOverpayment() {
        member.addFine(new BigDecimal("20"));
        member.payFine(new BigDecimal("50"));
        assertEquals(BigDecimal.ZERO, member.getFineBalance());
    }

  
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

   
    @Test
    void testCanBorrowNoFinesNoOverdues() {
        assertTrue(member.canBorrow());
    }

 
    @Test
    void testCannotBorrowWithUnpaidFines() {
        member.addFine(new BigDecimal("10"));
        assertFalse(member.canBorrow());
    }

    
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


    @Test
    void testEmptyMediaLoans() {
        assertEquals(0, member.getMediaLoans().length);
    }

   
    @Test
    void testLoansListReferenceIndependence() {
        List<loan> original = member.getLoans();

        member.addLoan(new loan(new Book("x", "y", "123"), member));

        assertEquals(original.size(), member.getLoans().size());
    }

}
