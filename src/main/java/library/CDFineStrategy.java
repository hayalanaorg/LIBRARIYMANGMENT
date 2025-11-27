package library;

public class CDFineStrategy implements FineStrategy {
    
    public int calculateFine(int overdueDays) {
        return overdueDays * 20; // 20 NIS per day
    }

	@Override
	public long calculateFine(long overdueDays) {
        return overdueDays * 20; // 20 NIS per day

	}
}
