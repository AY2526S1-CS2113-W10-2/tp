package commands;

import logger.AppLogger;
import transaction.Transaction;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;
import java.util.logging.Logger;

import static ui.OutputManager.printMessage;

/**
 * Represents a command to delete a transaction from the current bank.
 * The user must provide the transaction index (1-based) to delete.
 */
public class DeleteTransactionCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 1;
    private static final Logger logger = AppLogger.getLogger();
    private final ArrayList<String> arguments;

    /**
     * Constructs a {@code DeleteTransactionCommand} with the given arguments.
     *
     * @param arguments The list of arguments provided by the user.
     */
    public DeleteTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
        logger.info("DeleteTransactionCommand created with arguments: " + arguments);
    }

    /**
     * Executes the delete command.
     * <p>
     * It validates the index argument, deletes the specified transaction, prints
     * confirmation and updated balance, and saves the updated transactions to storage.
     *
     * @return Always returns {@code null}.
     * @throws FinanceException If the input is invalid or deletion fails.
     */
    @Override
    public String execute() throws FinanceException {
        try {
            logger.info("Executing DeleteTransactionCommand...");

            if (arguments.size() != REQUIRED_ARGUMENTS_LENGTH) {
                throw new FinanceException(" Sorry! Wrong format. Try delete <transaction_index>");
            }
            int index = Integer.parseInt(arguments.get(0));
            Transaction deleted = User.getCurrBank().deleteTransactionFromBank(index - 1);

            String output = "Deleted Transaction: " + deleted + "\n" +
                    "Updated bank balance: "
                    + User.getCurrBank().getCurrency().getSymbol()
                    + String.format("%.2f", User.getCurrBank().getBalance());

            printMessage(output);
            logger.info("Transaction deleted successfully: " + deleted);

            User.getStorage().saveTransactions(User.getBanks());
            logger.info("Transactions saved to storage successfully.");

        } catch (NumberFormatException e) {
            throw new FinanceException("Invalid input. Please enter a numeric transaction index.");
        } catch (Exception e) {
            throw new FinanceException("Error deleting transaction: " + e.getMessage());
        }
        return null;
    }
}
