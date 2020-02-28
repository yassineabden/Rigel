package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
public class EclipticCoordinatesTest {

        @Test
    void ofWorksWithValidValues(){
        EclipticCoordinates ec= EclipticCoordinates.of(0,1.0003);
        assertEquals(0,ec.lon());
        assertEquals(1.0003,ec.lat()); }

        @Test
    void ofFailsOnInvalidValues() {
            assertThrows(IllegalArgumentException.class,()->{
                EclipticCoordinates.of(Angle.TAU,1.003); });
            assertThrows(IllegalArgumentException.class,()->{
                EclipticCoordinates.of(1.5,(Math.PI/2)+1); });
            assertThrows(IllegalArgumentException.class,()->{
                EclipticCoordinates.of(-3,1.00002); });
            assertThrows(IllegalArgumentException.class,()->{
                EclipticCoordinates.of(0,-2.0003); }); }
    @Test
    void isLatGetterValid(){
        EclipticCoordinates hc4= EclipticCoordinates.of(0,1.0004);
        assertEquals(1.0004,hc4.lat());
    }

    @Test
    void isLonGetterValid(){
        EclipticCoordinates hc4= EclipticCoordinates.of(0,1.0004);
        assertEquals(0,hc4.lon()); }
     @Test
    void isLonDegGetterValid(){
         EclipticCoordinates hc4= EclipticCoordinates.of(Math.PI/2,1.0004);
         assertEquals(90,hc4.lonDeg()); }
    @Test
    void isLatDegGetterValid(){
         EclipticCoordinates hc4= EclipticCoordinates.of(0,Math.PI/2);
         assertEquals(90,hc4.latDeg()); }

    @Test
    void isValidLonDegWithValidValues(){
        assertTrue(EclipticCoordinates.isValidLonDeg(40));
        assertTrue(EclipticCoordinates.isValidLonDeg(179));
    }
    @Test
    void isValidLatDegWithValidValues(){
        assertTrue(EclipticCoordinates.isValidLatDeg(90));
        assertTrue(EclipticCoordinates.isValidLatDeg(40)); }

     @Test
    void toStringWorksonEclipticCoordinates(){
            assertEquals("(λ=0.0000°, β=90.0000°)",EclipticCoordinates.of(0,Math.PI/2).toString());
            assertEquals("(λ=180.0000°, β=60.0000°)",EclipticCoordinates.of(Math.PI,Math.PI/3).toString());
     }















}
