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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchCommandTest {
    private static final Path TX_FILE = Path.of("transactions.txt");

    @BeforeEach
    void setUp() throws FinanceException, IOException {
        Files.deleteIfExists(TX_FILE);
        User.initialise();

        User.banks = new ArrayList<>();
        User.budgets = new ArrayList<>();

        Bank testBank = new Bank(1, Currency.SGD, 1000f, 1.0f);
        User.addBank(testBank);
        User.currBank = testBank;

        User.currBank.addTransactionToBank(new Transaction(10.0f, Category.FOOD,
                new Date(5, Month.JAN, 2025), Currency.SGD));
        User.currBank.addTransactionToBank(new Transaction(25.0f, Category.ENTERTAINMENT,
                new Date(10, Month.JAN, 2025), Currency.SGD));
        User.currBank.addTransactionToBank(new Transaction(5.0f, Category.FOOD,
                new Date(12, Month.JAN, 2025), Currency.SGD));
    }

    @Test
    public void searchCommand_keywordMatches_returnsResults() throws FinanceException {
        ArrayList<String> args = new ArrayList<>();
        args.add("food");

        SearchCommand searchCommand = new SearchCommand(args);
        searchCommand.execute();

        ArrayList<Transaction> txs = User.currBank.getTransactions();

        boolean matchFound = txs.stream().anyMatch(
                t -> t.getCategory().name().toLowerCase().contains("food")
        );
        assertTrue(matchFound, "Expected at least one transaction to match the keyword 'food'");
    }

    @Test
    void searchCommand_twoArguments_throwsFinanceException() throws FinanceException {
        ArrayList<String> args = new ArrayList<>();
        args.add("foo");
        args.add("bar");

        SearchCommand searchCommand = new SearchCommand(args);

        FinanceException exception = assertThrows(FinanceException.class, searchCommand::execute);
        assertEquals("Too many arguments. Usage: search <keyword>", exception.getMessage());

    }
}
