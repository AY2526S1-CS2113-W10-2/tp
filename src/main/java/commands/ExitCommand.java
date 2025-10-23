package commands;

import ui.FinanceException;

import static ui.OutputManager.printMessage;

public class ExitCommand implements Command {

    @Override
    public String execute() throws FinanceException {
        printMessage("Exiting program. Goodbye!");
        return null;
    }

    @Override
    public boolean shouldExit() {
        return true;
    }
}
