package command;

import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionsCommand implements Command {

    private final List<Transaction> transactionsToRemove;

    private final Account account;

    public RemoveTransactionsCommand(List<Transaction> transactionsToRemove, Account account) {
        this.transactionsToRemove = transactionsToRemove;
        this.account = account;
    }

    @Override
    public void execute() {
        for(Transaction transaction : transactionsToRemove){
            account.removeTransaction(transaction);
        }
    }

    @Override
    public String getName() {
        if (transactionsToRemove.size() == 1){
            return "Removed transaction: " + transactionsToRemove.getFirst().toString();
        }
        return transactionsToRemove.size() + " transactions removed.";
    }

    @Override
    public void undo() {
        for(Transaction transaction : transactionsToRemove){
            account.addTransaction(transaction);
        }
    }

    @Override
    public void redo() {
        for(Transaction transaction : transactionsToRemove){
            account.removeTransaction(transaction);
        }
    }

}
