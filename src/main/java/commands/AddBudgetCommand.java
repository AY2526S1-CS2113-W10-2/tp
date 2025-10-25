package commands;

import ui.FinanceException;
import user.User;
import utils.Budget;
import utils.Category;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddBudgetCommand implements Command {
    private final ArrayList<String> arguments;

    public AddBudgetCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (!User.isLoggedIn) {
            throw new FinanceException("Please login to a bank to add a budget.");
        }

        if (arguments.size() < 3) {
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

            // Parse amount
            float amount = Float.parseFloat(arguments.get(1));
            if (amount < 0) {
                throw new FinanceException("Amount cannot be negative: " + arguments.get(1));
            }

            // Parse month
            Month month = Month.fromString(arguments.get(2));
            if (month == null) {
                throw new FinanceException("Invalid month: " + arguments.get(2));
            }

            // Use the currency of the currently logged-in bank
            var currency = User.currBank.getCurrency();

            // Create and add budget
            Budget budget = new Budget(category, amount, User.currBank.getCurrency(), month, User.currBank);
            User.addBudget(budget);

            printMessage("Added budget of " + amount + " " + currency.getSymbol() +
                    " for " + category + " in " + month);

        } catch (NumberFormatException e) {
            throw new FinanceException("Amount must be a valid number. You entered: " + arguments.get(1));
        }

        return null;
    }
}
