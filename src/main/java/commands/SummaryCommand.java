package commands;

import summary.Summary;
import ui.FinanceException;
import user.User;
import utils.Currency;
import utils.Month;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//@@author kevinlokewy
public class SummaryCommand implements Command {
    private static final Logger logger = Logger.getLogger(Summary.class.getName());
    private final ArrayList<String> arguments;

    public SummaryCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

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

            if (User.isLoggedIn && User.currBank != null) {
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
//@@author kevinlokey
    private static void showMonthlySummaryForAllTransactions(Summary summary, String monthInput) {
        summary.showMonthlySummary(monthInput, null, Currency.SGD, true);
    }
//@@author kevinlokey
    private static void showMonthlySummaryForCurrency(Summary summary, String monthInput, Currency currency) {
        summary.showMonthlySummary(monthInput, null, currency, false);
    }

    //@@author kevinlokey
    private static void showMonthlySummaryForBank(Summary summary, String monthInput) {
        summary.showMonthlySummary(monthInput, User.currBank, User.currBank.getCurrency(), false);
    }

    //@@author kevinlokey
    private Currency parseCurrency() throws FinanceException {
        Currency currency;
        try {
            currency = Currency.valueOf(arguments.get(1).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FinanceException("Invalid currency. Please provide a valid currency code.");
        }
        return currency;
    }

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
