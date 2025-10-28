package commands;

import ui.FinanceException;
import user.User;

import java.util.ArrayList;

import static user.User.currBank;


public class DeleteTransactionCommand implements Command {
    private final ArrayList<String> arguments;

    public DeleteTransactionCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }
    @Override
    public String execute() throws FinanceException {

        try {
            if (arguments.size() != 1) {
                throw new FinanceException(" Sorry! Wrong format. Try delete <transaction_index>");
            }
            int index = Integer.parseInt(arguments.get(0));
            currBank.deleteTransactionFromBank(index-1);
            User.getStorage().saveTransactions(User.getBanks());
        } catch (NumberFormatException e) {
            throw new FinanceException("Invalid input. Please enter a numeric transaction index.");
        } catch (Exception e) {
            throw new FinanceException("Error deleting transaction: " + e.getMessage());
        }
        return null;
    }
}
