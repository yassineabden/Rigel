package ch.epfl.rigel.coordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeographicCoordinatesTest {

    @Test
    void ofWorksWithValidValues(){
    GeographicCoordinates gc= GeographicCoordinates.ofDeg(100,40);
    assertEquals(100,gc.lonDeg());
    assertEquals(40,gc.latDeg()); }

    @Test
    void ofFailsOnInvalidValues() {
    assertThrows(IllegalArgumentException.class,()->{
        GeographicCoordinates.ofDeg(380,80);
        });
    assertThrows(IllegalArgumentException.class,()->{
        GeographicCoordinates.ofDeg(120,-100);
        });
    }

    @Test
    void isValidLonDegWithValidValues(){
    GeographicCoordinates gc2= GeographicCoordinates.ofDeg(100,40);
    assertTrue(GeographicCoordinates.isValidLatDeg(40));
    assertTrue(GeographicCoordinates.isValidLonDeg(100));
    }

    /*@Test
    void isValidLonDegWithInValidValues(){
        GeographicCoordinates gc3= GeographicCoordinates.ofDeg(200,-100);
        assertFalse(GeographicCoordinates.isValidLonDeg(200));
        assertFalse(GeographicCoordinates.isValidLatDeg(-100));

    } */



}
