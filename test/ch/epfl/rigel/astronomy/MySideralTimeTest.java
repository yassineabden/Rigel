package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class MySideralTimeTest {

    @Test
    void greenwichTest() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, (int) 6.7e8), ZoneOffset.UTC)), 1e-6); }
    @Test
    void greenwichWorksWithKnownValue(){
        assertEquals(4.894961213259,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0),
                        ZoneId.of("UTC"))), 1e-8);
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC)),
                1e-10); }


    @Test
    void localWorksWithValidValues (){
        ZonedDateTime d1 =ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC);
        GeographicCoordinates where= GeographicCoordinates.ofDeg(-64,0);
        assertEquals(Angle.ofHr(0.401453),SiderealTime.local(d1,where),1e-6);
    }












}











