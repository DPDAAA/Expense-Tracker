package tracker.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionContainer implements Iterable<Transaction> {

    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public Iterator<Transaction> iterator() {
        return transactions.iterator();
    }

    public void addTransaction(Transaction transaction) {

        if (transactions.contains(transaction)) {
            throw new IllegalStateException("Transaction already contained");
        }
        transactions.add(transaction);

    }

    public void deleteTransaction(Transaction transaction) {
        if (!transactions.contains(transaction)) {
            return;
        }
        transactions.remove(transaction);

    }

    public boolean deleteById(int searchId) {

        return this.transactions.removeIf(t -> t.getId() == searchId);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

}
