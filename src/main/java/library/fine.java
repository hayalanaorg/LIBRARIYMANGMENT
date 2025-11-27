package library;


public class fine {

    private library.user user;
    private double amount;

    public fine(user user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public user getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public void pay(double payment) {
        if (payment <= 0) return;
        amount -= payment;
        if (amount < 0) amount = 0;
    }

    public boolean isPaid() {
        return amount <= 0;
    }
}
