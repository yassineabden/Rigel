package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe de temps sidéral
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class SideralTime {

    final private static Polynomial S_0 = Polynomial.of(0.000025862, 2400.051336,6.697374558);
    final private static Polynomial S_1 = Polynomial.of(1.002737909,0);

    final static double MILLIS_TO_HOURS = 1.0/ Duration.ofHours(1).toMillis();

    private SideralTime(){}

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

         System.out.println(T);

      double t = atGreenwichMidnight.until(atGreenwich,ChronoUnit.MILLIS)*MILLIS_TO_HOURS;

        //double t = (Epoch.J2000.daysUntil(atGreenwich) - Epoch.J2000.daysUntil(atGreenwichMidnight)*24);
            System.out.println(t);

            System.out.println(S_0.at(T));
            System.out.println(S_1.at(t));

        double sGHr = S_0.at(T) + S_1.at(t);
            System.out.println(sGHr);

        System.out.println(Angle.normalizePositive(Angle.ofHr(sGHr)));
        return Angle.normalizePositive(Angle.ofHr(sGHr));
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
