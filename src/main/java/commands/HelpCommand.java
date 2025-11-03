package commands;

import logger.AppLogger;
import ui.FinanceException;

import java.util.logging.Logger;

import static ui.OutputManager.printMessage;

public class HelpCommand implements  Command {
    private static final Logger logger = AppLogger.getLogger();
    @Override
    public String execute() throws FinanceException {
        logger.info("Executing HelpCommand...");
        String helpMessage = """
                ================== HELP MENU ==================
                Available commands:
                login <bank_index> 
                   → Log in to an existing bank account.
                logout 
                   → Log out from the current bank session.
                addbank <amount> <currency>
                   → Add a new bank account with a specific currency and initial balance.
                listBanks 
                   → View all your registered bank accounts.
                add <tag(optional)> <category> <amount> <date(DD/MM)>
                   → Add a new transaction to the current bank.
                list 
                   → List recent transactions of the current bank.
                delete <transaction_index>
                   → Delete a transaction by its index in the list.
                addBudget <category> <amount> <month>
                   → Add a budget for a specific category.
                listBudget <month>
                   → View all existing budgets in the month.
                filter cost <MIN> <MAX>
                    → Filter transactions within a cost range.
                filter category <CATEGORY>
                    → Filter transactions by category.
                filter date <START_DATE(DD/MM)> <END_DATE(DD/MM)>
                    → Filter transactions within a date range.
                search <keyword>
                    → Search for transactions containing a keyword in the description.
                summary <month>
                    → Show summary of spending and remaining budgets.
                deposit <amount>
                    → Deposit money into your current bank account.
                withdraw <amount>
                    → Withdraw money from your current bank account.
                exit
                    → Exit the application.
                =================================================
                """;
        printMessage(helpMessage);
        logger.info("HelpCommand executed successfully.");
        return null;
    }
}
