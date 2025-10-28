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
    private static final int ALLOWED_ARGUMENTS_LENGTH = 3;
    private static final double MIN_VALUE = 0.0;
    private final ArrayList<String> arguments;

    public AddTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() < ALLOWED_ARGUMENTS_LENGTH) {
            throw new FinanceException("  Sorry! Wrong format. Try 'add <category> <value> <date>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024'");
        }

        try {
            Category category = Category.toCategory(arguments.get(0));
            if (category == null) {
                throw new FinanceException("Invalid category");
            }

            float value = Float.parseFloat(arguments.get(1));

            if (value < MIN_VALUE) {
                throw new FinanceException("Value cannot be negative");
            }

            Date date = Date.toDate(arguments.get(2));

            //Currency currency = Currency.toCurrency(arguments.get(3));
            Currency currency = currBank.getCurrency();
            if (currency == currBank.getCurrency()){
                Transaction trans = new Transaction(value, category, date, currency);
                currBank.addTransactionToBank(trans);
                currBank.setBalance(currBank.getBalance() - value);
                User.getStorage().saveTransactions(User.getBanks());
                User.getStorage().saveBanks(User.banks);              // save updated balance
                printMessage("Added Transaction: " + trans.toString());
            } else {
                throw new FinanceException("Currency must be in" + currBank.getCurrency().name());
            }

        } catch (FinanceException e) {
            throw e;
        } catch (Exception e) {
            throw new FinanceException(e.getMessage());
        }
        return null;
    }
}
