package library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a non-admin library user who can borrow books and CDs,
 * accumulate fines, and manage multiple types of loans.
 * <p>
 * This class extends {@link user} and adds member-specific attributes such as
 * email, fine balance, traditional book loans ({@link loan}), and
 * polymorphic media loans ({@link MediaLoan}) introduced in Sprint 5.
 * </p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *     <li>Manage personal loan history</li>
 *     <li>Track fines and payments</li>
 *     <li>Control borrowing permissions</li>
 *     <li>Support polymorphic media loaning</li>
 * </ul>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public class Member extends user {

    /** The member's registered email address. */
    private String email;

    /** Total outstanding fine amount in NIS. */
    private BigDecimal fineBalance = BigDecimal.ZERO;

    /** List of traditional book loans. */
    private List<loan> loans = new ArrayList<>();

    /** Array of polymorphic media loans (Books or CDs). */
    private List<MediaLoan> mediaLoans = new ArrayList<>();

    /** Number of media loans in the array. */
    private int mediaLoanCount = 0;

    /**
     * Creates a new member with the given details.
     *
     * @param id        unique identifier
     * @param username  login username
     * @param password  login password
     * @param fullName  member's real name
     * @param email     member's email address
     */
    public Member(String id, String username, String password, String fullName, String email) {
        super(id, username, password, fullName, false); // Members are not admins
        this.email = email;
    }

    /**
     * Returns the member's email.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the list of traditional book loans.
     *
     * @return list of {@link loan} objects
     */
    public List<loan> getLoans() {
        return loans;
    }

    /**
     * Adds a new loan to the member's record.
     *
     * @param loan the loan to add
     */
    public void addLoan(loan loan) {
        if (loan != null) {
            loans.add(loan);
        }
    }

    /**
     * Adds a fine to the member's balance.
     *
     * @param fine positive fine amount
     */
    public void addFine(BigDecimal fine) {
        if (fine != null && fine.compareTo(BigDecimal.ZERO) > 0) {
            fineBalance = fineBalance.add(fine);
        }
    }

    /**
     * Pays part (or all) of the outstanding fine balance.
     * Negative payments are ignored.
     *
     * @param amount amount to pay
     */
    public void payFine(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        fineBalance = fineBalance.subtract(amount);
        if (fineBalance.compareTo(BigDecimal.ZERO) < 0) {
            fineBalance = BigDecimal.ZERO;
        }
    }

    /**
     * Returns the current fine balance.
     *
     * @return outstanding fine amount
     */
    public BigDecimal getFineBalance() {
        return fineBalance;
    }

    /**
     * Determines whether the member is allowed to borrow new media.
     * Borrowing is blocked if:
     * <ul>
     *     <li>They have unpaid fines</li>
     *     <li>They have overdue and unreturned loans</li>
     * </ul>
     *
     * @return {@code true} if borrowing is allowed, otherwise {@code false}
     */
    public boolean canBorrow() {
        boolean hasUnpaidFines = fineBalance.compareTo(BigDecimal.ZERO) > 0;
        boolean hasOverdueLoans = loans.stream()
                .anyMatch(l -> !l.isReturned() && l.isOverdue());
        return !hasUnpaidFines && !hasOverdueLoans;
    }

    /**
     * Returns a formatted string representation of the member.
     *
     * @return a string containing name, username, and fine balance
     */
    @Override
    public String toString() {
        return String.format("Member: %s [%s] - Fine: %s",
                getFullName(), getUsername(), fineBalance);
    }

    /**
     * Adds a polymorphic media loan (Book or CD) to the member's record.
     * This is used in Sprint 5.
     *
     * @param loan media loan object
     */
    public void addMediaLoan(MediaLoan loan) {
        if (loan != null) {
        	mediaLoans.add(loan);
        
        loan.setUser(this);}
    }

    /**
     * Returns a copy of stored media loans.
     *
     * @return array of {@link MediaLoan} objects
     */
    public List<MediaLoan> getMediaLoans() {
        return new ArrayList<>(mediaLoans);
    }
}
