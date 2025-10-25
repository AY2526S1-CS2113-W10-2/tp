package storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



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

}
