package commands;

import ui.FinanceException;

import static ui.OutputManager.printMessage;

public class ExitCommand implements Command {

    @Override
    public void execute() throws FinanceException {
        printMessage("Exiting program. Goodbye!");
    }

    @Override
    public boolean shouldExit() {
        return true;
    }
}
