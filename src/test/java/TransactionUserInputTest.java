import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import tracker.model.Type;
import tracker.ui.console.TransactionUserInput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TransactionUserInput liest über einen Scanner direkt von System.in.
 * Um das ohne echte Tastatureingabe zu testen, ersetzen wir System.in
 * vor jedem Test durch simulierte Eingaben.
 */
class TransactionUserInputTest {

    private final InputStream echterSystemIn = System.in;

    @AfterEach
    void aufraeumen() {
        System.setIn(echterSystemIn);
    }

    private TransactionUserInput mitEingabe(String eingabe) {
        System.setIn(new ByteArrayInputStream(eingabe.getBytes(StandardCharsets.UTF_8)));
        return new TransactionUserInput();
    }

    @Test
    void inputSwitch_gibtErstesZeichenZurueck() {
        TransactionUserInput input = mitEingabe("g\n");

        assertEquals('g', input.inputSwitch());
    }

    @Test
    void inputSwitch_ueberspringtLeereZeilen() {
        TransactionUserInput input = mitEingabe("\n\nh\n");

        assertEquals('h', input.inputSwitch());
    }

    @Test
    void inputDescription_gibtEingabeZeileZurueck() {
        TransactionUserInput input = mitEingabe("Kaffee beim Bäcker\n");

        assertEquals("Kaffee beim Bäcker", input.inputDescription());
    }

    @Test
    void inputType_akzeptiertGueltigenTyp() {
        TransactionUserInput input = mitEingabe("AUSGABE\n");

        assertEquals(Type.AUSGABE, input.inputType());
    }

    @Test
    void inputType_istGrossKleinschreibungsUnabhaengig() {
        TransactionUserInput input = mitEingabe("einnahme\n");

        assertEquals(Type.EINNAHME, input.inputType());
    }

    @Test
    void inputType_fragtBeiUngueltigemWertErneut() {
        TransactionUserInput input = mitEingabe("nichtGueltig\nEINNAHME\n");

        assertEquals(Type.EINNAHME, input.inputType());
    }

    @Test
    void inputSum_akzeptiertGueltigenWert() {
        TransactionUserInput input = mitEingabe("42.5\n");

        assertEquals(42.5, input.inputSum());
    }

    @Test
    void inputSum_fragtBeiNegativemWertErneut() {
        TransactionUserInput input = mitEingabe("-5\n10\n");

        assertEquals(10.0, input.inputSum());
    }

    @Test
    void inputSum_fragtBeiUngueltigerEingabeErneut() {
        TransactionUserInput input = mitEingabe("abc\n7\n");

        assertEquals(7.0, input.inputSum());
    }

    @Test
    void inputTransactionId_akzeptiertGueltigeId() {
        TransactionUserInput input = mitEingabe("5\n");

        assertEquals(5, input.inputTransactionId());
    }

    @Test
    void inputTransactionId_fragtBeiNegativerZahlErneut() {
        TransactionUserInput input = mitEingabe("-1\n3\n");

        assertEquals(3, input.inputTransactionId());
    }

    @Test
    void inputTransactionId_fragtBeiUngueltigerEingabeErneut() {
        TransactionUserInput input = mitEingabe("abc\n8\n");

        assertEquals(8, input.inputTransactionId());
    }
}
