package commands;

import transaction.Transaction;
import ui.FinanceException;
import ui.OutputManager;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;
import static user.User.curr_bank;

public class AddTransactionCommand implements Command {
    private final ArrayList<String> arguments;

    public AddTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() < 3) {
            throw new FinanceException("  Sorry! Wrong format. Try 'add <category> <value> <date>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024'");
        }

        try {
            Category category = Category.toCategory(arguments.get(0));
            if (category == null) {
                throw new FinanceException("Invalid category");
            }

            float value = Float.parseFloat(arguments.get(1));
            if (value < 0) {
                throw new FinanceException("Value cannot be negative");
            }

            String[] dateParts = arguments.get(2).split("/");
            if (dateParts.length != 3) {
                throw new FinanceException("Date format must be DD/MM/YYYY");
            }

            int day = Integer.parseInt(dateParts[0]);
            int monthNum = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Convert numeric month to enum (1 = JAN, 2 = FEB, ...)
            Month month = Month.values()[monthNum - 1];

            Date date = new Date(day, month, year);

            //Currency currency = Currency.toCurrency(arguments.get(3));
            Currency currency = curr_bank.getCurrency();
            if (currency == curr_bank.getCurrency()){
                Transaction trans = new Transaction(value, category, date, currency);
                //User.addTransaction(trans);
                curr_bank.addTransactionToBank(trans);
                curr_bank.setBalance(curr_bank.getBalance() - value);
                User.getStorage().saveTransactions(User.getBanks());
                User.getStorage().saveBanks(User.banks);              // save updated balance
                printMessage("Added Transaction: " + trans.toString());
            }
            else{
                throw new FinanceException("Currency must be in" + curr_bank.getCurrency().name());
            }

        } catch (FinanceException e) {
            throw e;
        } catch (Exception e) {
            throw new FinanceException("  Sorry! Wrong format. Try 'add <category> <value> <date>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024'");
        }
        return null;
    }
}
