package commands;

import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import ui.FinanceException;
import ui.Parser;
import user.User;
import utils.Category;
import utils.Currency;

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
        User.banks = new ArrayList<>();
        User.budgets = new ArrayList<>();

        // Set up a test bank
        Bank testBank = new Bank(1, Currency.SGD, 1000f, 1.0f);
        User.addBank(testBank);
        User.currBank = testBank;
        User.isLoggedIn = true;
    }

    @Test
    public void execute_validInput_transactionAddedSuccessfully() throws FinanceException {
        Parser.parseCommand("add food 10.50 10/4/2024");


        assertEquals(1, User.currBank.getTransactions().size());
        Transaction t = User.currBank.getTransactions().get(0);
        assertEquals(Category.FOOD, t.getCategory());
        assertEquals(10.5f, t.getValue());
    }

    @Test
    public void execute_invalidInput_throwsException() {
        FinanceException exception = assertThrows(FinanceException.class, () ->
                Parser.parseCommand("add food 10/4/2024")
        );
        assertEquals("Sorry! Wrong format. Try 'add <tag(optional)> <category> <value> <date>' \n" +
                "e.g. 'add food 4.50 10/4/2024' or 'add 'groceries' food 4.50 10/4/2024'", exception.getMessage());
    }


    @Test
    public void execute_negativeValue_throwsException() {
        FinanceException exception = assertThrows(FinanceException.class, () ->
                Parser.parseCommand("add food -0.50 10/4/2024")
        );
        assertEquals("Value cannot be negative", exception.getMessage());
    }

    @Test
    public void execute_nullCategory_throwsException() {
        FinanceException exception = assertThrows(FinanceException.class, () ->
                Parser.parseCommand("add sports 10.50 10/4/2024")
        );
        assertEquals("Unknown category: sports", exception.getMessage());
    }

}
