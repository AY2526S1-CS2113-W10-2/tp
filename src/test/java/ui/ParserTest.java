package ui;

import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import user.User;
import utils.Currency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ParserTest {
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


}
