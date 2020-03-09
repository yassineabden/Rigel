package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class MyPlanetTest {

    @Test
    public void isConstructorValidwithValidValues() {
        Planet p = new Planet("Saturne", EquatorialCoordinates.of(0.5, 1.5), 12, 8);
        assertEquals("Saturne", p.name());
        assertEquals(EquatorialCoordinates.of(0.5, 1.5).toString(), p.equatorielPos().toString());
        assertEquals(12, p.angularSize());
        assertEquals(8, p.magnitude());
    }


    @Test
    public void isConstructorValidwithInvalidValues() {
        assertThrows(NullPointerException.class, () -> {
            Planet p= new Planet(null,EquatorialCoordinates.of(0.5, 1.5), 12, 8);
        });
        assertThrows(NullPointerException.class, () -> {
            Planet p= new Planet("oui",null, 12, 8);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Planet p= new Planet("cool",EquatorialCoordinates.of(0.5, 1.5), -4, 8);
        }); }








}