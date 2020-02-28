package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Classe de temps sidéral
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class SiderealTime {

    protected SiderealTime(){}

    public static double greenwich(ZonedDateTime when){
        RightOpenInterval normalLatRad = RightOpenInterval.of(0,Angle.TAU);

       ZonedDateTime atGreenwhich =  when.withZoneSameInstant(ZoneId.of("UTC"));

       //TODO mais la méthode retourne un nbr double (à virgule) du nbr de Julian century et pas des heures non?
       double T = Epoch.J2000.julianCenturiesUntil(atGreenwhich);


       double t = when.getHour();
        Polynomial s0 = Polynomial.of(0.000025863, 2400.051336,6.697374558);
        Polynomial s1 = Polynomial.of(1.002737909);
        double sGHr = s0.at(T) + s1.at(t);

        normalLatRad.reduce(Angle.ofHr(sGHr));

        //TODO dernière lignes normalement


        return 0;
    }

}
