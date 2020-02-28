package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class HorizontalCoordinatesTest {

    @Test
    void ofWorksWithValidValues(){
        HorizontalCoordinates hc1 = HorizontalCoordinates.of(0,Math.PI/4);
        assertEquals(0,hc1.az());
        assertEquals(Math.PI/4,hc1.alt());
        HorizontalCoordinates hc2 = HorizontalCoordinates.of(3.13,-Math.PI/2);
        assertEquals(3.13,hc2.az());
        assertEquals(-Math.PI/2,hc2.alt()); }

    @Test
    void ofWorksWithInvalidValues(){
        assertThrows(IllegalArgumentException.class,()->{
            HorizontalCoordinates.of(3,-Math.PI); });
        assertThrows(IllegalArgumentException.class,()->{
            HorizontalCoordinates.of(2.54,(Math.PI/2)+1);
        });
    }

    @Test
    void ofDegWorksWithValidValues(){
        HorizontalCoordinates hc3= HorizontalCoordinates.ofDeg(200,85);
        assertEquals(200, hc3.azDeg());
        assertEquals(85,hc3.altDeg());
        assertEquals(40.567,HorizontalCoordinates.ofDeg(40.567,89.9999).azDeg());
        assertEquals(89.9999,HorizontalCoordinates.ofDeg(40.567,89.9999).altDeg()); }
    @Test
    void ofDegWorksWithInvalidValues(){
        assertThrows(IllegalArgumentException.class,()->{
            HorizontalCoordinates.ofDeg(380,80);
        });
        assertThrows(IllegalArgumentException.class,()->{
            HorizontalCoordinates.ofDeg(100,95);
        });
    }

    @Test
    void isAzGetterValid(){
        HorizontalCoordinates hc4= HorizontalCoordinates.of(Math.PI,1.54345);
        assertEquals(Math.PI,hc4.az());
        HorizontalCoordinates hc5= HorizontalCoordinates.ofDeg(180,-90);
        assertEquals(Math.PI,hc5.az());
    }

    @Test
    void isAzDegGetterValid(){
        HorizontalCoordinates hc4= HorizontalCoordinates.ofDeg(130,75);
        assertEquals(130,hc4.azDeg());
        HorizontalCoordinates hc5= HorizontalCoordinates.of(Math.PI,1.54345);
        assertEquals(180,hc5.azDeg());
    }

    @Test
    void isAzOctantNameValid(){
        assertEquals("NO",HorizontalCoordinates.ofDeg(335,0).azOctantName("N","E","S","O"));
        assertEquals("NE",HorizontalCoordinates.ofDeg(22.5,0).azOctantName("N","E","S","O"));
        assertEquals("N",HorizontalCoordinates.ofDeg(350,0).azOctantName("N","E","S","O"));
        assertEquals("S",HorizontalCoordinates.ofDeg(180,0).azOctantName("N","E","S","O"));
        assertEquals("SO",HorizontalCoordinates.ofDeg(230,12).azOctantName("N","E","S","O"));
        assertEquals("SE",HorizontalCoordinates.ofDeg(150,89).azOctantName("N","E","S","O")); }

    @Test
    void isAltGetterValid(){
        HorizontalCoordinates hc4= HorizontalCoordinates.of(Math.PI,1.54345);
        assertEquals(1.54345,hc4.alt());
        HorizontalCoordinates hc5= HorizontalCoordinates.ofDeg(180,-90);
        assertEquals(-Math.PI/2,hc5.alt());
    }

    @Test
    void isAltDegGetterValid(){
        HorizontalCoordinates hc4= HorizontalCoordinates.ofDeg(130,75);
        assertEquals(75,hc4.altDeg());
        HorizontalCoordinates hc6= HorizontalCoordinates.of(Math.PI,Math.PI/3);
        assertEquals(60,hc6.altDeg(),1e6);
    }

    @Test
   void toStringWorksonHorizontalCoordinates(){
        assertEquals("(az=350.0000°, alt=7.2000°)", HorizontalCoordinates.ofDeg(350,7.2).toString());
        assertEquals("(az=45.9879°, alt=-85.7876°)", HorizontalCoordinates.ofDeg(45.987897,-85.7876).toString());
        assertEquals("(az=80.7654°, alt=-90.0000°)", HorizontalCoordinates.ofDeg(80.76543,-90).toString());
    }
}












