package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Classe de conversion de coordonées équatoriale en coordonées horizontales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double cosLat,sinLat, localSideralTime;



    /**
     * Constructeur de conversion de coordonées données
     *
     *  @param when L'instant à ctransformer en coordonées horizontales
     * @param where L'endroit à transformer en coordonées horizontales
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {

        cosLat = Math.cos(where.lat());
        sinLat = Math.sin(where.lat());
        localSideralTime = SiderealTime.local(when,where);
    }

    /**
     *
     * Effectue la conversion de coordonées équatoriales à des coordonées horizontales
     *
     * @param equ coordonées équatoriales à trsndformer
     *
     * @return les coordonées horizontales correspondantes
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {

        double angleHoraire = localSideralTime - equ.ra();

        double eclD = equ.dec();

        double h = Math.asin(Math.sin(eclD)*sinLat + Math.cos(eclD)*Math.cos(angleHoraire)*cosLat);
        double A = Angle.normalizePositive(Math.atan2(-Math.cos(eclD)*cosLat*Math.sin(angleHoraire), Math.sin(eclD)-sinLat*Math.sin(h)));

        return HorizontalCoordinates.of(A,h);
    }

    /**
     *TODO javdoc
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *TODO javadoc
     *
     * @param obj
     *@throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();

    }
}
