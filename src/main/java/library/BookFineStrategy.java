package library;

/**
 * Strategy for calculating fines for books (10 NIS per day)
 */
public class BookFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 10;
    }
}
