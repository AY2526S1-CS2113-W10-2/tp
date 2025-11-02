package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Date;

import java.util.ArrayList;

import static ui.OutputManager.listFilter;
import static ui.OutputManager.printMessage;

/**
 * Represents a command that filters transactions based on a specified filter type.
 * Supported filter types are:
 * <ul>
 *     <li><b>category</b> – Filters transactions by their category (e.g. FOOD, TRANSPORT).</li>
 *     <li><b>cost</b> – Filters transactions within a specified cost range.</li>
 *     <li><b>date</b> – Filters transactions within a date range.</li>
 * </ul>
 */
public class FilterCommand implements Command {
    private static final int MIN_ARGUMENTS_LENGTH = 2;
    private final ArrayList<String> arguments;

    /**
     * Constructs a {@code FilterCommand} with the specified arguments.
     *
     * @param arguments The list of arguments provided by the user.
     */
    public FilterCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the filter command based on the user's input.
     * It retrieves all transactions from the current bank and applies
     * filtering according to the specified filter type:
     * <ul>
     *     <li><b>category</b> – Matches transactions by {@link utils.Category}.</li>
     *     <li><b>cost</b> – Matches transactions with values between the given minimum and maximum amounts.</li>
     *     <li><b>date</b> – Matches transactions that occur within the given start and end dates.</li>
     * </ul>
     * The filtered transactions are then displayed using {@link ui.OutputManager#listFilter(String, ArrayList)}.
     *
     * @return Always returns {@code null}.
     * @throws FinanceException If the arguments are missing, invalid,
     *                          or if parsing the cost/date/category fails.
     */
    @Override
    public String execute() throws FinanceException {
        if (arguments.isEmpty()) {
            throw new FinanceException("Sorry! Wrong format. Please specify a filter type: category, cost, or date.");
        }

        String filterType = arguments.get(0).toLowerCase();
        ArrayList<Transaction> allTrans = User.getCurrBank().getTransactions();
        ArrayList<Transaction> filteredTrans = new ArrayList<>();

        switch (filterType) {
        case "category":
            if (arguments.size() < MIN_ARGUMENTS_LENGTH) {
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

            String minStr = arguments.get(1);
            String maxStr = arguments.get(2);

            try {
                float min = Float.parseFloat(minStr);
                float max = Float.parseFloat(maxStr);

                // Check if both numbers have at most 2 decimal places
                if (!minStr.matches("\\d+(\\.\\d{1,2})?") || !maxStr.matches("\\d+(\\.\\d{1,2})?")) {
                    throw new FinanceException("MIN and MAX must be numbers with at most 2 decimal places.");
                }

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
            }  catch (NumberFormatException e) {
                throw new FinanceException("Invalid min/max values.");
            }
            break;

        case "date":
            if (arguments.size() < 3) {
                throw new FinanceException("Usage: filter date <start(DD/MM)> <end(DD/MM)>");
            }
            try {
                Date start = Date.toDate(arguments.get(1));
                Date end = Date.toDate(arguments.get(2));

                if (end.isBefore(start)) {
                    throw new FinanceException("Start date cannot be after end date.");
                }

                for (Transaction t : allTrans) {
                    if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                        filteredTrans.add(t);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new FinanceException("Error filtering by date: " + e.getMessage());
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
