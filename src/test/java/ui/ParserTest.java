package ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import user.User;
import savedata.Storage;
import transaction.Transaction;
import utils.Category;
import utils.Month;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    private static final Path TX_FILE = Path.of("transactions.txt");

    @BeforeEach
    public void setup() throws IOException {
        // Delete persisted transaction file to start fresh
        Files.deleteIfExists(TX_FILE);

        // Ensure User storage is initialized
        Storage storage = User.getStorage();

        // Initialize static transactions and banks if null
        if (User.getTransactions() == null) {
            User.initialise();
        }

        // Clear any loaded transactions in User storage
        User.getTransactions().clear();
        //User.getTransactions().clear();
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(TX_FILE);
    }

    @Test
    public void parseCommand_validAddCommand_uodatesUserAndStorageFile() throws IOException {
        String cmd = "add food 4.50 10/01/2025 SGD";
        try {
            Parser.parseCommand(cmd); // assumes Parser.parseCommand exists
        } catch (FinanceException e) {
            e.printStackTrace();
            fail("FinanceException thrown");
        }

        // User should now have an extra transaction
        List<Transaction> txs = User.getTransactions();
        assertFalse(txs.isEmpty(), "Transactions should not be empty after adding");

        Transaction t = txs.get(txs.size() - 1); // last added
        assertEquals(4.5f, t.getValue(), 0.001);
        assertEquals(Category.FOOD, t.getCategory());
        assertEquals(Month.JAN, t.getDate().getMonth());

        // persisted file must exist and contain the category and value
        assertTrue(Files.exists(TX_FILE), "Transaction file should exist");
        String content = Files.readString(TX_FILE);
        assertTrue(content.contains("FOOD|") && content.contains("|4.5|"), "File should contain transaction data");
    }
}
