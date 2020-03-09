package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import org.junit.jupiter.api.Test;

import java.text.Format;

import static org.junit.jupiter.api.Assertions.*;

public class MyMoonTest {

    @Test
    void IsMoonWellConstructed() {
        Moon m1 = new Moon(EquatorialCoordinates.of(Math.PI/3,Math.PI/3), 4, 2, 1);
        Moon m2 = new Moon(EquatorialCoordinates.of(Angle.TAU/3, -Math.PI/3), 2.3456f, 6.6787f, 0.3752f);

        assertEquals("Lune", m1.name());
        assertEquals("(ra=4.0000h, dec=60.0000°)", m1.EquatorielPos().toString());
        assertEquals(2f, m1.magnitude());
        assertEquals(4f, m1.angularSize());
        assertEquals("Lune (100.0%)", m1.info());
        assertEquals(m1.info(), m1.toString());

        assertEquals("Lune", m2.name());
        assertEquals("(ra=8.0000h, dec=-60.0000°)", m2.EquatorielPos().toString());
        assertEquals(6.6787f, m2.magnitude());
        assertEquals(2.3456f, m2.angularSize());
        assertEquals("Lune (37.5%)", m2.info());
        assertEquals(m1.info(), m1.toString());

    }

    @Test
    void MoonThrowsNullPointerException() {

        assertThrows(NullPointerException.class, () -> {
            new Moon(null, 2.3456f, 6.6787f, 0.5678f);
        });
    }

    @Test
    void MoonThrowsIllegalArgumentExceptionWhenPhaseIsNotInInterval(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(EquatorialCoordinates.of(Math.PI/3,Math.PI/3), 2.3456f, 6.6787f, 1.2f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(EquatorialCoordinates.of(Math.PI/3,Math.PI/3), 2.3456f, 6.6787f, -0.6f);
        });
    }


}
