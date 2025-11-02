package commands;

import summary.Summary;
import ui.FinanceException;
import user.User;
import utils.Currency;
import utils.Month;
import logger.AppLogger;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the "summary" command which generates a financial summary
 * for a specified month and currency scope (bank, currency, or all).
 * <p>
 * This command intelligently determines the summary type based on the user’s
 * login state and provided arguments:
 * <ul>
 *   <li>If logged in → show summary for the logged-in bank only.</li>
 *   <li>If logged out with currency specified → show summary for that currency.</li>
 *   <li>If logged out without currency → show summary for all banks converted to SGD.</li>
 * </ul>
 * </p>
 *
 * Usage examples:
 * <pre>
 * summary JAN
 * summary FEB USD
 * </pre>
 */

//@@author kevinlokewy
public class SummaryCommand implements Command {
    private static final Logger logger = AppLogger.getLogger();
    private final ArrayList<String> arguments;

    public SummaryCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the "summary" command to display a financial summary.
     * <p>
     * The behavior depends on the user's login state:
     * <ul>
     *   <li>If logged in → generates summary for the current bank.</li>
     *   <li>If logged out and a currency is provided → generates summary for that currency.</li>
     *   <li>If logged out and no currency is provided → aggregates all banks (converted to SGD).</li>
     * </ul>
     * </p>
     *
     * @return always returns {@code null}, as summary output is handled by {@link summary.Summary}
     * @throws FinanceException if the arguments are invalid or if a summary cannot be generated
     */

    @Override
    //@@author kevinlokewy
    public String execute() throws FinanceException {
        try {
            logger.log(Level.INFO, "Handling summary command with arguments: " + arguments);

            if (arguments.isEmpty()) {
                throw new FinanceException("Please provide a month. Usage: summary <month> [currency]");
            }

            String monthInput = parseMonth();
            Summary summary = new Summary(User.getStorage());

            if (User.isLoggedIn() && User.getCurrBank() != null) {
                // Logged in → show only this bank
                showMonthlySummaryForBank(summary, monthInput);
            } else if (arguments.size() >= 2) {
                // Logged out WITH currency specified → show only that currency
                Currency currency = parseCurrency();
                showMonthlySummaryForCurrency(summary, monthInput, currency);
            } else {
                // Logged out WITHOUT currency → show ALL banks converted to SGD
                showMonthlySummaryForAllTransactions(summary, monthInput);
            }

        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Invalid month or currency:" + e.getMessage());
            throw new FinanceException("Invalid month or currency. Please check your input.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in generating summary:" + e.getMessage());
            throw new FinanceException("Error generating summary: " + e.getMessage());
        }

        return null;
    }
    /**
     * Displays a combined summary for all banks, converting all values to SGD.
     *
     * @param summary     the {@link Summary} instance handling output
     * @param monthInput  the month to generate summary for
     */

    //@@author kevinlokey
    private static void showMonthlySummaryForAllTransactions(Summary summary, String monthInput) {
        summary.showMonthlySummary(monthInput, null, Currency.SGD, true);
    }

    /**
     * Displays a summary for all banks with transactions in the specified currency.
     *
     * @param summary     the {@link Summary} instance handling output
     * @param monthInput  the target month
     * @param currency    the currency to display
     */
    //@@author kevinlokey
    private static void showMonthlySummaryForCurrency(Summary summary, String monthInput, Currency currency) {
        summary.showMonthlySummary(monthInput, null, currency, false);
    }

    /**
     * Displays a summary for the logged-in user's current bank only.
     *
     * @param summary     the {@link Summary} instance handling output
     * @param monthInput  the target month
     */
    //@@author kevinlokey
    private static void showMonthlySummaryForBank(Summary summary, String monthInput) {
        summary.showMonthlySummary(monthInput, User.getCurrBank(), User.getCurrBank().getCurrency(), false);
    }

    /**
     * Parses the currency argument from the command input.
     *
     * @return a valid {@link Currency} enum value
     * @throws FinanceException if the provided currency code is invalid
     */
    //@@author kevinlokey
    private Currency parseCurrency() throws FinanceException {
        Currency currency;
        try {
            currency = Currency.valueOf(arguments.get(1).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FinanceException("Invalid currency. " +
                    "Please provide a valid currency code: MYR, VND, JPY, IDR, SGD, THB.");
        }
        return currency;
    }

    /**
     * Parses and validates the month argument from the command input.
     *
     * @return a valid month string in uppercase (e.g., "JAN")
     * @throws FinanceException if the month name is invalid or unrecognized
     */

    //@@author kevinlokey
    private String parseMonth() throws FinanceException {
        String monthInput = arguments.get(0).toUpperCase();
        try {
            Month.valueOf(monthInput); // just to check validity
        } catch (IllegalArgumentException e) {
            throw new FinanceException("Invalid month name. Please try again (e.g., summary JAN).");
        }
        return monthInput;
    }

}
