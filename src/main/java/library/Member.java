package library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member (non-admin user) that can borrow items
 * and accumulate fines.
 */
public class Member extends user {

    private String email;
    private BigDecimal fineBalance = BigDecimal.ZERO;
    private List<loan> loans = new ArrayList<>();
    private MediaLoan[] mediaLoans = new MediaLoan[100];
    private int mediaLoanCount = 0;


    public Member(String id, String username, String password, String fullName, String email) {
        // Member is not an admin
        super(id, username, password, fullName, false);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<loan> getLoans() {
        return loans;
    }

    public void addLoan(loan loan) {
        if (loan != null) {
            loans.add(loan);
        }
    }

    public void addFine(BigDecimal fine) {
        if (fine != null && fine.compareTo(BigDecimal.ZERO) > 0) {
            fineBalance = fineBalance.add(fine);
        }
    }

    public void payFine(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        fineBalance = fineBalance.subtract(amount);
        if (fineBalance.compareTo(BigDecimal.ZERO) < 0) {
            fineBalance = BigDecimal.ZERO;
        }
    }

    public BigDecimal getFineBalance() {
        return fineBalance;
    }

    /**
     * Borrowing is allowed only if there are no unpaid fines
     * and no overdue, unreturned loans.
     */
    public boolean canBorrow() {
        boolean hasUnpaidFines = fineBalance.compareTo(BigDecimal.ZERO) > 0;
        boolean hasOverdueLoans = loans.stream()
                .anyMatch(l -> !l.isReturned() && l.isOverdue());
        return !hasUnpaidFines && !hasOverdueLoans;
    }

    @Override
    public String toString() {
        return String.format("Member: %s [%s] - Fine: %s",
                getFullName(), getUsername(), fineBalance);
    }
    public void addMediaLoan(MediaLoan loan) {
        mediaLoans[mediaLoanCount++] = loan;
        loan.setUser(this);   // مهم جداً لسبرنت 5
    }

    public MediaLoan[] getMediaLoans() {
        MediaLoan[] copy = new MediaLoan[mediaLoanCount];
        for (int i = 0; i < mediaLoanCount; i++) {
            copy[i] = mediaLoans[i];
        }
        return copy;
    }

}
