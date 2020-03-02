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


}

