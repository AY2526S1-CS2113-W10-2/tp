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
    public void execute() throws FinanceException {
        if (arguments.size() < 3) {
            throw new FinanceException("  Sorry! Wrong format. Try 'addBank <balance> <currency> <exchangerate>' \n" +
                    "  e.g. 'addBank 1000.00 JPY 0.01'");
        }
        try{
            float balance       = Float.parseFloat(arguments.get(0));
            Currency currency   = Currency.toCurrency(arguments.get(1));
            float exchangeRate  = Float.parseFloat(arguments.get(2));
            Bank bank = new Bank(User.getBanks().size(), currency, balance, exchangeRate);
            User.addBank(bank);
            printMessage("  Added " + bank);
        }catch (NumberFormatException e) {
            throw new FinanceException("Balance or exchange rate must be a valid number.");
        } catch (IllegalArgumentException e) {
            throw new FinanceException("Invalid currency or input: " + e.getMessage());
        }
    }
}
