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
 * Provides calculation logic for overdue fines across mixed media types
 * (Books and CDs) as part of Sprint 5.
 * <p>
 * This service implements <b>US5.3 – Mixed Media Handling</b>, which requires
 * calculating fines for both traditional {@link loan} (books) and new
 * {@link MediaLoan} items that may represent either a {@link CD} or a Book.
 * </p>
 *
 * <h2>Fine Policy:</h2>
 * <ul>
 *     <li><b>Books:</b> 10 NIS per overdue day → handled by {@link BookFineStrategy}</li>
 *     <li><b>CDs:</b> 20 NIS per overdue day → handled by {@link CDFineStrategy}</li>
 * </ul>
 *
 * <h3>Main Responsibilities:</h3>
 * <ul>
 *     <li>Compute total fines for multiple media loans</li>
 *     <li>Apply correct fine strategy based on media type</li>
 *     <li>Support deterministic calculations by allowing a custom "today" date</li>
 *     <li>Generate total fine for a specific {@link Member}</li>
 * </ul>
 *
 * @author
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class OverdueReportService {

    /**
     * Calculates the total fine for a list of {@link MediaLoan} objects.
     * <p>
     * Each loan determines its overdue days by comparing its due date with
     * the provided {@code today} date. If overdue, the appropriate fine strategy
     * ({@link BookFineStrategy} or {@link CDFineStrategy}) is applied depending
     * on whether the media is a Book or {@link CD}.
     * </p>
     *
     * @param loans list of active media loans (Book or CD)
     * @param today reference date used to compute overdue days
     * @return total fine amount in NIS
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

                // Determine fine strategy by media type
                if (media instanceof CD) {
                    strategy = new CDFineStrategy();     // 20 NIS/day
                } else {
                    strategy = new BookFineStrategy();    // 10 NIS/day
                }

                total += strategy.calculateFine(overdueDays);
            }
        }

        return total;
    }

    /**
     * Calculates the total overdue fine for a specific {@link Member},
     * combining both:
     * <ul>
     *     <li>traditional book loans ({@link loan})</li>
     *     <li>mixed media loans ({@link MediaLoan})</li>
     * </ul>
     *
     * <p>This method applies:</p>
     * <ul>
     *     <li>{@link BookFineStrategy} → Books = 10/day</li>
     *     <li>{@link CDFineStrategy} → CDs = 20/day</li>
     * </ul>
     *
     * @param m   the member whose fines should be computed
     * @param now the "current" date used to compare overdue days
     * @return total fine amount in NIS
     */
    public int calculateForUser(Member m, LocalDate now) {
        if (m == null || now == null) return 0;

        int total = 0;

        // ============================
        // 1) Book loans (loan)
        // ============================
        for (loan ln : m.getLoans()) {
            if (ln == null || ln.isReturned()) continue;

            long overdueDays = ln.getOverdueDays(now);
            if (overdueDays > 0) {

                BookFineStrategy strategy = new BookFineStrategy();
                total += strategy.calculateFine((int) overdueDays);
            }
        }

        // ============================
        // 2) MediaLoan loans (books + CDs)
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
                     // fallback = book
                    strategy = new BookFineStrategy();
                }

                total += strategy.calculateFine((int) overdueDays);
            }
        }

        return total;
    }
}
