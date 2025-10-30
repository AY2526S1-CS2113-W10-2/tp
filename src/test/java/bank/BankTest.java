package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Date;
import utils.Month;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.Currency.SGD;
import static utils.Currency.JPY;
import static utils.Currency.VND;

public class BankTest {
    private static final Path TX_FILE = Path.of("transactions.txt");
    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(TX_FILE);
        User.initialise();

        User.getBanks().clear();
        User.getBudgets().clear();

        Bank b1 = new Bank(0, SGD, 100, 0.4f);
        Bank b2 = new Bank(0, JPY, 1000, 2f);
        User.addBank(b1);
        User.addBank(b2);
        User.setCurrBank(b2);

    }

    @Test
    public void bank_setBalance_success(){
        Bank currBank = User.getCurrBank();
        float b1 = currBank.getBalance();
        assertEquals(1000, b1);
        currBank.setBalance(110);
        float b2 = currBank.getBalance();
        assertEquals(110, b2);
    }

    @Test
    public void bank_setNegativeBalance_exceptionThrown(){
        Bank currBank = User.getCurrBank();
        float b1 = currBank.getBalance();
        assertThrows(IllegalArgumentException.class, () -> {
            currBank.setBalance(-10);
        });
        float b2 = currBank.getBalance();
        assertEquals(b1, b2);
    }

    @Test
    public void bank_initialiseNegativeBalance_exceptionThrown(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Bank(0, VND, -100, 0.2f);
        });
    }

    @Test
    public void bank_setExchangeRate_success(){
        Bank currBank = User.getCurrBank();
        float b1 = currBank.getExchangeRate();
        assertEquals(2f, b1);
        currBank.setExchangeRate(0.5f);
        float b2 = currBank.getExchangeRate();
        assertEquals(0.5f, b2);
    }

    @Test
    public void bank_setNegativeExchangeRate_exceptionThrown(){
        Bank currBank = User.getCurrBank();
        float b1 = currBank.getExchangeRate();
        assertThrows(IllegalArgumentException.class, () -> {
            currBank.setExchangeRate(-0.3f);
        });
        float b2 = currBank.getExchangeRate();
        assertEquals(b1, b2);
    }

    @Test
    public void bank_initialiseNegativeExchangeRate_exceptionThrown(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Bank(0, VND, 0, -0.2f);
        });
    }

    @Test
    public void bank_addTransaction_success() throws FinanceException {
        Bank currBank = User.getCurrBank();
        float value = 9.8f;
        Category category = Category.FOOD;
        Date date = new Date(2, Month.APR, 2025);
        String tag = "Milo";
        Transaction transaction = new Transaction(value, category, date, SGD, tag);
        currBank.addTransactionToBank(transaction);
        Transaction addedTransaction = currBank.getTransactions().get(0);
        assertEquals(addedTransaction, transaction);
    }

    @Test
    public void bank_addTransactionNegativeValue_failure() {
        float value = -1 * 9.8f;
        Category category = Category.FOOD;
        Date date = new Date(2, Month.APR, 2025);
        String tag = "Milo";
        assertThrows(FinanceException.class, () -> {
            new Transaction(value, category, date, SGD, tag);
        });
    }

    @Test
    public void bank_addTransactionNullValues_failure(){
        float value = 9.8f;
        Category category = null;
        Date date = null;
        String tag = "Milo";
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(value, category, date, null, tag);
        });
    }
}
