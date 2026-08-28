package model;
import org.junit.jupiter.api.Test;

import tracker.model.Type;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    @Test
    void enumHatGenauZweiWerte() {
        assertEquals(2, Type.values().length);
    }

    @Test
    void valueOf_findetAusgabe() {
        assertEquals(Type.AUSGABE, Type.valueOf("AUSGABE"));
    }

    @Test
    void valueOf_findetEinnahme() {
        assertEquals(Type.EINNAHME, Type.valueOf("EINNAHME"));
    }

    @Test
    void valueOf_wirftBeiUnbekanntemWert() {
        assertThrows(IllegalArgumentException.class, () -> Type.valueOf("SONSTIGES"));
    }
}
