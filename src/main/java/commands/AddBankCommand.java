package commands;

import bank.Bank;
import ui.FinanceException;
import user.User;
import utils.Currency;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddBankCommand implements Command {
    private final ArrayList<String> arguments;

    public AddBankCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() < 2) {
            throw new FinanceException(
                    "  Sorry! Wrong format. Try 'addBank <balance> <currency>' \n" +
                            "  e.g. 'addBank 1000.00 JPY'"
            );
        }

        // Parse balance
        float balance;
        try {
            balance = Float.parseFloat(arguments.get(0));
            if (balance < 0) {
                throw new FinanceException("Balance cannot be negative. You entered: " + arguments.get(0));
            }
        } catch (NumberFormatException e) {
            throw new FinanceException("Balance must be a valid number. You entered: " + arguments.get(0));
        }

        // Parse currency
        Currency currency = Currency.toCurrency(arguments.get(1));
        if (currency == null) {
            throw new FinanceException("Invalid currency. You entered: " + arguments.get(1));
        }

        // Set exchange rate automatically
        float exchangeRate = Currency.getExchangeRateToSGD(currency);

        // Create and save bank
        Bank bank = new Bank(User.getBanks().size(), currency, balance, exchangeRate);
        User.addBank(bank);
        printMessage("  Added " + bank + " | Exchange rate to SGD: " + exchangeRate);

        return null;
    }


}
