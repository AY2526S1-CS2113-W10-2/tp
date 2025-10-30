package commands;

import ui.FinanceException;
import ui.OutputManager;
import user.User;


public class ListBanksCommand implements Command{

    @Override
    public String execute() throws FinanceException {
        StringBuilder strb = new StringBuilder();
        strb.append("  Your Bank Accounts are:");
        for (int i = 0; i < User.getBanks().size(); i++){
            strb.append("\n  ").append(User.getBanks().get(i).toString());
        }
        OutputManager.printMessage(strb.toString());
        return "";
    }
}
