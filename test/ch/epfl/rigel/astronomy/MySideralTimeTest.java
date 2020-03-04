package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class MySideralTimeTest {

    @Test
    void greenwichTest() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, (int) 6.7e8), ZoneOffset.UTC)), 1e-6);
    }
    @Test
    void greenwichWorksWithKnownValue1(){
        assertEquals(4.894961213259,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0),
                        ZoneId.of("UTC"))), 1e-8);
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, (int) 6.7e8),
                ZoneOffset.UTC)),
                1e-10);
    }

    @Test
    void greenwichWorksWithKnownValue(){
        assertEquals(4.894899243459,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0),
                        ZoneId.of("UTC"))), 1e-4);

        assertEquals(5.274208582903,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1995, Month.JANUARY, 9),
                        LocalTime.of(12, 54, 23),
                        ZoneId.of("UTC"))), 1e-4);

        assertEquals(2.209444098114,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2018, Month.SEPTEMBER, 9),
                        LocalTime.of(9, 12, 52),
                        ZoneId.of("UTC"))), 1e-6);

        assertEquals(4.309511476916,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2018, Month.SEPTEMBER, 9),
                        LocalTime.of(9, 12, 52),
                        ZoneId.of("Asia/Shanghai"))), 1e-6);
    }












}











