package commands;

import summary.Summary;
import ui.FinanceException;
import user.User;

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
    public void execute() throws FinanceException {
        try {
            logger.log(Level.INFO, "Handling summary command with arguments: " + arguments);

            if (arguments.isEmpty()) {
                logger.log(Level.WARNING, "Invalid command length: " + arguments.size());
                throw new FinanceException("Please provide a month! \n Usage: summary <month>" );
            }

            String monthInput = arguments.get(0);
            Summary summary = new Summary(User.getStorage());
            summary.showMonthlySummary(monthInput);

        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Invalid month name provided:" + e.getMessage());
            throw new FinanceException("Invalid month name. Please try again (e.g., summary JAN).");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in generating summary:" + e.getMessage());
            throw new FinanceException("Error generating summary: " + e.getMessage());
        }
    }
}
