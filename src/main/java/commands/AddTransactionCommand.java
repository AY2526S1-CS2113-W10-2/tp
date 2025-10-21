package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddTransactionCommand implements Command {
    private final ArrayList<String> arguments;

    public AddTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() throws FinanceException {
        if (arguments.size() < 4) {
            throw new FinanceException("  Sorry! Wrong format. Try 'add <category> <value> <date> <currency>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024 JPY'");
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

            Currency currency = Currency.toCurrency(arguments.get(3));
            Transaction transaction = new Transaction(value, category, date, currency);

            User.addTransaction(transaction);
            printMessage("  Added Transaction: " + transaction.toString());
        } catch (FinanceException e) {
            throw e;
        } catch (Exception e) {
            throw new FinanceException("  Sorry! Wrong format. Try 'add <category> <value> <date> <currency>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024 JPY'");
        }
    }
}
