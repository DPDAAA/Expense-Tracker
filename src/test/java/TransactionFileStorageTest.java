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
        Path datei = tempDir.resolve("speicher.txt");

        TransactionContainer original = new TransactionContainer();
        Transaction t1 = new Transaction("Miete", Type.AUSGABE, 500.0);
        Transaction t2 = new Transaction("Gehalt", Type.EINNAHME, 2000.0);
        original.addTransaction(t1);
        original.addTransaction(t2);

        TransactionFileStorage.saveTransactions(original, datei.toString());
        TransactionContainer geladen = TransactionFileStorage.loadTransactions(datei.toString());

        int count = 0;
        for (Transaction t : geladen) {
            count++;
        }
        assertEquals(2, count);
        assertTrue(geladen.deleteById(t1.getId()));
        assertTrue(geladen.deleteById(t2.getId()));
    }

    @Test
    void loadTransactions_liefertLeerenContainerWennDateiFehlt(@TempDir Path tempDir) {
        Path nichtVorhandeneDatei = tempDir.resolve("existiert-nicht.txt");

        TransactionContainer container = TransactionFileStorage.loadTransactions(nichtVorhandeneDatei.toString());

        assertFalse(container.iterator().hasNext());
    }

    @Test
    void loadTransactions_ignoriertKaputteDateiOhneAbsturz(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("kaputt.txt");
        Files.writeString(datei, "das;ist;keine;gueltige;zeile;mit;zu;vielen;feldern\n");

        // Darf nicht crashen - Fehlerbehandlung fängt NumberFormatException /
        // ArrayIndexOutOfBoundsException ab.
        assertDoesNotThrow(() -> TransactionFileStorage.loadTransactions(datei.toString()));
    }

    @Test
    void saveTransactions_schreibtLeereDateiBeiLeeremContainer(@TempDir Path tempDir) throws IOException {
        Path datei = tempDir.resolve("leer.txt");
        TransactionContainer leer = new TransactionContainer();

        TransactionFileStorage.saveTransactions(leer, datei.toString());

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

        TransactionFileStorage.saveTransactions(container, datei.toString());

        String inhalt = Files.readString(datei);
        assertFalse(inhalt.contains("alter"));
        assertTrue(inhalt.contains("Neu"));
    }
}
