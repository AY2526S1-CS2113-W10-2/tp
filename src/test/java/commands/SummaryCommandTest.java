package commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;
import user.User;
import bank.Bank;
import utils.Currency;
import utils.Month;
import utils.Budget;
import utils.Category;
import ui.FinanceException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SummaryCommandTest {
    private Storage storage;

    @BeforeEach
    public void setup() {
        // Initialize User static fields
        User.banks = new ArrayList<>();
        User.transactions = new ArrayList<>();
        User.budgets = new ArrayList<>();
        User.isLoggedIn = false;
        User.currBank = null;

        storage = new Storage();

        // Add one bank and one budget for testing
        Bank bank = new Bank(0, Currency.SGD, 1000f, 1.0f);
        User.banks.add(bank);

        Budget budget = new Budget(Category.FOOD, 200f, Currency.SGD, Month.JAN, bank);
        User.budgets.add(budget);
    }

    @Test
    public void execute_loggedOutWithoutCurrency_shouldNotThrow() {
        ArrayList<String> args = new ArrayList<>();
        args.add("JAN"); // month only

        SummaryCommand cmd = new SummaryCommand(args);

        assertDoesNotThrow(() -> cmd.execute());
    }

    @Test
    public void execute_loggedOutWithCurrency_shouldNotThrow() {
        ArrayList<String> args = new ArrayList<>();
        args.add("JAN");
        args.add("SGD");

        SummaryCommand cmd = new SummaryCommand(args);

        assertDoesNotThrow(() -> cmd.execute());
    }

    @Test
    public void execute_loggedInWithBank_shouldNotThrow() {
        User.isLoggedIn = true;
        User.currBank = User.banks.get(0);

        ArrayList<String> args = new ArrayList<>();
        args.add("JAN");

        SummaryCommand cmd = new SummaryCommand(args);

        assertDoesNotThrow(() -> cmd.execute());
    }

    @Test
    public void execute_invalidMonth_shouldThrow() {
        ArrayList<String> args = new ArrayList<>();
        args.add("FOO"); // invalid month

        SummaryCommand cmd = new SummaryCommand(args);

        assertThrows(FinanceException.class, () -> cmd.execute());
    }

    @Test
    public void execute_invalidCurrency_shouldThrow() {
        ArrayList<String> args = new ArrayList<>();
        args.add("JAN");
        args.add("XXX"); // invalid currency

        SummaryCommand cmd = new SummaryCommand(args);

        assertThrows(FinanceException.class, () -> cmd.execute());
    }
}
