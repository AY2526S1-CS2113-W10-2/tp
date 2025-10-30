package commands;

import ui.FinanceException;
import user.User;
import utils.Budget;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.listBudget;
import static ui.OutputManager.printMessage;

public class ListBudgetsCommand implements Command {
    private final ArrayList<String> arguments;

    public ListBudgetsCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() != 1) {
            throw new FinanceException(" Sorry! Wrong format. Try 'listBudget <month>'\n" +
                    "e.g. 'listBudget JAN'");
        }

        if (User.currBank == null){
            throw new FinanceException("You must be logged into a bank account to see its budgets! Try login command!");
        }

        try {
            Month month = Month.fromString(arguments.get(0).toUpperCase());
            ArrayList<Budget> budgets = User.getBudgets();
            printMessage(listBudget(budgets, month));
        } catch (IllegalArgumentException e) {
            throw new FinanceException("Invalid month! Use e.g., JAN, FEB, etc.");
        }
        return null;
    }
}
