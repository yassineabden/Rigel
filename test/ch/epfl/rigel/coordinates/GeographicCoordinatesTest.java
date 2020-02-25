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
    assertTrue(GeographicCoordinates.isValidLonDeg(40));
    assertTrue(GeographicCoordinates.isValidLonDeg(179));
    }
    @Test
    void isValidLatDegWithValidValues(){
    assertTrue(GeographicCoordinates.isValidLatDeg(90));
    assertTrue(GeographicCoordinates.isValidLatDeg(40));


    }

    @Test
    void toStringWorksonGeographicCoordinates(){
        assertEquals("(lon=-120.0000°, lat=2.0000°)",GeographicCoordinates.ofDeg(-120,2).toString());
        assertEquals("(lon=40.5434°, lat=-80.7672°)",GeographicCoordinates.ofDeg(40.54342, -80.76722).toString());
        assertEquals("(lon=120.0000°, lat=2.0000°)",GeographicCoordinates.ofDeg(120,2).toString());
        assertEquals("(lon=100.9899°, lat=89.8787°)",GeographicCoordinates.ofDeg(100.9898776,89.87872).toString());


    }


}




