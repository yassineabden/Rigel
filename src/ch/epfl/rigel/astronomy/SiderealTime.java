package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe de temps sidéral
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class SiderealTime {

    final private static Polynomial S_0 = Polynomial.of(0.000025863, 2400.051336,6.697374558);
    final private static Polynomial S_1 = Polynomial.of(1.002737909);

    final private static RightOpenInterval NORMAL_LAT_RAD = RightOpenInterval.of(0,Angle.TAU);

    private SiderealTime(){}

    /**
     * Calcul le temps sidéral en radians à Greewich d'un moment donné
     *
     * @param when Moment donné
     *
     * @return temps sidéral à Greenwich en radians
     */
    public static double greenwich(ZonedDateTime when){


       ZonedDateTime atGreenwich =  when.withZoneSameInstant(ZoneId.of("UTC"));
       ZonedDateTime atGreenwichMidnight = atGreenwich.truncatedTo(ChronoUnit.DAYS);

       double T = Epoch.J2000.julianCenturiesUntil(atGreenwichMidnight);
       double t = when.getHour();


        double sGHr = S_1.at(T) + S_1.at(t);

        return NORMAL_LAT_RAD.reduce(Angle.ofHr(sGHr));
    }

    /**
     * Calcul le temps sidéral d'un endroit donnée à un moment donné
     *
     * @param when Moment donné
     * @param where Endroit donné
     *
     * @return temps sidéral en radians de l'endroit donné à un moment donné
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){

        return greenwich(when) + where.lon();
    }

}
