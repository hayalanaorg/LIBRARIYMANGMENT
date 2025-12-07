package library;

/**
 * A strategy interface for calculating fines for overdue library items.
 * <p>
 * Different media types (Books, CDs, etc.) may have different fine policies.
 * Implementations of this interface provide the logic for calculating fines
 * based on the number of overdue days.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * FineStrategy strategy = new BookFineStrategy();
 * long fine = strategy.calculateFine(5);   // 50 NIS
 * </pre>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public interface FineStrategy {

    /**
     * Calculates the fine based on the number of overdue days.
     * <p>
     * This method supports long values, making it suitable for date calculations
     * or large overdue periods.
     * </p>
     *
     * @param overdueDays number of days the item is overdue
     * @return the calculated fine in NIS
     */
    long calculateFine(long overdueDays);

    /**
     * Calculates the fine based on the number of overdue days.
     * <p>
     * Recommended for simple integer-based cases, such as fixed overdue limits.
     * </p>
     *
     * @param overdueDays number of days the item is overdue
     * @return the calculated fine in NIS
     */
    int calculateFine(int overdueDays);
}
