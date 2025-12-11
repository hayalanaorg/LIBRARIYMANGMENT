package library;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LoanTest { 

    private Member member;
    private Book book;
    private loan ln;

  
    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana", "mail@mail.com");
        book = new Book("Java", "Oracle", "111");
        ln = new loan(book, member);
    }

    // ============================================================
    // CONSTRUCTOR + GETTERS
    // ============================================================

    
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

 
    @Test
    void testSetReturned() {
        assertFalse(ln.isReturned());
        ln.setReturned(true);
        assertTrue(ln.isReturned());
    }


    @Test
    void testSetReturnedFalse() {
        ln.setReturned(true);
        ln.setReturned(false);
        assertFalse(ln.isReturned());
    }


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

    
    @Test
    void testIsOverdueTrue() throws Exception {
        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, LocalDate.now().minusDays(3));

        assertTrue(ln.isOverdue());
        assertTrue(ln.overdueDays() >= 3);
    }

  
    @Test
    void testIsOverdueFalse_NotPastDue() {
        assertFalse(ln.isOverdue());
    }

  
    @Test
    void testIsOverdueFalse_Returned() {
        ln.setReturned(true);
        assertFalse(ln.isOverdue());
    }

    // ============================================================
    // overdueDays()
    // ============================================================

  
    @Test
    void testOverdueDaysZeroWhenNotOverdue() {
        assertEquals(0, ln.overdueDays());
    }

    
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


    @Test
    void testGetOverdueDaysNowNull() {
        assertEquals(0, ln.getOverdueDays(null));
    }

 
    @Test
    void testGetOverdueDaysReturned() {
        ln.setReturned(true);
        assertEquals(0, ln.getOverdueDays(LocalDate.now()));
    }


    @Test
    void testGetOverdueDaysNotOverdue() {
        assertEquals(0, ln.getOverdueDays(LocalDate.now()));
    }


    @Test
    void testGetOverdueDaysOverdue() throws Exception {
        LocalDate today = LocalDate.now();

        Field f = loan.class.getDeclaredField("dueDate");
        f.setAccessible(true);
        f.set(ln, today.minusDays(7));

        assertEquals(7, ln.getOverdueDays(today));
    }

  
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


    @Test
    void testSetDueDate() {
        LocalDate newDate = LocalDate.now().plusDays(10);
        ln.setDueDate(newDate);
        assertEquals(newDate, ln.getDueDate());
    }

    @Test
    void testSetDueDatePast() {
        LocalDate past = LocalDate.now().minusDays(10);
        ln.setDueDate(past);
        assertEquals(past, ln.getDueDate());
    } 

 
    @Test
    void testSetDueDateDoesNotBreak() {
        assertDoesNotThrow(() -> ln.setDueDate(LocalDate.now()));
    }


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
