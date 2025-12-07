package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MediaLoanTest {

    private Member member;

  
    @BeforeEach
    void setup() {
        member = new Member("1", "lana", "pass", "Lana", "lana@mail.com");
    }

    // --------------------------------------------------------
    // 1) BOOK → due = +28 days
    // --------------------------------------------------------

 
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

    @Test
    void testIsNotOverdue() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isOverdue());
    }

    // --------------------------------------------------------
    // 5) Deterministic overdueDays(today)
    // --------------------------------------------------------


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


    @Test
    void testGetBorrowDate() {
        LocalDate today = LocalDate.now();
        Book b = new Book("Java", "Oracle", "111");

        MediaLoan ml = new MediaLoan(b, today);

        assertEquals(today, ml.getBorrowDate());
    }


    @Test
    void testGetDueDateFixed() {
        Book b = new Book("Java", "Oracle", "111");

        LocalDate borrow = LocalDate.of(2024, 1, 1);
        MediaLoan ml = new MediaLoan(b, borrow);

        assertEquals(borrow.plusDays(28), ml.getDueDate());
    }



    @Test
    void testIsReturnedDirectly() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isReturned());
        ml.markReturned();
        assertTrue(ml.isReturned());
    }


    @Test
    void testGetOverdueDaysNotOverdue() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertEquals(0, ml.getOverdueDays());
    }


    @Test
    void testGetOverdueDaysOverdue() {
        Book b = new Book("Java", "Oracle", "111");

        LocalDate borrow = LocalDate.now().minusDays(30);
        MediaLoan ml = new MediaLoan(b, borrow);

        int days = ml.getOverdueDays();

        assertEquals(2, days);  // 30 - 28 = 2
    }
}
