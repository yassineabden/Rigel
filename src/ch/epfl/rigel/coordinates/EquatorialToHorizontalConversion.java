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

    private final double cosLat,sinLat,localSideralTime;

    /**
     * Constructeur de conversion de coordonnées équatoriales en coordonnées horizontales à un instant et un lieu donnés
     *
     * @param when instant auquel la transfomartion est appliquée
     * @param where lieu de référence
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {

        cosLat = Math.cos(where.lat());
        sinLat = Math.sin(where.lat());
        localSideralTime = SiderealTime.local(when,where);
    }

    /**
     * Effectue la conversion de coordonnées équatoriales en des coordonnées horizontales
     *
     * @param equ coordonnées équatoriales à transformer
     *
     * @return les coordonnées horizontales transformées
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {

        double angleHoraire = localSideralTime - equ.ra();
        double sinDec = Math.sin(equ.dec());
        double cosDec = Math.cos(equ.dec());

        double sinAlt = sinDec*sinLat
                                + cosDec*Math.cos(angleHoraire)*cosLat;
        double az = Angle.normalizePositive(Math.atan2(
                                - cosDec*cosLat*Math.sin(angleHoraire)
                                ,sinDec - sinLat*sinAlt));

        return HorizontalCoordinates.of(az, Math.asin(sinAlt));
    }

    /**
     * Lance une exception car on ne peut pas utiliser cette méthode avec une conversion de coordonnées
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour une conversion de coordonnées
     */
    @Override
    public int hashCode() {throw new UnsupportedOperationException(); }

    /**
     * Lance une exception car on ne peut pas utiliser cette méthode avec une conversion de coordonnées
     * @param obj objet arbitraire
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour une conversion de coordonnées
     */
    @Override
    public boolean equals(Object obj) {throw new UnsupportedOperationException(); }
}
