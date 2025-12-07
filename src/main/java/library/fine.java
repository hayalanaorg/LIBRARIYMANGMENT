package library;

/**
 * Represents a financial fine associated with a library user.
 * <p>
 * A fine is typically applied when a user has overdue items or violates
 * library policies. This class stores the fine amount and provides
 * operations to pay the fine and check its status.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * fine f = new fine(member, 30.0);
 * f.pay(10);        // amount becomes 20
 * f.isPaid();       // false
 * </pre>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-06
 */
public class fine {

    /** The user who owes this fine. */
    private library.user user;

    /** The remaining amount of the fine in NIS. */
    private double amount;

    /**
     * Creates a new {@code fine} instance with a specified user and fine amount.
     *
     * @param user   the user who incurred the fine
     * @param amount the initial amount of the fine in NIS
     */
    public fine(user user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    /**
     * Returns the user associated with this fine.
     *
     * @return the user who owes the fine
     */
    public user getUser() {
        return user;
    }

    /**
     * Returns the remaining fine amount.
     *
     * @return the fine amount in NIS
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Applies a payment toward the fine.
     * <p>
     * Payments must be positive. Overpayment reduces the amount to zero
     * without making it negative.
     * </p>
     *
     * @param payment the amount to pay toward the fine
     */
    public void pay(double payment) {
        if (payment <= 0) return;
        amount -= payment;
        if (amount < 0) amount = 0;
    }

    /**
     * Checks whether the fine has been fully paid.
     *
     * @return {@code true} if the fine amount is zero or less, otherwise {@code false}
     */
    public boolean isPaid() {
        return amount <= 0;
    }
}
