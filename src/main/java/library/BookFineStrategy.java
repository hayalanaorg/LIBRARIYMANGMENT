package library;

public class BookFineStrategy implements FineStrategy {
    public int calculateFine(int overdueDays) {
        return overdueDays * 10; // 10 NIS per day
    }

	@Override
	public long calculateFine(long overdueDays) {
		return overdueDays * 10; // 10 NIS per day		
	}
}
