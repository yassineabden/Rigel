package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MyEclipticToEquatorialConversionTest {
    @Test
    void applyOK(){
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(143.7225092),Angle.ofDeg(19.53569924)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDeg(139.6861111), Angle.ofDeg(4.875277778))).toString());
        EclipticToEquatorialConversion f = new EclipticToEquatorialConversion(ZonedDateTime.of(2020,8,14,11,0,0, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(11.8583), Angle.ofDeg(58.6403)).toString(), f.apply(EclipticCoordinates.of(Angle.ofDMS(120, 30, 10), Angle.ofDMS(30,52,31))).toString());
    }

    @Test
    void applyOK1(){
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2020,8,14,11,0,0, 0, ZoneId.of("Europe/Paris")));
        EquatorialToHorizontalConversion f= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,8,14,11,0,0, 0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        EclipticCoordinates ec1 = EclipticCoordinates.of(Math.PI,Math.PI/6);
        assertEquals(HorizontalCoordinates.ofDeg(68.71179,17.29562).toString(),f.apply(e.apply(ec1)).toString());

       /* @Test
        void apply(){
            ZonedDateTime ZD1 = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 5), LocalTime.of(12, 0), ZoneOffset.UTC);
            EclipticToEquatorialConversion Conv1 = new EclipticToEquatorialConversion(ZD1);
            System.out.println(Angle.toDeg(Angle.ofDMS(23,26,17)));
            EclipticCoordinates Ecl1 = EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31));
            EquatorialCoordinates Eq1 = Conv1.apply(Ecl1);

            assertEquals(2.508431,Eq1.ra());
            assertEquals(0.3409623,Eq1.dec());

        }
*/


    }


















}




