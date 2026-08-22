package tracker.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;

public class TransactionFileStorage {

    public static void saveTransactions(TransactionContainer p, String dateiPfad) {
        System.out.println("Speichervorgang...");
        Path pfad = Path.of(dateiPfad);

        try {

            Files.writeString(pfad, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            for (Transaction t : p) {
                try {
                    String text = t.getId() + ";" + t.getDescription() + ";" + t.getType() + ";" + t.getSum()
                            + System.lineSeparator();

                    Files.writeString(pfad, text, StandardOpenOption.APPEND);
                    System.out.println("Transaction ID: " + t.getId() + " wurde erfolgreich geschrieben!");
                } catch (IOException e) {
                    System.err.println("Fehler beim Schreiben von ID: " + t.getId());
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Initialisieren der Datei: " + e.getMessage());
        }
    }

    public static TransactionContainer loadTransactions(String dateiPfad) {

        TransactionContainer Container = new TransactionContainer();
        Path pfad = Path.of(dateiPfad);
        if (!Files.exists(pfad)) {
            System.out.println("Keine Speicherdatei gefunden. Starte mit leerem Container");
            return Container;
        }
        try {
            List<String> zeilen = Files.readAllLines(pfad);
            for (String zeile : zeilen) {

                String[] zeilen2 = zeile.split(";");

                int id = Integer.parseInt(zeilen2[0]);
                String description = zeilen2[1];
                Type type = Type.valueOf(zeilen2[2]);
                double sum = Double.parseDouble(zeilen2[3]);

                Transaction transaction = new Transaction(id, description, type, sum);
                Container.addTransaction(transaction);

            }

        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Datei beschädigt oder im falschen Format!");
        }

        return Container;
    }
}
