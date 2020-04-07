package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;


import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Classe de temps sidéral
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class SiderealTime {

    private final static Polynomial S_0 = Polynomial.of(0.000025862, 2400.051336,6.697374558);
    private final static Polynomial S_1 = Polynomial.of(1.002737909,0);
    private final static double MILLIS_TO_HOURS = 1.0/ Duration.ofHours(1).toMillis();

    private SiderealTime(){}

    /**
     * Calcul le temps sidéral de Greewich en radians d'un couple date/heure donné
     *
     * @param when instant donné
     *
     * @return temps sidéral, normalisé, de Greenwich en radians d'un couple date/heure donné
     */
    public static double greenwich(ZonedDateTime when){

        ZonedDateTime atGreenwich =  when.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime atGreenwichMidnight = atGreenwich.truncatedTo(ChronoUnit.DAYS);

        double T = Epoch.J2000.julianCenturiesUntil(atGreenwichMidnight);
        double t = atGreenwichMidnight.until(atGreenwich,ChronoUnit.MILLIS)*MILLIS_TO_HOURS;

        double sGHr = S_0.at(T) + S_1.at(t);
        return Angle.normalizePositive(Angle.ofHr(sGHr)); }

    /**
     * Calcul le temps sidéral d'un endroit et d'un couple date/heure donnés
     *
     * @param when instant donné
     * @param where Endroit donné
     *
     * @return temps sidéral en radians d'un endroit et d'un couple date/heure donnés (normalisé)
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){

        return Angle.normalizePositive(greenwich(when) + where.lon()); }

}





