package commands;

import bank.Bank;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddTransactionCommandTest {
    private static final Path TX_FILE = Path.of("transactions.txt");

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(TX_FILE);
        // Initialize User class (loads structure)
        User.initialise();

        // Completely reset lists to avoid leftover file data
        User.transactions = new ArrayList<>();
        User.banks = new ArrayList<>();
        User.budgets = new ArrayList<>();

        // Set up a test bank
        Bank testBank = new Bank(1, Currency.SGD, 1000f, 1.0f);
        User.addBank(testBank);
        User.currBank = testBank;
    }

    @Test
    public void addTransaction_validInput_transactionAddedSuccessfully() throws FinanceException {
        Transaction transaction = new Transaction(
                10.5f,
                Category.FOOD,
                new Date(10, Month.AUG, 2024),
                Currency.SGD
        );

        User.currBank.addTransactionToBank(transaction);

        assertEquals(1, User.currBank.getTransactions().size());

        Transaction storedTransaction = User.currBank.getTransactions().get(0);
        assertEquals(Category.FOOD, storedTransaction.getCategory());
        assertEquals(10.5f, storedTransaction.getValue());
    }


    @Test
    public void addTransaction_negativeValue_throwsException() {
        assertThrows(FinanceException.class, () -> new Transaction(
                -5.0f,
                Category.FOOD,
                new Date(1, Month.JAN, 2025),
                Currency.SGD
        ));

    }

    @Test
    public void addTransaction_invalidCategory_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Category.toCategory("invalidCategory"));
    }

    @Test
    public void addTransaction_nullDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                10.0f,
                Category.FOOD,
                null,
                Currency.SGD
        ));
    }
}
