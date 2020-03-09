package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MyEquatorialToHorizontalConversionTest {

    @Test
    void isConversionValid(){
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,3,2,17,10,57,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(187.9679,21.6044).toString(),equToHor.apply(EquatorialCoordinates.of(Angle.ofHr(2.54),Angle.ofDeg(-23))).toString());

            EquatorialToHorizontalConversion equToHor1= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,8,14,11,00,00,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(65.82367,67.88464).toString(),equToHor1.apply(EquatorialCoordinates.of(Angle.ofHr(8.83764),Angle.ofDeg(49.90546))).toString()); }

    @Test
    void applyWorks() {
        ZonedDateTime when = ZonedDateTime.of(
                LocalDate.of(2009, Month.JULY, 6),
                LocalTime.of(0,0,0),
                ZoneOffset.UTC
        );
        GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0, 52);
        EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(when, geoCoord);
        EquatorialCoordinates equCoord = EquatorialCoordinates.of(Angle.ofHr(8), Angle.ofDMS(23,13,10));
        HorizontalCoordinates horCoord = conversion.apply(equCoord);
        assertEquals(Angle.ofDeg(283.271027), horCoord.az(),1e-7);
        assertEquals(Angle.ofDeg(19.334345), horCoord.alt(),1e-7);
    }

    @Test
    void horairAngleWorksOnKnownValues(){
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(1980,4,22,14,36,51,(int)67e7, ZoneId.of("America/Puerto_Rico")), GeographicCoordinates.ofDeg(-64,45));
        assertEquals(HorizontalCoordinates.ofDeg(0,0).toString(),equToHor.apply(EquatorialCoordinates.of(Angle.ofHr(18.539167),Angle.ofDeg(-23))).toString());
    }

    @Test
    void EcliptictoHorizontal(){
        EclipticCoordinates ecl = EclipticCoordinates.of(Angle.ofDeg(8),Angle.ofDeg(-70));
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("Europe/Paris")));
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2009,7,6,0,0,0,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        Function<EclipticCoordinates,HorizontalCoordinates> ecltoHor= e.andThen(equToHor);
        assertEquals(HorizontalCoordinates.ofDeg(0.0,0.0).toString(),ecltoHor.apply(ecl).toString());

    }

    @Test
    void rr(){
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,3,2,17,10,57,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(111.6819,-5.13597).toString(),equToHor.apply(EquatorialCoordinates.of(Angle.ofHr(6.01451),Angle.ofDeg(-20.00021))).toString());

    }





}
