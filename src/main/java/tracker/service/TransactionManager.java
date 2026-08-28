package tracker.service;

import java.util.List;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.storage.TransactionFileStorage;

public class TransactionManager {

    private TransactionContainer container;

    public void TransactionHistory() {

        System.out.println("---Verlauf---");
        this.container.forEach(t -> System.out.println(t));
    }

    public TransactionManager() {
        this.container = TransactionFileStorage.loadTransactions();
    }

    public void addTransaction(String description, Type type, double sum) {

        Transaction transaction = new Transaction(description, type, sum);
        this.container.addTransaction(transaction);

    }

    public boolean deleteTransaction(int id) {

        return this.container.deleteById(id);

    }

    public void saveTransactions() {
        TransactionFileStorage.saveTransactions(this.container);
        System.out.println("---Erfolgreich gespeichert!---");

    }

    public List<Transaction> getContainer() {

        return this.container.getTransactions();
    }

    public double getTotal(Type type) {
        double total = 0;

    for (Transaction t : this.container) {
        if(t.getType() == type) {
            total += t.getSum();
        }
    }
    return total;
    }

}
