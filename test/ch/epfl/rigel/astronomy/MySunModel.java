package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;


import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
public class MySunModel{

    @Test
    void IsAtValid(){

        assertEquals(8.3926828082978,SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr());

    }







}
