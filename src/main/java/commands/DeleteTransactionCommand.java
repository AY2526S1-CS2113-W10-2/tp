package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;


public class DeleteTransactionCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 1;
    private final ArrayList<String> arguments;

    public DeleteTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }
    @Override
    public String execute() throws FinanceException {

        try {
            if (arguments.size() != REQUIRED_ARGUMENTS_LENGTH) {
                throw new FinanceException(" Sorry! Wrong format. Try delete <transaction_index>");
            }
            int index = Integer.parseInt(arguments.get(0));
            Transaction deleted = User.getCurrBank().deleteTransactionFromBank(index-1);
            printMessage("Deleted Transaction: " + deleted);
            User.getStorage().saveTransactions(User.getBanks());
        } catch (NumberFormatException e) {
            throw new FinanceException("Invalid input. Please enter a numeric transaction index.");
        } catch (Exception e) {
            throw new FinanceException("Error deleting transaction: " + e.getMessage());
        }
        return null;
    }
}
