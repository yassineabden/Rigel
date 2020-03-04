package ch.epfl.rigel.astronomy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;


public class MyEpochTest {

    @Test
    void isDaysUntilJ2000ValidWithValidValues(){
        ZonedDateTime a =  ZonedDateTime.of(2000,1,1,12,0,0,0, ZoneId.of("UTC"));
        ZonedDateTime b =  ZonedDateTime.of(1999,1,1,12,0,0,0, ZoneId.of("UTC"));

        assertEquals(0, Epoch.J2000.daysUntil(a));
        assertEquals(-365,Epoch.J2000.daysUntil(b));


    }
    @Test
    void isDaysUntilJ2010ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(2010,1,1,0,0,0,0, ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(2020,3,20,23,34,0,0, ZoneId.of("UTC"));

    assertEquals(1, Epoch.J2010.daysUntil(a));
    assertEquals(3732.98, Epoch.J2010.daysUntil(b),1e-2);

}

    @Test
    void isJulianCenturiesJ2000ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(LocalDate.of(2001,1,27), LocalTime.MIDNIGHT,ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(LocalDate.of(2000,1,1).minusDays(7305), LocalTime.NOON,ZoneId.of("UTC"));

    assertEquals(-0.08925393566, Epoch.J2000.julianCenturiesUntil(a));
    assertEquals(-0.2,Epoch.J2000.julianCenturiesUntil(b), 1e-10);
}

    @Test
    void isJulianCenturiesJ2010ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(LocalDate.of(2010,1,1), LocalTime.MIDNIGHT,ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(LocalDate.of(2010,1,1).minusDays(7305), LocalTime.MIDNIGHT,ZoneId.of("UTC"));

   assertEquals(2.7378507871, Epoch.J2010.julianCenturiesUntil(a),1e-2);
   assertEquals(-0.2,Epoch.J2010.julianCenturiesUntil(b), 1e-4);
}


    @Test
        void isTestTelegramworks() {


    ZonedDateTime a = ZonedDateTime.of(
            LocalDate.of(2003, Month.JULY, 30),
            LocalTime.of(15, 0),
            ZoneOffset.UTC);
    ZonedDateTime b = ZonedDateTime.of(
            LocalDate.of(2020, Month.MARCH, 20),
            LocalTime.of(0, 0),
            ZoneOffset.UTC);
    ZonedDateTime c = ZonedDateTime.of(
            LocalDate.of(2006, Month.JUNE, 16),
            LocalTime.of(18, 13),
            ZoneOffset.UTC);
    ZonedDateTime d = ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 3),
            LocalTime.of(18, 0),
            ZoneOffset.UTC);
    ZonedDateTime e = ZonedDateTime.of(
            LocalDate.of(1999, Month.DECEMBER, 6),
            LocalTime.of(23, 3),
            ZoneOffset.UTC);

    assertEquals(1306.125, Epoch.J2000.daysUntil(a));
    assertEquals(7383.5, Epoch.J2000.daysUntil(b));
    assertEquals(2358.259028, Epoch.J2000.daysUntil(c), 1e-6);
    assertEquals(2.25, Epoch.J2000.daysUntil(d));
    assertEquals(-25.539583, Epoch.J2000.daysUntil(e), 1e-6);

    assertEquals(-2345.375, Epoch.J2010.daysUntil(a), 1e-6);
    assertEquals(3732, Epoch.J2010.daysUntil(b));
    assertEquals(-1293.240972, Epoch.J2010.daysUntil(c), 1e-6);
    assertEquals(-3649.25, Epoch.J2010.daysUntil(d), 1e-6);
    assertEquals(-3677.039583, Epoch.J2010.daysUntil(e), 1e-6);

}
}

