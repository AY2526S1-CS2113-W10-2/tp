package commands;

import summary.Summary;
import ui.FinanceException;
import user.User;
import utils.Currency;
import utils.Month;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SummaryCommand implements Command {
    private static final Logger logger = Logger.getLogger(Summary.class.getName());
    private final ArrayList<String> arguments;

    public SummaryCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        try {
            logger.log(Level.INFO, "Handling summary command with arguments: " + arguments);

            /*if (arguments.size() < 2) {
                logger.log(Level.WARNING, "Invalid command length: " + arguments.size());
                throw new FinanceException("Please provide a month! \n Usage: summary <month>" );
            }*/

            String monthInput = arguments.get(0);
            Summary summary = new Summary(User.getStorage());
            Month monthEnum = Month.valueOf(monthInput.toUpperCase());

            // Case 1: Three or more arguments
            if (arguments.size() >= 2) {
                if (User.isLoggedIn) {
                    throw new FinanceException("You are logged into a bank. Too many arguments for summary command.");
                }
                String currencyArg = arguments.get(1).toUpperCase();
                Currency currency = Currency.valueOf(currencyArg);
                summary.showMonthlySummary(monthInput, null, currency);
            }

            // Case 2: Less than 3 arguments
            else {
                if (!User.isLoggedIn) {
                    throw new FinanceException("No bank logged in. Please provide a currency. Usage: summary <month> <currency>");
                }
                summary.showMonthlySummary(monthInput, User.curr_bank, User.curr_bank.getCurrency());
            }


        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Invalid month name provided:" + e.getMessage());
            throw new FinanceException("Invalid month name. Please try again (e.g., summary JAN).");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in generating summary:" + e.getMessage());
            throw new FinanceException("Error generating summary: " + e.getMessage());
        }
        return null;
    }
}
