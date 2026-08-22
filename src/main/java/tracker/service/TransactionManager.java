package tracker.service;
import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.storage.TransactionFileStorage;

public class TransactionManager {

    public void TransactionHistory(TransactionContainer Container) {

        System.out.println("---Verlauf---");
        Container.forEach(t -> System.out.println(t));
    }

    public void addTransaction(TransactionContainer Container, String description, Type type, double sum) {

        Transaction transaction = new Transaction(description, type, sum);
        Container.addTransaction(transaction);

    }

    public boolean deleteTransaction(TransactionContainer Container, int id) {

        return Container.deleteById(id);

    }

    public void saveTransactions(TransactionContainer Container, String dateiPfad) {
        TransactionFileStorage.saveTransactions(Container, dateiPfad);
        System.out.println("---Erfolgreich gespeichert!---");

    }


}
