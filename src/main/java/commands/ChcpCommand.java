package commands;

import ui.FinanceException;

import static ui.OutputManager.printMessage;

public class ChcpCommand implements Command{
    public String execute() throws FinanceException {
        printMessage("You must 'exit' the program before you can change the terminal encoding!");
        return null;
    }
}
