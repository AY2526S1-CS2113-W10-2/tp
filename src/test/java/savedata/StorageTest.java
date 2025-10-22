package savedata;

import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for Storage persistence: addTransaction, save/load transactions.
 */
public class StorageTest {
    private static final Path TX_FILE = Path.of("transactions.txt");
    private static final Path BUD_FILE = Path.of("budgets.txt");
    private static final Path BANK_FILE = Path.of("banks.txt");

    private Storage storage;


    @BeforeEach
    public void beforeEach() throws IOException {
        // remove files so we start clean
        Files.deleteIfExists(TX_FILE);
        Files.deleteIfExists(BUD_FILE);
        Files.deleteIfExists(BANK_FILE);

        storage = new Storage(); // constructor will attempt to load (empty)
    }

    @AfterEach
    public void afterEach() throws IOException {
        // cleanup
        Files.deleteIfExists(TX_FILE);
        Files.deleteIfExists(BUD_FILE);
        Files.deleteIfExists(BANK_FILE);
    }

    @Test
    public void addTransaction_validInput_updatesUserAndStorageFile() throws IOException, FinanceException {
        // create a transaction and add
        Transaction t = new Transaction(12.34f, Category.FOOD, new Date(10, Month.JAN, 2025), Currency.SGD);
        User.addTransaction(t);

        // in-memory list must contain it
        List<Transaction> txs = User.getTransactions();
        assertEquals(1, txs.size());
        assertEquals(12.34f, txs.get(0).getValue(), 0.001);

        // file must exist and contain a line
        assertTrue(Files.exists(TX_FILE));
        String content = Files.readString(TX_FILE).trim();
        assertFalse(content.isEmpty());
        // basic format check (category|value|day|MONTH|year|currency)
        assertTrue(content.startsWith("FOOD|") && content.contains("|10|JAN|2025|SGD"));

        // Now create a new Storage instance which should load from file
        Storage second = new Storage();

        assertEquals(1, User.getTransactions().size());
        Transaction loaded = User.getTransactions().get(0);
        assertEquals(12.34f, loaded.getValue(), 0.001);
        assertEquals(Category.FOOD, loaded.getCategory());
        assertEquals(10, loaded.getDate().getDay());
        assertEquals(Month.JAN, loaded.getDate().getMonth());
        assertEquals(Currency.SGD, loaded.getCurrency());
    }
}
