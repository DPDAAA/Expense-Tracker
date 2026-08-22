import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.service.TransactionManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    private TransactionManager manager;
    private TransactionContainer container;

    @BeforeEach
    void setUp() {
        manager = new TransactionManager();
        container = new TransactionContainer();
    }

    @Test
    void addTransaction_legtNeueTransaktionImContainerAn() {
        manager.addTransaction(container, "Kaffee", Type.AUSGABE, 3.5);

        int count = 0;
        for (Transaction t : container) {
            count++;
            assertEquals("Kaffee", t.getDescription());
            assertEquals(Type.AUSGABE, t.getType());
            assertEquals(3.5, t.getSum());
        }
        assertEquals(1, count);
    }

    @Test
    void deleteTransaction_gibtErfolgsmeldungBeiVorhandenerId() {
        manager.addTransaction(container, "Test", Type.AUSGABE, 5.0);
        int id = container.iterator().next().getId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            manager.deleteTransaction(container, id);
        } finally {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("gelöscht"));
        assertFalse(container.iterator().hasNext());
    }

    @Test
    void deleteTransaction_gibtFehlermeldungBeiUnbekannterId() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            manager.deleteTransaction(container, -1);
        } finally {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("Keine Transaktion"));
    }

    @Test
    void transactionHistory_druckVerlaufOhneFehler() {
        manager.addTransaction(container, "Test", Type.AUSGABE, 5.0);

        // Wir prüfen hier nur, dass die Ausgabe nicht crasht und
        // die Beschreibung im Verlauf auftaucht - reine Konsolenausgabe
        // ist sonst schwer sinnvoll zu testen (siehe Feedback zur Architektur).
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            manager.TransactionHistory(container);
        } finally {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("Test"));
    }
}
