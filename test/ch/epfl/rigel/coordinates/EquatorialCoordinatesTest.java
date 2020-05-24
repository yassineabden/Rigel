package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class EquatorialCoordinatesTest {
    @Test
    void ofWorksWithValidValues(){
        assertEquals(1.435,EquatorialCoordinates.of(1.435,0.89).ra());
        assertEquals(0.89,EquatorialCoordinates.of(1.435,0.89).dec()); }

    @Test
    void ofWorksWithInvalidValues(){
        assertThrows(IllegalArgumentException.class,()->{
            EquatorialCoordinates.of(Angle.TAU,2.34); });
        assertThrows(IllegalArgumentException.class,()->{
            EquatorialCoordinates.of(2.54,(Math.PI/2)+1);
        }); }

    @Test
    void isRaGetterValid(){
        EquatorialCoordinates hc4= EquatorialCoordinates.of(Math.PI,1.54345);
        assertEquals(Math.PI,hc4.ra());
    }

    @Test
    void isRaDegGetterValid(){
        EquatorialCoordinates hc4= EquatorialCoordinates.of(Math.PI,1.54345);;
        assertEquals(180,hc4.raDeg()
        ); }

    @Test
    void isRaHrGetterValid(){
        EquatorialCoordinates hc4= EquatorialCoordinates.of(Math.PI,1.54345);;
        assertEquals(12,hc4.raHr()
        ); }

    @Test
    void isDecGetterValid(){
        EquatorialCoordinates hc4= EquatorialCoordinates.of(Math.PI,Math.PI/2);;
        assertEquals(Math.PI/2,hc4.dec()
        ); }

    @Test
    void isDecDegGetterValid(){
        EquatorialCoordinates hc4= EquatorialCoordinates.of(Math.PI,Math.PI/2);;
        assertEquals(90,hc4.decDeg()
        ); }


    @Test
    void toStringWorksonEclipticCoordinates(){
        assertEquals("(ra=4.0000h, dec=60.0000°)",EquatorialCoordinates.of(Math.PI/3,Math.PI/3).toString());
    }






























}
