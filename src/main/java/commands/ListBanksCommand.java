package commands;

import ui.FinanceException;
import ui.OutputManager;

import static user.User.banks;

//@@author Mack34021
public class ListBanksCommand implements Command{

    @Override
    public String execute() throws FinanceException {
        StringBuilder strb = new StringBuilder();
        strb.append("  Your Bank Accounts are:");
        for (int i = 0; i < banks.size(); i++){
            strb.append("\n  ").append(banks.get(i).toString());
        }
        OutputManager.printMessage(strb.toString());
        return "";
    }
}
