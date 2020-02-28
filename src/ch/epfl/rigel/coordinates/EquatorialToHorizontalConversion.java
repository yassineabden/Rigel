package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.HashMap;
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
     *  @param when L'instant à ctransformer en coordonées horizontales
     * @param where L'endroit à transformer en coordonées horizontales
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {

        cosLat = Math.cos(where.lat());
        sinLat = Math.sin(where.lat());
        localSideralTime = SiderealTime.local(when,where);
    }

    /**
     * TODO javadoc et vérifier méthide
     * Applies this function to the given argument.
     *
     * @param equatorialCoordinates the function argument
     * @return the function result
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        double angleHoraire = localSideralTime - equatorialCoordinates.ra();

        double h = Math.asin(Math.sin(equatorialCoordinates.dec()*sinLat + Math.cos(equatorialCoordinates.dec())*Math.cos(angleHoraire)*cosLat));
        double A = Math.atan2(-Math.cos(equatorialCoordinates.dec())*cosLat*Math.sin(angleHoraire), Math.sin(equatorialCoordinates.dec())-sinLat*Math.sin(h));

        return HorizontalCoordinates.of(A,h);
    }

    /**
     * TODO javadoc + méthode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * TODO javadoc + méthode
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
