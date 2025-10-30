package commands;

import transaction.Transaction;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;

import static ui.OutputManager.listSearch;
import static ui.OutputManager.printMessage;

public class SearchCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 1;
    private final ArrayList<String> arguments;

    public SearchCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }


    @Override
    public String execute() throws FinanceException {
        if (arguments.isEmpty()) {
            throw new FinanceException("Please provide a keyword to search. Usage: search <keyword>");
        }
        if (arguments.size() > REQUIRED_ARGUMENTS_LENGTH) {
            throw new FinanceException("Too many arguments. Usage: search <keyword>");
        }

        String keyword = arguments.get(0).toLowerCase();
        ArrayList<Transaction> transactions = User.getCurrBank().getTransactions();

        String result = listSearch(keyword, transactions);
        printMessage(result);
        return null;
    }
}
