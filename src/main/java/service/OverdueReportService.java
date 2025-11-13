package service;

import java.time.LocalDate;
import java.util.List;
import library.MediaLoan;
import library.CD;
import library.BookFineStrategy;
import library.CDFineStrategy;
import library.FineStrategy;

/**
 * Sprint 5 – Overdue Report Service
 * Calculates total fines for all borrowed media (books & CDs)
 * 
 * US5.3 – Mixed Media Handling
 * As a user, I want my overdue report to include both books and CDs
 * so that I know my full fines.
 * 
 * ✅ Acceptance: Fine summary is accurate across all media types.
 */
public class OverdueReportService {

    /**
     * Calculates total fines for all media loans as of today.
     * @param loans list of active media loans
     * @param today reference date for overdue calculation
     * @return total fine amount (NIS)
     */
    public int calculateTotalFine(List<MediaLoan> loans, LocalDate today) {
        int total = 0;

        for (MediaLoan loan : loans) {
            int overdueDays = loan.getOverdueDays();

            // فقط احسب الغرامة إذا فيه تأخير
            if (overdueDays > 0) {
                Object media = loan.getMedia();
                FineStrategy strategy;

                // لو كانت CD → 20 NIS باليوم
                if (media instanceof CD) {
                    strategy = new CDFineStrategy();
                } 
                // غير هيك (كتب) → 10 NIS باليوم
                else {
                    strategy = new BookFineStrategy();
                }

                total += strategy.calculateFine(overdueDays);
            }
        }

        return total;
    }
}
