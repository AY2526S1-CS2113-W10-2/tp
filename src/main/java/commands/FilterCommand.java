package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Date;

import java.util.ArrayList;

import static ui.OutputManager.listFilter;
import static ui.OutputManager.printMessage;

public class FilterCommand implements Command {
    private final ArrayList<String> arguments;

    public FilterCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.isEmpty()) {
            throw new FinanceException("Sorry! Wrong format. Please specify a filter type: category, cost, or date.");
        }

        String filterType = arguments.get(0).toLowerCase();
        ArrayList<Transaction> allTrans = User.currBank.getTransactions();
        ArrayList<Transaction> filteredTrans = new ArrayList<>();

        switch (filterType) {
        case "category":
            if (arguments.size() < 2) {
                throw new FinanceException("Usage: filter category <CATEGORY>");
            }
            try {
                Category category = Category.valueOf(arguments.get(1).toUpperCase());
                for (Transaction t : allTrans) {
                    if (t.getCategory() == category) {
                        filteredTrans.add(t);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new FinanceException("Invalid category. Please use a valid one (e.g. FOOD, TRANSPORT).");
            }
            break;

        case "cost":
            if (arguments.size() < 3) {
                throw new FinanceException("Usage: filter cost <MIN> <MAX>");
            }

            float min = Float.parseFloat(arguments.get(1));
            float max = Float.parseFloat(arguments.get(2));

            if (min < 0 || max < 0) {
                throw new FinanceException("MIN and MAX values cannot be negative.");
            }

            if (max < min) {
                throw new FinanceException("MAX value cannot be less than MIN value.");
            }

            for (Transaction t : allTrans) {
                if (t.getValue() >= min && t.getValue() <= max) {
                    filteredTrans.add(t);
                }
            }
            break;

        case "date":
            if (arguments.size() < 3) {
                throw new FinanceException("Usage: filter date <start(DD/MM/YYYY)> <end(DD/MM/YYYY)>");
            }
            try {
                Date start = Date.toDate(arguments.get(1));
                Date end = Date.toDate(arguments.get(2));

                for (Transaction t : allTrans) {
                    if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                        filteredTrans.add(t);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new FinanceException(e.getMessage());
            }
            break;

        default:
            throw new FinanceException("Unknown filter type. Valid options: category, cost, date.");
        }
        String result = listFilter(filterType, filteredTrans);
        printMessage(result);

        return null;
    }
}
