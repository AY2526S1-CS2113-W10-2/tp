package ui;

import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import user.User;
import utils.Category;
import utils.Currency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    private static final Path TX_FILE = Path.of("transactions.txt");

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(TX_FILE);
        User.initialise();

        User.banks = new ArrayList<>();
        User.budgets = new ArrayList<>();

        // Set up a test bank
        Bank bank0 = new Bank(0, Currency.SGD, 1000f, 1.0f);
        Bank bank1 = new Bank(1, Currency.SGD, 2000f, 1.0f);
        User.addBank(bank0);
        User.addBank(bank1);
        User.currBank = bank0;
        User.isLoggedIn = true;
    }

    @Test
    public void parseCommand_login_success() throws FinanceException {
        User.isLoggedIn = false;
        boolean result = Parser.parseCommand("login 1");
        assertFalse(result);
        assertTrue(User.isLoggedIn);
        assertEquals(1, User.currBank.getId());
    }

    @Test
    public void parseCommand_logout_success() throws FinanceException {
        boolean result = Parser.parseCommand("logout");
        assertFalse(result);
        assertFalse(User.isLoggedIn);
        assertNull(User.currBank);
    }

    @Test
    public void parseCommand_addValidInput_transactionAdded() throws FinanceException {
        assertDoesNotThrow(() -> Parser.parseCommand("add food 10.50 10/4/2024"));
        assertEquals(1, User.currBank.getTransactions().size());
        Transaction t = User.currBank.getTransactions().get(0);
        assertEquals(Category.FOOD, t.getCategory());
        assertEquals(10.5f, t.getValue());
    }

    @Test
    public void parseCommand_addWhenNotLoggedIn_throwsFinanceException() {
        User.isLoggedIn = false;

        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("add food 10 01/01/2025"));
        assertEquals("Please login to a bank to execute this command", e.getMessage());
    }


    @Test
    public void parseCommand_invalidCommand_throwsFinanceException() {
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("foobar"));
        assertEquals("Does not match any known command.", e.getMessage());
    }

    @Test
    public void parseCommand_validExitCommand_exitProgram() throws FinanceException {
        boolean result = Parser.parseCommand("exit");
        assertTrue(result, "Parser should return true for exit command");
    }

    @Test
    public void parseCommand_listCommand_returnsTransactions() throws FinanceException {
        Parser.parseCommand("add food 5.0 01/01/2025");

        assertDoesNotThrow(() -> Parser.parseCommand("list"),
                "List command should not throw exception");
    }

    @Test
    public void parseCommand_listBanksWhenNotLoggedIn_returnsBanks() {
        User.isLoggedIn = false;

        assertDoesNotThrow(() -> Parser.parseCommand("listbanks"),
                "ListBanks command should execute without exception when not logged in");
    }

    @Test
    public void parseCommand_listBanksWhenLoggedIn_throwsFinanceException() {
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("listbanks"));
        assertEquals("Please logout to execute this command", e.getMessage());
    }


    @Test
    public void parseCommand_deleteValidIndexWhenLoggedIn_success() throws FinanceException {
        Parser.parseCommand("add food 10.0 01/01/2025");
        assertEquals(1, User.currBank.getTransactions().size());

        Parser.parseCommand("delete 1");
        assertEquals(0, User.currBank.getTransactions().size(), "Transaction should be deleted");
    }

    @Test
    public void parseCommand_deleteInvalidIndexWhenLoggedIn_throwsFinanceException() throws FinanceException {
        Parser.parseCommand("add food 10.0 01/01/2025");
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("delete 5"));
        assertTrue(e.getMessage().contains("Error deleting transaction"));
    }

    @Test
    public void parseCommand_deleteWhenNotLoggedIn_throwsFinanceException() throws FinanceException {
        Parser.parseCommand("add food 10.0 01/01/2025");
        assertEquals(1, User.currBank.getTransactions().size());

        User.isLoggedIn = false;
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("delete 4"));
        assertEquals("Please login to a bank to execute this command", e.getMessage());
    }

    @Test
    public void parseCommand_addBudgetValidInput_success() {
        assertDoesNotThrow(() -> Parser.parseCommand("addbudget food 500 FEB"),
                "addbudget command should not throw exception");
    }

    @Test
    public void parseCommand_listBudget_success() {
        assertDoesNotThrow(() -> Parser.parseCommand("listbudget FEB"),
                "listbudget command should not throw exception");
    }

    @Test
    public void parseCommand_depositWhenValidInput_success() throws FinanceException {
        float prevBalance = User.currBank.getBalance();
        Parser.parseCommand("deposit 200");
        assertEquals(prevBalance + 200f, User.currBank.getBalance(), 0.001);
    }

    @Test
    public void parseCommand_depositWhenNotLoggedIn_throwsFinanceException() throws FinanceException {
        User.isLoggedIn = false;
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("deposit 200"));
        assertEquals("Please login to a bank to execute this command", e.getMessage());
    }

    @Test
    public void parseCommand_withdrawWhenValidInput_success() throws FinanceException {
        float prevBalance = User.currBank.getBalance();
        Parser.parseCommand("withdraw 300");
        assertEquals(prevBalance - 300f, User.currBank.getBalance(), 0.001);
    }

    @Test
    public void parseCommand_withdrawWhenNotLoggedIn_throwsFinanceException() throws FinanceException {
        User.isLoggedIn = false;
        FinanceException e = assertThrows(FinanceException.class,
                () -> Parser.parseCommand("withdraw 200"));
        assertEquals("Please login to a bank to execute this command", e.getMessage());
    }

}
