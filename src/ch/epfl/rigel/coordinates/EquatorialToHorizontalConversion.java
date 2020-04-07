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
     * Constructeur de conversion de coordonées équatoriale en coordonées horizontales à un instant et un lieu donnés
     *
     * @param when instant auquel la transfomartion est appliquée
     * @param where lieu de référence
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {

        cosLat = Math.cos(where.lat());
        sinLat = Math.sin(where.lat());
        localSideralTime = SiderealTime.local(when,where); }

    /**
     * Effectue la conversion de coordonées équatoriales à des coordonées horizontales
     *
     * @param equ coordonées équatoriales à transformer
     *
     * @return les coordonées horizontales transformées
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {

        double angleHoraire = localSideralTime - equ.ra();
        double sinDec = Math.sin(equ.dec());
        double cosDec = Math.cos(equ.dec());

        double alt = Math.asin(sinDec*sinLat +
                                cosDec*Math.cos(angleHoraire)*cosLat);
        double az = Angle.normalizePositive(Math.atan2(
                                - cosDec*cosLat*Math.sin(angleHoraire),
                                sinDec - sinLat*Math.sin(alt)));

        return HorizontalCoordinates.of(az,alt); }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec une conversion de coordonées
     *
     * @throws UnsupportedOperationException car car cette méthode de peut pas être appelée pour une conversion de coordonées
     */
    @Override
    public int hashCode() {throw new UnsupportedOperationException(); }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec une conversion de coordonées
     *
     * @throws UnsupportedOperationException car car cette méthode de peut pas être appelée pour une conversion de coordonées
     */
    @Override
    public boolean equals(Object obj) {throw new UnsupportedOperationException(); }
}
