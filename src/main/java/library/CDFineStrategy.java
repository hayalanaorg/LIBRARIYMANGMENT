package library;

/**
 * Strategy for calculating fines for CDs (20 NIS per day)
 */
public class CDFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 20;
    }
}
