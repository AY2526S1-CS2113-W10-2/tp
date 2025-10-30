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
        /*if (!User.isLoggedIn) {
            throw new FinanceException("Please login to a bank to add a budget.");
        }*/

        if (arguments.size() < 3) {
            throw new FinanceException("  Sorry! Wrong format. " +
                    "Try 'addBudget <category> <amount> <month>'\n" +
                    "  e.g. 'addBudget food 200 JAN'");
        }

        // Ensure that commands are parsable as correct type and order
        try {
            Category.toCategory(arguments.get(0));
            Float.parseFloat(arguments.get(1));
            Month.fromString(arguments.get(2));
        } catch(Exception e){
            throw new FinanceException("  Sorry! Wrong format. " +
                    "Try 'addBudget <category> <amount> <month>'\n" +
                    "  e.g. 'addBudget food 200 JAN'");
        }

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

        // Use the currency of the currently logged-in bank. Handle error if not signed in to a particular bank.
        if (User.currBank == null){
            throw new FinanceException("You must be logged into a bank account to set a budget! Try login command!");
        }else{
            Currency currency = User.currBank.getCurrency();
            // Create and add budget
            Budget budget = new Budget(category, amount, User.currBank.getCurrency(), month, User.currBank);
            User.addBudget(budget);

            printMessage("Added budget of " + amount + " " + currency.getSymbol() +
                    " for " + category + " in " + month);
        }

        return null;
    }
}
