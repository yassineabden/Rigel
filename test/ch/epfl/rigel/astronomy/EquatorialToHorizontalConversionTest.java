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
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,3,1,15,0,0,0, ZoneId.of("UTC")), GeographicCoordinates.ofDeg(10,6));
        assertEquals(HorizontalCoordinates.ofDeg(0.0335,44.907).toString(),equToHor.apply(EquatorialCoordinates.of(Math.PI,Math.PI/2)).toString());


    }

}
