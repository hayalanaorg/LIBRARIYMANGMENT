package library;

/**
 * A concrete implementation of the {@link FineStrategy} interface
 * used for calculating late return fines for books.
 * <p>
 * This strategy charges a flat rate of <strong>10 NIS per overdue day</strong>.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * BookFineStrategy fine = new BookFineStrategy();
 * int amount = fine.calculateFine(3);   // returns 30
 * </pre>
 *
 * @author Lana Omar
 * @version 1.0
 */
public class BookFineStrategy implements FineStrategy {

    /**
     * Calculates the fine based on the number of overdue days.
     * The fine is calculated as {@code overdueDays * 10}.
     *
     * @param overdueDays the number of days the book is overdue
     * @return the fine amount in NIS
     */
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 10; // 10 NIS per day
    }

    /**
     * Overloaded version of {@link #calculateFine(int)} that accepts a long.
     * <p>
     * Useful when overdue days exceed the integer range or come from a long-based date API.
     * </p>
     *
     * @param overdueDays the number of overdue days as a long
     * @return the fine amount in NIS as a long
     */
    @Override
    public long calculateFine(long overdueDays) {
        return overdueDays * 10; // 10 NIS per day
    }
}
