package commands;

import bank.Bank;
import transaction.Transaction;
import ui.FinanceException;
import ui.OutputManager;
import user.User;

import java.util.ArrayList;
import java.util.List;

import static utils.Date.getDaySuffix;

public class ListRecentTransactionsCommand implements Command{

    @Override
    public String execute() throws FinanceException {
        StringBuilder strb = new StringBuilder();
        strb.append("  Recent Transactions:");

        List<Transaction> transactionsToDisplay = new ArrayList<>();

        // Step 1: Check if user is logged in to a bank
        Bank loggedInBank = User.currBank;

        if (loggedInBank != null) {
            // User logged in → show transactions from this bank only
            transactionsToDisplay.addAll(loggedInBank.getTransactions());
        } else {
            // No bank logged in → aggregate all transactions from all banks
            List<Bank> userBanks = User.getBanks();
            for (Bank b : userBanks) {
                transactionsToDisplay.addAll(b.getTransactions());
            }
        }

        // Step 2: Handle empty transaction case
        if (transactionsToDisplay.isEmpty()) {
            OutputManager.printMessage("There are no transactions");
            return "";
        }

        // Step 3: Display up to 10 most recent transactions
        int size = transactionsToDisplay.size();
        int start = Math.max(0, size - 10);

        for (int i = start; i < size; i++) {
            Transaction t = transactionsToDisplay.get(i);
            strb.append("\n  [").append(i + 1).append("] ") // 1-based index
                    .append(t.getCurrency().name()).append(t.getCurrency().getSymbol())
                    .append(t.getValue()).append(" spent on ")
                    .append(t.getTag())
                    .append("(")
                    .append(t.getCategory().name())
                    .append(")")
                    .append(" on ")
                    .append(t.getDate().getDay())
                    .append(getDaySuffix(t.getDate().getDay())).append(" ")
                    .append(t.getDate().getMonth().name()).append(", ")
                    .append(t.getDate().getYear());
        }

        OutputManager.printMessage(strb.toString());
        return "";
    }

}
