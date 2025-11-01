package commands;

import bank.Bank;
import ui.FinanceException;
import user.User;
import utils.Currency;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

public class AddBankCommand implements Command {
    private static final int REQUIRED_ARGUMENTS_LENGTH = 2;
    private static final float MIN_VALUE = 0f;
    private final ArrayList<String> arguments;

    public AddBankCommand(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String execute() throws FinanceException {
        if (arguments.size() < REQUIRED_ARGUMENTS_LENGTH) {
            throw new FinanceException(
                    "  Sorry! Wrong format. Try 'addBank <balance> <currency>' \n" +
                            "  e.g. 'addBank 1000.00 JPY'"
            );
        }

        // Parse balance
        float balance;
        try {
            balance = Float.parseFloat(arguments.get(0));
            if (balance < MIN_VALUE) {
                throw new FinanceException("Balance cannot be negative. You entered: " + arguments.get(0));
            }
        } catch (NumberFormatException e) {
            throw new FinanceException("Balance must be a valid number. You entered: " + arguments.get(0));
        }

        // Parse currency
        Currency currency = Currency.toCurrency(arguments.get(1));
        if (currency == null) {
            throw new FinanceException("Invalid currency. Only the following currencies work: MYR, VND, JPY, IDR, SGD, THB  You entered: " + arguments.get(1));
        }



        // Set exchange rate automatically
        float exchangeRate = Currency.getExchangeRateToSGD(currency);

        // Create and save bank
        Bank bank = new Bank(User.getBanks().size(), currency, balance, exchangeRate);
        User.addBank(bank);
        String formattedExchangeRate = String.format("%.6f", exchangeRate);
        printMessage("  Added " + bank + " | Exchange rate to SGD: " + formattedExchangeRate);

        return null;
    }


}
