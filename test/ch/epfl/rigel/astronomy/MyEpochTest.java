package ch.epfl.rigel.astronomy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class MyEpochTest {

    @Test
    void isDaysUntilJ2000ValidWithValidValues(){
        ZonedDateTime a =  ZonedDateTime.of(2000,1,2,18,0,0,0, ZoneId.of("UTC"));
        ZonedDateTime b =  ZonedDateTime.of(1999,1,1,12,0,0,0, ZoneId.of("UTC"));

        assertEquals(1.25, Epoch.J2000.daysUntil(a));
        assertEquals(-365,Epoch.J2000.daysUntil(b));


    }
@Test
    void isDaysUntilJ2010ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(2010,1,2,0,0,0,0, ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(2020,3,20,23,34,0,0, ZoneId.of("UTC"));

    assertEquals(2, Epoch.J2010.daysUntil(a));
    assertEquals(3732.98, Epoch.J2010.daysUntil(b),1e-6);

}

@Test
    void isJulianCenturiesJ2000ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(LocalDate.of(2000,1,1).plusDays(36525), LocalTime.NOON,ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(LocalDate.of(2000,1,1).minusDays(7305), LocalTime.NOON,ZoneId.of("UTC"));

    assertEquals(1, Epoch.J2000.julianCenturiesUntil(a));
    assertEquals(-0.2,Epoch.J2000.julianCenturiesUntil(b), 1e-10);
}

@Test
    void isJulianCenturiesJ2010ValidWithValidValues(){
    ZonedDateTime a =  ZonedDateTime.of(LocalDate.of(2010,1,1).plusDays(36525), LocalTime.MIDNIGHT,ZoneId.of("UTC"));
    ZonedDateTime b =  ZonedDateTime.of(LocalDate.of(2010,1,1).minusDays(7305), LocalTime.MIDNIGHT,ZoneId.of("UTC"));

   assertEquals(1, Epoch.J2010.julianCenturiesUntil(a),1e-4);
    assertEquals(-0.2,Epoch.J2010.julianCenturiesUntil(b), 1e-4);
}

}

