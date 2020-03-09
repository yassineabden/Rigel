package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class MyPlanet {

    @Test
    public void isConstructorValid(){
        Planet p= new Planet("Saturne", EquatorialCoordinates.of(0.5,1.5),12,8);
        assertEquals("Saturne",p.name());
        assertEquals(EquatorialCoordinates.of(0.5,1.5).toString(),p.EquatorielPos().toString());
        assertEquals(12,p.angularSize());
        assertEquals(8,p.magnitude()); }
}
