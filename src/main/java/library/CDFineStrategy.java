package library;

/**
 * A concrete implementation of the {@link FineStrategy} interface used for
 * calculating late return fines specifically for CDs.
 * <p>
 * This strategy charges a flat rate of <strong>20 NIS per overdue day</strong>,
 * which is higher than book fines because CDs are considered higher-value media.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * CDFineStrategy fine = new CDFineStrategy();
 * int amount = fine.calculateFine(3);   // returns 60
 * </pre>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-06
 */
public class CDFineStrategy implements FineStrategy {

    /**
     * Calculates the fine based on the number of overdue days.
     * The fine is calculated as:
     * <pre>
     * overdueDays * 20
     * </pre>
     *
     * @param overdueDays the number of days the CD is overdue
     * @return the fine amount in NIS
     */
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 20; // 20 NIS per day
    }

    /**
     * Overloaded version of {@link #calculateFine(int)} that accepts a long.
     * <p>
     * Useful when the overdue days value comes from long-based date APIs.
     * </p>
     *
     * @param overdueDays the number of overdue days as a long
     * @return the fine amount in NIS as a long
     */
    @Override
    public long calculateFine(long overdueDays) {
        return overdueDays * 20; // 20 NIS per day
    }
}
