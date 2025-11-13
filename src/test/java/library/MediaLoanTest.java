package library;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Sprint 5 – Borrow CD for 7 days
 */
public class MediaLoanTest {

    @Test
    void testCDDueDate7Days() {
        CD cd = new CD("Top Hits", "DJ Mix", "CD01");
        LocalDate borrow = LocalDate.of(2025, 1, 1);
        MediaLoan loan = new MediaLoan(cd, borrow);
        assertEquals(borrow.plusDays(7), loan.getDueDate());
    }

    @Test
    void testOverdueCalculationForCD() {
        CD cd = new CD("Top Hits", "DJ Mix", "CD01");
        LocalDate borrow = LocalDate.of(2025, 1, 1);
        MediaLoan loan = new MediaLoan(cd, borrow);
        // تاريخ الإرجاع كان 8 يناير → الآن 11 يناير = 3 أيام تأخير
        assertTrue(loan.isOverdue());
    }
}

