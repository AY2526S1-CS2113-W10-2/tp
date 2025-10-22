package commands;

import ui.FinanceException;
import user.User;
import utils.Budget;
import utils.Category;
import utils.Currency;
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
        if (arguments.size() < 4) {
            throw new FinanceException("  Sorry! Wrong format. " +
                    "Try 'addBudget <category> <amount> <currency> <month>'\n" +
                    "  e.g. 'addBudget food 200 SGD JAN'");
        }

        try{
            Category category = Category.toCategory(arguments.get(0));
            float amount = Float.parseFloat(arguments.get(1));
            Currency currency = Currency.toCurrency(arguments.get(2));
            Month month = Month.fromString(arguments.get(3));

            Budget budget = new Budget(category, amount, currency, month);
            User.addBudget(budget);

            printMessage("Added budget of " + amount + " " + currency + " for " + category + " in " + month);
        }catch (Exception e) {
            throw new FinanceException("Error adding budget: " + e.getMessage());
        }
        return null;
    }
}
