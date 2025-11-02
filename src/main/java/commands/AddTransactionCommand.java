package commands;

import bank.Bank;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddTransactionCommand implements Command {
    private static final String ERROR_INVALID_FORMAT =
            "Sorry! Wrong format. Try 'add <tag(optional)> <category> <value> <date>' \n"
        + "e.g. 'add food 4.50 10/4' or 'add 'groceries' food 4.50 10/4'";
    private static final int MIN_ALLOWED_ARGUMENTS_LENGTH = 3;
    private static final int MAX_ALLOWED_ARGUMENTS_LENGTH = 4;
    private static final float MIN_VALUE = 0f;
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

        if (arguments.size() == MAX_ALLOWED_ARGUMENTS_LENGTH) {
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
            float value = Float.parseFloat(valueString);

            Category category = Category.toCategory(categoryString);
            if (category == null) {
                throw new FinanceException("Invalid category");
            }

            if (value <= MIN_VALUE) {
                throw new FinanceException("Transaction value cannot be negative or zero");
            }

            //Checks if value input is within 2 decimal points
            if (!valueString.matches("\\d+(\\.\\d{1,2})?")) {
                throw new FinanceException("Amount must have at most 2 decimal places.");
            }

            Date date = Date.toDate(dateString);
            Bank currBank = User.getCurrBank();

            Currency currency = currBank.getCurrency();

            if (value > currBank.getBalance()) {
                throw new FinanceException("Insufficient funds. Your balance is "
                        + currency.getSymbol() + currBank.getBalance());
            }

            Transaction trans = new Transaction(value, category, date, currency, tag);
            currBank.addTransactionToBank(trans);
            currBank.setBalance(currBank.getBalance() - value);
            User.getStorage().saveTransactions(User.getBanks());
            User.getStorage().saveBanks(User.getBanks()); // save updated balance


            String output = "Added Transaction: " + trans + "\n" +
                    "Updated bank balance: " + currency.getSymbol() + String.format("%.2f", currBank.getBalance());
            printMessage(output);

        } catch (FinanceException e) {
            throw e;
        } catch (Exception e) {
            throw new FinanceException("Error adding transaction: " + e.getMessage() + "\n" + ERROR_INVALID_FORMAT);
        }
        return null;
    }

}
