package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.Currency.SGD;
import static utils.Currency.JPY;
import static utils.Currency.CNY;

public class BankTest {
    private static final Path TX_FILE = Path.of("transactions.txt");
    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(TX_FILE);
        User.initialise();
        Bank b1 = new Bank(0, SGD, 100, 0.4f);
        Bank b2 = new Bank(0, JPY, 1000, 2f);
        User.addBank(b1);
        User.addBank(b2);
        User.currBank = b2;

    }

    @Test
    public void bank_setBalance_success(){
        float b1 = User.currBank.getBalance();
        User.currBank.setBalance(110);
        float b2 = User.currBank.getBalance();
        assertEquals(110, b2);
    }

    @Test
    public void bank_setNegativeBalance_exceptionThrown(){
        float b1 = User.currBank.getBalance();
        assertThrows(IllegalArgumentException.class, () -> {
            User.currBank.setBalance(-10);
        });
        float b2 = User.currBank.getBalance();
        assertEquals(b1, b2);
    }

    @Test
    public void bank_initialiseNegativeBalance_exceptionThrown(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Bank(0, CNY, -100, 0.2f);
        });
    }

    @Test
    public void bank_setExchangeRate_success(){
        float b1 = User.currBank.getExchangeRate();
        User.currBank.setExchangeRate(0.5f);
        float b2 = User.currBank.getExchangeRate();
        assertEquals(0.5f, b2);
    }

    @Test
    public void bank_setNegativeExchangeRate_exceptionThrown(){
        float b1 = User.currBank.getExchangeRate();
        assertThrows(IllegalArgumentException.class, () -> {
            User.currBank.setExchangeRate(-0.3f);
        });
        float b2 = User.currBank.getExchangeRate();
        assertEquals(b1, b2);
    }

    @Test
    public void bank_initialiseNegativeExchangeRate_exceptionThrown(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Bank(0, CNY, 0, -0.2f);
        });
    }

    @Test
    public void BankAddTransactionSuccess() throws FinanceException {
        float value = 9.8f;
        Category category = Category.FOOD;
        Date date = new Date(2, Month.APR, 2025);
        Transaction transaction = new Transaction(value, category, date, SGD);
        User.currBank.addTransactionToBank(transaction);
        Transaction addedTransaction = User.currBank.getTransactions().get(0);
        assertEquals(addedTransaction, transaction);
    }

    @Test
    public void BankAddTransactionFailureNegativeValue() {
        float value = -1 * 9.8f;
        Category category = Category.FOOD;
        Date date = new Date(2, Month.APR, 2025);
        assertThrows(FinanceException.class, () -> {
                    new Transaction(value, category, date, SGD);
                });
    }

    @Test
    public void BankAddTransactionFailureNullValues(){
        float value = 9.8f;
        Category category = null;
        Date date = null;
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(value, category, date, null);
        });
    }



}
