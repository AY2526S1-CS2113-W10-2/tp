package commands;

import ui.FinanceException;
import user.User;

import static ui.OutputManager.printMessage;

public class LogoutCommand implements Command {

    @Override
    public String execute() throws FinanceException {
        if (!User.isLoggedIn) {
            printMessage("You are not logged into any bank.");
            return null;
        }
        User.currBank = null;
        User.isLoggedIn = false;
        printMessage("Logged out successfully.");
        return null;
    }
}
