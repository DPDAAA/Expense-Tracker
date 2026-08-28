package model;
import org.junit.jupiter.api.Test;

import tracker.model.Transaction;
import tracker.model.Type;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void konstruktor_setztFelderKorrekt() {
        Transaction t = new Transaction("Miete", Type.AUSGABE, 500.0);

        assertEquals("Miete", t.getDescription());
        assertEquals(Type.AUSGABE, t.getType());
        assertEquals(500.0, t.getSum());
    }

    @Test
    void konstruktor_vergibtFortlaufendeId() {
        Transaction t1 = new Transaction("A", Type.EINNAHME, 10.0);
        Transaction t2 = new Transaction("B", Type.EINNAHME, 20.0);


        assertEquals(t1.getId() + 1, t2.getId());
    }

    @Test
    void konstruktorMitId_uebernimmtGegebeneId() {
        Transaction t = new Transaction(9999, "Import", Type.EINNAHME, 42.0);

        assertEquals(9999, t.getId());
        assertEquals("Import", t.getDescription());
    }

    @Test
    void konstruktorMitId_erhoehtNextIdWennGroesser() {
        Transaction t1 = new Transaction(50000, "Alt", Type.AUSGABE, 5.0);
        Transaction t2 = new Transaction("Neu", Type.AUSGABE, 5.0);

        assertEquals(50001, t2.getId());
        assertNotEquals(t1.getId(), t2.getId());
    }

    @Test
    void setType_wirftBeiNull() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 1.0);

        assertThrows(IllegalArgumentException.class, () -> t.setType(null));
    }

    @Test
    void setSum_wirftBeiNull() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 1.0);

        assertThrows(IllegalArgumentException.class, () -> t.setSum(0));
    }

    @Test
    void setSum_wirftBeiNegativemWert() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 1.0);

        assertThrows(IllegalArgumentException.class, () -> t.setSum(-5.0));
    }

    @Test
    void setSum_akzeptiertPositivenWert() {
        Transaction t = new Transaction("Test", Type.AUSGABE, 1.0);

        t.setSum(123.45);

        assertEquals(123.45, t.getSum());
    }

    @Test
    void equals_gleicheIdSindGleich() {
        Transaction t1 = new Transaction(1, "A", Type.AUSGABE, 1.0);
        Transaction t2 = new Transaction(1, "B", Type.EINNAHME, 2.0);


        assertEquals(t1, t2);
    }

    @Test
    void equals_unterschiedlicheIdSindUngleich() {
        Transaction t1 = new Transaction(1, "A", Type.AUSGABE, 1.0);
        Transaction t2 = new Transaction(2, "A", Type.AUSGABE, 1.0);

        assertNotEquals(t1, t2);
    }

    @Test
    void equals_mitNullIstFalse() {
        Transaction t1 = new Transaction(1, "A", Type.AUSGABE, 1.0);

        assertFalse(t1.equals(null));
    }

    @Test
    void equals_mitAnderemTypIstFalse() {
        Transaction t1 = new Transaction(1, "A", Type.AUSGABE, 1.0);

        assertFalse(t1.equals("keine Transaction"));
    }

    @Test
    void hashCode_entsprichtId() {
        Transaction t = new Transaction(7, "A", Type.AUSGABE, 1.0);

        assertEquals(7, t.hashCode());
    }

    @Test
    void toString_enthaeltAlleFelder() {
        Transaction t = new Transaction(3, "Kaffee", Type.AUSGABE, 4.5);

        String result = t.toString();

        assertTrue(result.contains("Kaffee"));
        assertTrue(result.contains("AUSGABE"));
        assertTrue(result.contains("4.5"));
        assertTrue(result.contains("3"));
    }
}
