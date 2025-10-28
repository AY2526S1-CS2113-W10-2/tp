package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;
import static user.User.currBank;

public class AddTransactionCommand implements Command {
    private static final String ERROR_INVALID_FORMAT =
            "Sorry! Wrong format. Try 'add <tag(optional)> <category> <value> <date>' \n"
        + "e.g. 'add food 4.50 10/4/2024' or 'add 'groceries' food 4.50 10/4/2024'";
    private static final int MIN_ALLOWED_ARGUMENTS_LENGTH = 3;
    private static final int MAX_ALLOWED_ARGUMENTS_LENGTH = 4;
    private static final double MIN_VALUE = 0.0;
    private final ArrayList<String> arguments;

    public AddTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        // Accept either 3 or 4 arguments
        if (arguments.size() < MIN_ALLOWED_ARGUMENTS_LENGTH || arguments.size() > MAX_ALLOWED_ARGUMENTS_LENGTH) {
            throw new FinanceException(ERROR_INVALID_FORMAT);
        }

        String tag;
        String categoryString;
        String valueString;
        String dateString;

        if (arguments.size() == 4) {
            // Tag provided
            tag = arguments.get(0);
            categoryString = arguments.get(1);
            valueString = arguments.get(2);
            dateString = arguments.get(3);
        } else {
            // Tag omitted
            tag = "unnamed";
            categoryString = arguments.get(0);
            valueString = arguments.get(1);
            dateString = arguments.get(2);
        }

        try {
            Category category = Category.toCategory(categoryString);
            if (category == null) {
                throw new FinanceException("Invalid category");
            }

            float value = Float.parseFloat(valueString);
            if (value < MIN_VALUE) {
                throw new FinanceException("Value cannot be negative");
            }

            Date date = Date.toDate(dateString);

            Currency currency = currBank.getCurrency();

            Transaction trans = new Transaction(value, category, date, currency, tag);
            currBank.addTransactionToBank(trans);
            currBank.setBalance(currBank.getBalance() - value);
            User.getStorage().saveTransactions(User.getBanks());
            User.getStorage().saveBanks(User.banks); // save updated balance
            printMessage("Added Transaction: " + trans.toString());

        } catch (FinanceException e) {
            throw e;
        } catch (Exception e) {
            throw new FinanceException(e.getMessage());
        }
        return null;
    }

}
