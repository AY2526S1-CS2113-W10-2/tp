package commands;

import ui.FinanceException;
import user.User;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class LoginCommand implements Command {
    private final ArrayList<String> arguments;

    public LoginCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }


    @Override
    public String execute() throws FinanceException {
        if (User.isLoggedIn) {
            printMessage("Already logged into a bank. Please logout first.");
            return null;
        }
        if (arguments.size() != 1) {
            throw new FinanceException("Please specify a bank ID. Usage: login <bank_id>");
        }

        int bankId = parseBankId(arguments.get(0));

        if (bankId >= User.banks.size() || User.banks.get(bankId).getId() != bankId) {
            throw new FinanceException("Bank not found. Try bank with index 0 - " + (User.banks.size() - 1));
        }

        User.currBank = User.banks.get(bankId);
        User.isLoggedIn = true;
        printMessage("Successfully logged into bank " + bankId);
        return null;
    }

    private int parseBankId(String s) throws FinanceException {
        try {
            int id = Integer.parseInt(s);
            if (id < 0) {
                throw new FinanceException("Bank ID cannot be negative.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new FinanceException("Invalid Bank ID. Please enter a number.");
        }
    }
}
