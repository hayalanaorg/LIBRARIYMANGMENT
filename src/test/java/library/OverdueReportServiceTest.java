package library;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import service.OverdueReportService;

/**
 * Sprint 5 – Mixed Media Report test
 */
public class OverdueReportServiceTest {

    @Test
    void testMixedMediaFineCalculation() {
        // كتاب – 28 يوم استعارة → الآن بعد 35 يوم = 7 أيام تأخير × 10 = 70
        Book book = new Book("Java Programming", "Author A", "B001");
        MediaLoan bookLoan = new MediaLoan(book, LocalDate.now().minusDays(35));

        // CD – 7 أيام استعارة → الآن بعد 35 يوم = 28 أيام تأخير × 20 = 560
        CD cd = new CD("Top 40", "DJ Mix", "CD01");
        MediaLoan cdLoan = new MediaLoan(cd, LocalDate.now().minusDays(35));

        List<MediaLoan> loans = new ArrayList<>();
        loans.add(bookLoan);
        loans.add(cdLoan);

        OverdueReportService service = new OverdueReportService();
        int totalFine = service.calculateTotalFine(loans, LocalDate.now());

        // إجمالي الغرامة = 630 NIS
        assertEquals(630, totalFine);
    }
}
