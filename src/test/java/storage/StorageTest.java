package storage;

import bank.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import user.User;
import utils.Budget;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;
import ui.FinanceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Tests for Storage persistence: addTransaction, save/load transactions.
 */
public class StorageTest {
    private static final Path TX_FILE = Path.of("transactions.txt");
    private static final Path BUD_FILE = Path.of("budgets.txt");
    private static final Path BANK_FILE = Path.of("banks.txt");

    private Storage storage;


    @BeforeEach
    public void setup() throws IOException {
        // Delete existing files to start fresh
        Files.deleteIfExists(TX_FILE);
        Files.deleteIfExists(BUD_FILE);
        Files.deleteIfExists(BANK_FILE);

        User.initialise();
        // Initialize User static fields
        User.getBanks().clear();
        User.getBudgets().clear();

        storage = new Storage();
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(TX_FILE);
        Files.deleteIfExists(BUD_FILE);
        Files.deleteIfExists(BANK_FILE);
    }

    @Test
    public void saveAndLoadBanks_shouldPersistBanks() {
        Bank bank = new Bank(0, Currency.THB, 100f, 0.04f);
        User.getBanks().add(bank);

        storage.saveBanks(User.getBanks());
        ArrayList<Bank> loaded = storage.loadBanks();

        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        assertEquals(bank.getId(), loaded.get(0).getId());
        assertEquals(bank.getBalance(), loaded.get(0).getBalance());
        assertEquals(bank.getCurrency(), loaded.get(0).getCurrency());
    }

    @Test
    public void saveAndLoadTransactions_shouldPersistTransactions() throws FinanceException {
        Bank bank = new Bank(0, Currency.THB, 100f, 0.04f);
        User.getBanks().add(bank);

        Transaction tx = new Transaction(50f, Category.FOOD, new Date(15, Month.JAN, 2025), Currency.THB, "Lunch");
        bank.getTransactions().add(tx);

        storage.saveTransactions(User.getBanks());

        // Reset transactions to test load
        bank.getTransactions().clear();

        storage.loadTransactions();

        assertEquals(1, bank.getTransactions().size());
        Transaction loaded = bank.getTransactions().get(0);
        assertEquals(tx.getValue(), loaded.getValue());
        assertEquals(tx.getCategory(), loaded.getCategory());
        assertEquals(tx.getTag(), loaded.getTag());
        assertEquals(tx.getCurrency(), loaded.getCurrency());
    }

    @Test
    public void saveAndLoadBudgets_shouldPersistBudgets() {
        Bank bank = new Bank(0, Currency.THB, 100f, 0.04f);
        User.getBanks().add(bank);

        Budget budget = new Budget(Category.FOOD, 200f, Currency.THB, Month.JAN, bank);
        User.getBudgets().add(budget);

        storage.saveBudgets(User.getBudgets());

        // Reset budgets to test load
        User.getBudgets().clear();

        ArrayList<Budget> loaded = storage.loadBudgets();
        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        Budget loadedBudget = loaded.get(0);
        assertEquals(budget.getBudget(), loadedBudget.getBudget());
        assertEquals(budget.getCategory(), loadedBudget.getCategory());
        assertEquals(budget.getMonth(), loadedBudget.getMonth());
        assertEquals(budget.getBank(), loadedBudget.getBank());
    }
}
