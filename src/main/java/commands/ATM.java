package commands;

import bank.Bank;
import ui.FinanceException;
import user.User;

import java.util.ArrayList;

public class ATM implements Command{
    private float amount;
    private Bank currBank;
    private boolean deposit;
    private boolean withdraw;

    public ATM(ArrayList<String> arguments, Bank currBank, boolean deposit, boolean withdraw){
        this.amount = Float.parseFloat(arguments.get(0));
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
            System.out.println("Successful deposit! Your bank balance is now: "
                    + this.currBank.getCurrency().getSymbol()
                    + this.currBank.getBalance());
        }
        if(this.withdraw && !this.deposit){
            newBalance = this.currBank.getBalance() - this.amount;
            this.currBank.setBalance(newBalance);
            System.out.println("Successful withdrawal! Your bank balance is now: "
                    + this.currBank.getCurrency().getSymbol()
                    + this.currBank.getBalance());
        }
        User.getStorage().saveBanks(User.getBanks());
        return null;
    }
}
