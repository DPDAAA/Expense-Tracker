package tracker.service;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.storage.TransactionFileStorage;

public class TransactionManager {

    private static final String FILE_PATH = "Speicher.txt";
    private TransactionContainer container;

    public void TransactionHistory() {

        System.out.println("---Verlauf---");
        this.container.forEach(t -> System.out.println(t));
    }

    public TransactionManager() {
        this.container = TransactionFileStorage.loadTransactions(FILE_PATH);
    }

    public void addTransaction(String description, Type type, double sum) {

        Transaction transaction = new Transaction(description, type, sum);
        this.container.addTransaction(transaction);

    }

    public boolean deleteTransaction(int id) {

        return this.container.deleteById(id);

    }

    public void saveTransactions() {
        TransactionFileStorage.saveTransactions(this.container, FILE_PATH);
        System.out.println("---Erfolgreich gespeichert!---");

    }

}
