package commands;

import logger.AppLogger;
import transaction.Transaction;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ui.OutputManager.listSearch;
import static ui.OutputManager.printMessage;

/**
 * Represents a command that searches for transactions containing a specific keyword.
 * The search is case-insensitive and matches the keyword against transaction details.
 */
public class SearchCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 1;
    private final ArrayList<String> arguments;
    private static final Logger logger = AppLogger.getLogger();

    /**
     * Constructs a {@code SearchCommand} with the given arguments.
     *
     * @param arguments The list of arguments provided by the user.
     */
    public SearchCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
        logger.info("SearchCommand created with arguments: " + arguments);
    }


    /**
     * Executes the search command.
     * <p>
     * It checks that exactly one keyword argument is provided, retrieves all transactions
     * from the current bank, searches for transactions containing the keyword, and
     * prints the filtered list using {@link ui.OutputManager#listSearch(String, ArrayList)}.
     *
     * @return Always returns {@code null}.
     * @throws FinanceException If no keyword is provided or if too many arguments are given.
     */
    @Override
    public String execute() throws FinanceException {
        logger.info("Executing SearchCommand...");
        if (arguments.isEmpty()) {
            throw new FinanceException("Please provide a keyword to search. Usage: search <keyword>");
        }
        if (arguments.size() > REQUIRED_ARGUMENTS_LENGTH) {
            throw new FinanceException("Too many arguments. Usage: search <keyword>");
        }

        String keyword = arguments.get(0).toLowerCase();
        logger.info("Searching transactions for keyword: \"" + keyword + "\"");
        ArrayList<Transaction> transactions = User.getCurrBank().getTransactions();

        try {
            String result = listSearch(keyword, transactions);
            printMessage(result);
            logger.info("Search completed. Results displayed.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during search execution.", e);
            throw new FinanceException("Error in searching transactions: " + e.getMessage());
        }
        return null;
    }
}
