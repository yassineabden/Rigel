package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class MySunTest {

    @Test
     public void isConstructorValid() {
       Sun s1 = new Sun(EclipticCoordinates.of(1.2,1.3), EquatorialCoordinates.of(0.5, 1.0),2,4);
       assertEquals(EclipticCoordinates.of(1.2,1.3).toString(),s1.eclipticPos().toString());
       assertEquals("Soleil",s1.name());
       assertEquals(-26.7,s1.magnitude(),1e-6);
       assertEquals(EquatorialCoordinates.of(0.5, 1.0).toString(),s1.equatorielPos().toString());
       assertEquals(2, s1.angularSize());
       assertEquals(4,s1.meanAnomaly());


       assertThrows(NullPointerException.class, () -> {
                Sun sun = new Sun(null,EquatorialCoordinates.of(0.5, 1.0),2,4);
            });
       assertThrows(IllegalArgumentException.class, () -> {
                Sun sun = new Sun(EclipticCoordinates.of(1.2,1.3),EquatorialCoordinates.of(0.5, 1.0),-5,4);
            });
       assertThrows(NullPointerException.class, () -> {
                Sun sun = new Sun(EclipticCoordinates.of(1.2,1.3),null,7,4);
            }); }






    }










