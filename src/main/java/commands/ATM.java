package commands;

import bank.Bank;
import ui.FinanceException;

import java.util.ArrayList;

public class ATM implements Command{
    private float amount;
    private Bank curr_bank;
    private boolean deposit;
    private boolean withdraw;

    public ATM(ArrayList<String> arguments, Bank curr_bank, boolean deposit, boolean withdraw){
        this.amount = Float.parseFloat(arguments.get(0));
        this.curr_bank = curr_bank;
        this.deposit = deposit;
        this.withdraw = withdraw;
    }

    @Override
    public void execute() throws FinanceException {
        float new_balance;
        if(this.deposit && !this.withdraw){
            new_balance = this.curr_bank.getBalance() + this.amount;
            this.curr_bank.setBalance(new_balance);
            System.out.println("Successful deposit! Your bank balance is now:" + this.curr_bank.getBalance());
        }
        if(this.withdraw && !this.deposit){
            new_balance = this.curr_bank.getBalance() - this.amount;
            this.curr_bank.setBalance(new_balance);
            System.out.println("Successful withdrawal! Your bank balance is now:" + this.curr_bank.getBalance());
        }
    }
}
