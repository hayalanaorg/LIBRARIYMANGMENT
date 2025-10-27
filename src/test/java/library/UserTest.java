package library;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testCanBorrowWithoutFines() {
        user user = new user("u1", "pass", "Alice");
        assertTrue(user.canBorrow());
    }

    @Test
    public void testCannotBorrowWithFines() {
        user user = new user("u2", "pass", "Bob");
        user.addFine(BigDecimal.valueOf(20));
        assertFalse(user.canBorrow());
    }

    @Test
    public void testPayFinePartial() {
        user user = new user("u3", "pass", "Charlie");
        user.addFine(BigDecimal.valueOf(50));
        user.payFine(BigDecimal.valueOf(30));
        assertEquals(BigDecimal.valueOf(20), user.getFineBalance());
    }

    @Test
    public void testPayFineFull() {
        user user = new user("u4", "pass", "David");
        user.addFine(BigDecimal.valueOf(40));
        user.payFine(BigDecimal.valueOf(40));
        assertEquals(BigDecimal.ZERO, user.getFineBalance());
        assertTrue(user.canBorrow());
    }

    @Test
    public void testAddAndGetLoans() {
        user user = new user("u5", "pass", "Eve");
        loan l1 = new loan(null, user); // assuming you have a default constructor
        loan l2 = new loan(null, user);
        user.addLoan(l1);
        user.addLoan(l2);

        List<loan> loans = user.getLoans();
        assertEquals(2, loans.size());
        assertTrue(loans.contains(l1));
        assertTrue(loans.contains(l2));
    }

    @Test
    public void testToString() {
        user user = new user("u6", "pass", "Frank");
        user.addFine(BigDecimal.valueOf(15));
        String str = user.toString();
        assertTrue(str.contains("Frank"));
        assertTrue(str.contains("u6"));
        assertTrue(str.contains("15"));
    }
    
    @Test
    void testUserGetters() {
        user u = new user("u1", "pass123", "Alice");

        assertEquals("u1", u.getUsername());
        assertEquals("pass123", u.getPassword());
        assertEquals("Alice", u.getFullName());
    }

}
