package library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class user {
    private String username;
    private String password;
    private String fullName;
    private BigDecimal fineBalance = BigDecimal.ZERO;
    private List<loan> loans = new ArrayList<>();

    public user(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }

    public List<loan> getLoans() { return loans; }

    public void addLoan(loan loan) { loans.add(loan); }

    public void addFine(BigDecimal amount) { fineBalance = fineBalance.add(amount); }

    public void payFine(BigDecimal amount) { fineBalance = fineBalance.subtract(amount); }

    public BigDecimal getFineBalance() { return fineBalance; }

    public boolean canBorrow() { return fineBalance.compareTo(BigDecimal.ZERO) == 0; }

    @Override
    public String toString() {
        return String.format("User: %s (%s) - Fine: %s", fullName, username, fineBalance);
    }
}
