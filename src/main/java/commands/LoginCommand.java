package commands;

import bank.Bank;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class LoginCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 1;
    private final ArrayList<String> arguments;

    public LoginCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }


    @Override
    public String execute() throws FinanceException {
        if (User.isLoggedIn()) {
            printMessage("Already logged into a bank. Please logout first.");
            return null;
        }
        if (arguments.size() != REQUIRED_ARGUMENTS_LENGTH) {
            throw new FinanceException("Please specify a bank ID. Usage: login <bank_id>");
        }

        int bankId = parseBankId(arguments.get(0));
        ArrayList<Bank> banks = User.getBanks();

        if (banks.isEmpty()) {
            throw new FinanceException("There are no banks in the system. Please add a bank first.");
        }

        Bank currBank = banks.stream()
                .filter(b -> b.getId() == bankId)
                .findFirst()
                .orElseThrow(() -> new FinanceException(
                        "Bank not found. Try a valid bank ID."
                ));

        User.setCurrBank(currBank);
        User.setIsLoggedIn(true);

        String message = String.format(
                "Successfully logged into bank %d | Current balance: %s%.2f",
                currBank.getId(),
                currBank.getCurrency().getSymbol(),
                currBank.getBalance()
        );
        printMessage(message);

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
