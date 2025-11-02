package commands;

import bank.Bank;
import ui.FinanceException;
import ui.OutputManager;
import user.User;

import java.util.ArrayList;

public class ATM implements Command{
    private float amount;
    private Bank currBank;
    private boolean deposit;
    private boolean withdraw;

    public ATM(ArrayList<String> arguments, Bank currBank, boolean deposit, boolean withdraw) throws FinanceException {

        if (arguments.size() != 1) {
            throw new FinanceException("Invalid format! Usage: withdraw <amount>");
        }
        try {
            this.amount = Float.parseFloat(arguments.get(0));
        } catch (NumberFormatException e) {
            throw new FinanceException("Amount must be a valid number. Your input: " + arguments.get(0));
        }
        if (this.amount <= 0) {
            throw new FinanceException("Amount withdrawn or deposited must be a positive value");
        }
        if ((this.amount + currBank.getBalance()) >= Float.MAX_VALUE) {
            throw new FinanceException("Max possible value of account " + Float.MAX_VALUE + " exceeded, you attempted to set to " + this.amount + currBank.getBalance() + ". Try with a lower number.");
        }
        this.currBank = currBank;
        this.deposit = deposit;
        this.withdraw = withdraw;
    }

    @Override
    public String execute() throws FinanceException {
        float newBalance;
        if(this.deposit && !this.withdraw){
            newBalance = this.currBank.getBalance() + this.amount;
            this.currBank.setBalance(newBalance);

            OutputManager.printMessage("Successful deposit! Your bank balance is now: "
                    + this.currBank.getCurrency().getSymbol()
                    + this.currBank.getBalance());
        }
        if(this.withdraw && !this.deposit){
            if (this.amount > this.currBank.getBalance()) {
                throw new FinanceException("Insufficient funds. Your balance is "
                        + this.currBank.getCurrency().getSymbol() + this.currBank.getBalance());
            }
            newBalance = this.currBank.getBalance() - this.amount;
            this.currBank.setBalance(newBalance);

            OutputManager.printMessage("Successful withdrawal! Your bank balance is now: "
                    + this.currBank.getCurrency().getSymbol()
                    + this.currBank.getBalance());
        }
        User.getStorage().saveBanks(User.getBanks());
        return null;
    }
}
