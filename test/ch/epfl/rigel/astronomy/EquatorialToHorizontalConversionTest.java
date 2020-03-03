package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EquatorialToHorizontalConversionTest {

    @Test
    void isConversionValid(){
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,3,2,17,10,57,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(187.9679,21.6044).toString(),equToHor.apply(EquatorialCoordinates.of(Angle.ofHr(2.54),Angle.ofDeg(-23))).toString());

        EquatorialToHorizontalConversion equToHor1= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,8,14,11,00,00,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(65.82367,67.88464).toString(),equToHor1.apply(EquatorialCoordinates.of(Angle.ofHr(8.83764),Angle.ofDeg(49.90546))).toString());

    }

}
