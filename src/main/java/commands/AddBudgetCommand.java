package commands;

import bank.Bank;
import ui.FinanceException;
import user.User;
import utils.Budget;
import utils.Category;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddBudgetCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 3;
    private static final float MIN_VALUE = 0f;
    private final ArrayList<String> arguments;

    public AddBudgetCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() < REQUIRED_ARGUMENTS_LENGTH) {
            throw new FinanceException("  Sorry! Wrong format. " +
                    "Try 'addBudget <category> <amount> <month>'\n" +
                    "  e.g. 'addBudget food 200 JAN'");
        }

        try {
            // Determine category
            Category category = Category.toCategory(arguments.get(0));
            if (category == null) {
                throw new FinanceException("Invalid category: " + arguments.get(0));
            }

            // Parse month
            Month month = Month.fromString(arguments.get(2));
            if (month == null) {
                throw new FinanceException("Invalid month: " + arguments.get(2));
            }

            String amountString = arguments.get(1);
            if (!amountString.matches("\\d+(\\.\\d{1,2})?")) {
                throw new FinanceException("Amount must have at most 2 decimal places.");
            }

            float amount = Float.parseFloat(amountString);

            if (amount < MIN_VALUE) {
                throw new FinanceException("Amount cannot be negative: " + amountString);
            }

            Bank currBank = User.getCurrBank();
            var currency = currBank.getCurrency();

            // Create and add budget
            Budget budget = new Budget(category, amount, currBank.getCurrency(), month, currBank);
            User.addBudget(budget);

            printMessage("Added budget of " + amount + " " + currency.getSymbol() +
                    " for " + category + " in " + month);

        } catch (NumberFormatException e) {
            throw new FinanceException("Amount must be a valid number. You entered: " + arguments.get(1));
        }

        return null;
    }
}
