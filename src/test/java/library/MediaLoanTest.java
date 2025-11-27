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
    // 1) Test constructor for BOOK → due = +28 days
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
    // 2) Test constructor for CD → due = +7 days
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
    // 3) Test overdue detection
    // --------------------------------------------------------
    @Test
    void testIsOverdue() {
        Book b = new Book("Java", "Oracle", "111");

        // due date = yesterday → overdue
        MediaLoan ml = new MediaLoan(b, LocalDate.now().minusDays(29));
        ml.setUser(member);

        assertTrue(ml.isOverdue());
    }

    // --------------------------------------------------------
    // 4) Test NOT overdue
    // --------------------------------------------------------
    @Test
    void testIsNotOverdue() {
        Book b = new Book("Java", "Oracle", "111");
        MediaLoan ml = new MediaLoan(b, LocalDate.now());

        assertFalse(ml.isOverdue());
    }

    // --------------------------------------------------------
    // 5) Test deterministic overdueDays(today)
    // --------------------------------------------------------
    @Test
    void testOverdueDaysWithDate() {
        Book b = new Book("Java", "Oracle", "111");

        // Due date = today - 3 days
        LocalDate borrow = LocalDate.of(2024, 1, 1);
        MediaLoan ml = new MediaLoan(b, borrow); // due = Jan 29 (28 days after)

        long days = ml.getOverdueDays(LocalDate.of(2024, 2, 1)); // 3 days late
        assertEquals(3, days);
    }

    // --------------------------------------------------------
    // 6) Test markReturned changes returned flag
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
    // 7) Test markReturned resets Book availability
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
    // 8) Test markReturned resets CD availability
    // --------------------------------------------------------
    @Test
    void testMarkReturnedCDAvailable() {
        CD cd = new CD("Hits", "Artist");
        cd.markBorrowed();

        MediaLoan ml = new MediaLoan(cd, LocalDate.now());
        ml.markReturned();

        assertTrue(cd.isAvailable());
    }
}
