import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tracker.model.Transaction;
import tracker.model.TransactionContainer;
import tracker.model.Type;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class TransactionContainerTest {

    private TransactionContainer container;

    @BeforeEach
    void setUp() {
        container = new TransactionContainer();
    }

    @Test
    void addTransaction_fuegtTransaktionHinzu() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 10.0);

        container.addTransaction(t);

        Iterator<Transaction> it = container.iterator();
        assertTrue(it.hasNext());
        assertEquals(t, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void addTransaction_wirftBeiDuplikat() {
        Transaction t = new Transaction(1, "Test", Type.AUSGABE, 10.0);
        Transaction gleicheId = new Transaction(1, "Anderer Text", Type.EINNAHME, 20.0);

        container.addTransaction(t);

        assertThrows(IllegalStateException.class, () -> container.addTransaction(gleicheId));
    }

    @Test
    void deleteTransaction_entferntVorhandeneTransaktion() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 10.0);
        container.addTransaction(t);

        container.deleteTransaction(t);

        assertFalse(container.iterator().hasNext());
    }

    @Test
    void deleteTransaction_ohneVorhandeneTransaktionAendertNichts() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 10.0);

        // t wurde nie hinzugefügt -> darf keine Exception werfen
        assertDoesNotThrow(() -> container.deleteTransaction(t));
    }

    @Test
    void deleteById_entferntBeiTreffer() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 10.0);
        container.addTransaction(t);

        boolean erfolg = container.deleteById(t.getId());

        assertTrue(erfolg);
        assertFalse(container.iterator().hasNext());
    }

    @Test
    void deleteById_liefertFalseBeiUnbekannterId() {
        boolean erfolg = container.deleteById(-1);

        assertFalse(erfolg);
    }

    @Test
    void iterator_gibtAlleHinzugefuegtenTransaktionenZurueck() {
        Transaction t1 = new Transaction("A", Type.AUSGABE, 1.0);
        Transaction t2 = new Transaction("B", Type.EINNAHME, 2.0);
        container.addTransaction(t1);
        container.addTransaction(t2);

        int count = 0;
        for (Transaction t : container) {
            count++;
        }

        assertEquals(2, count);
    }
}
