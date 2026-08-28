package storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.storage.TransactionFileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFileStorageTest {

    @Test
    void saveUndLoad_roundtripErhaeltDaten(@TempDir Path tempDir) throws IOException {
       

        TransactionContainer original = new TransactionContainer();
        Transaction t1 = new Transaction("Miete", Type.AUSGABE, 500.0);
        Transaction t2 = new Transaction("Gehalt", Type.EINNAHME, 2000.0);
        original.addTransaction(t1);
        original.addTransaction(t2);

        TransactionFileStorage.saveTransactions(original);
        TransactionContainer geladen = TransactionFileStorage.loadTransactions();

        int count = 0;
        for (Transaction t : geladen) {
            count++;
        }
        assertEquals(2, count);
        assertTrue(geladen.deleteById(t1.getId()));
        assertTrue(geladen.deleteById(t2.getId()));
    }


    @Test
    void loadTransactions_ignoriertKaputteDateiOhneAbsturz(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("kaputt.txt");
        Files.writeString(datei, "das;ist;keine;gueltige;zeile;mit;zu;vielen;feldern\n");


        assertDoesNotThrow(() -> TransactionFileStorage.loadTransactions());
    }

    @Test
    void saveTransactions_schreibtLeereDateiBeiLeeremContainer(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("leer.txt");
        TransactionContainer leer = new TransactionContainer();

        TransactionFileStorage.saveTransactions(leer);

        assertTrue(Files.exists(datei));
        assertEquals("", Files.readString(datei));
    }

    @Test
    void saveTransactions_ueberschreibtBestehendeDatei(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("ueberschreiben.txt");
        Files.writeString(datei, "alter;inhalt;der;ueberschrieben;werden;soll\n");

        TransactionContainer container = new TransactionContainer();
        Transaction t = new Transaction("Neu", Type.EINNAHME, 1.0);
        container.addTransaction(t);

        TransactionFileStorage.saveTransactions(container);

        String inhalt = Files.readString(datei);
        assertFalse(inhalt.contains("alter"));
        assertTrue(inhalt.contains("Neu"));
    }
}
