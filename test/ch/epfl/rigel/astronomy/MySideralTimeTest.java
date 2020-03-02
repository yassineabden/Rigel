package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MySideralTimeTest {

    @Test
    void isGreenwichValidWithValidValues(){
        ZonedDateTime a= ZonedDateTime.of(2001,1,27,12,00,00,00, ZoneId.of("UTC"));
        assertEquals(20.51620215,SideralTime.greenwich(a));
    }

}
