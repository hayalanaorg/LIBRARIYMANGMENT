package service;

import java.time.LocalDate;
import java.util.List;

import library.BookFineStrategy;
import library.CD;
import library.CDFineStrategy;
import library.FineStrategy;
import library.MediaLoan;
import library.Member;
import library.loan;

/**
 * Sprint 5 – Overdue Report Service
 * Calculates total fines for all borrowed media (books & CDs)
 *
 * US5.3 – Mixed Media Handling
 */
public class OverdueReportService {

    /**
     * Calculates total fines for all media loans using the given "today" date.
     *
     * @param loans list of active media loans
     * @param today reference date for overdue calculation
     * @return total fine amount (NIS)
     */
    public int calculateTotalFine(List<MediaLoan> loans, LocalDate today) {
        int total = 0;

        if (loans == null || today == null) {
            return 0;
        }

        for (MediaLoan loan : loans) {
            long overdueDays = loan.getOverdueDays(today);

            if (overdueDays > 0) {
                Object media = loan.getMedia();
                FineStrategy strategy;

                if (media instanceof CD) {
                    strategy = new CDFineStrategy();
                } else {
                    strategy = new BookFineStrategy();
                }

                total += strategy.calculateFine(overdueDays);
            }
        }

        return total;
    }

    public int calculateForUser(Member m, LocalDate now) {
        if (m == null || now == null) return 0;

        int total = 0;

    
        for (loan ln : m.getLoans()) {
            if (ln == null || ln.isReturned()) continue;

            long overdueDays = ln.getOverdueDays(now);
            if (overdueDays > 0) {
                // Book fine = 10 NIS per day
                BookFineStrategy strategy = new BookFineStrategy();
                total += strategy.calculateFine((int) overdueDays);
            }
        }

        // ============================
        // 2) MEDIA LOANS (MediaLoan[])
        // ============================
        for (MediaLoan ml : m.getMediaLoans()) {
            if (ml == null || ml.isReturned()) continue;

            long overdueDays = ml.getOverdueDays(now);
            if (overdueDays > 0) {

                FineStrategy strategy;

                // CD = 20 NIS/day
                if (ml.getMedia() instanceof CD) {
                    strategy = new CDFineStrategy();
                } else {
                    // fallback (book or unknown)
                    strategy = new BookFineStrategy();
                }

                total += strategy.calculateFine((int) overdueDays);
            }
        }

        return total;
    }

}
