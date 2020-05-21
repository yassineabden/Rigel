package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées équatoriales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class EquatorialCoordinates extends SphericalCoordinates{

    private EquatorialCoordinates(double lon, double lat){ super(lon,lat); }

    /**
     * Méthode de construction de coordonées équatoriales
     *
     * @param ra ascension droite  en radians
     * @param dec declinaison en radians
     *
     * @throws IllegalArgumentException si  l'ascension droite n'est pas inclus entre [0h, 24h[
     * @throws  IllegalArgumentException si la declinaison  n'est pas inclus entre [-90 deg, 90 deg]
     * @return des coordonnées équatoriales
     */
    public static EquatorialCoordinates of(double ra, double dec){

        Preconditions.checkInInterval(SphericalCoordinates.LONGITUDE_RAD_INTERVAL_TAU,ra);
        Preconditions.checkInInterval(SphericalCoordinates.LATITUDE_RAD_INTERVAL,dec);

        return new EquatorialCoordinates(ra,dec); }

    /**
     * Vérifie qu'une heure est une ascension droite valide, comprise entre [0h, 24h[
     *
     * @param raHr ascension droite en heure
     *
     * @return vrai si l'heure est valide, faux sinon
     */
    public static boolean isValidRaHr(double raHr){
        return SphericalCoordinates.LONGITUDE_RAD_INTERVAL_TAU.contains(Angle.ofHr(raHr));
    }

    //TODO on les garde quand même non?
    /**
     * Vérifie qu'un angle est une declinaison valide, contenue entre [-90 deg, 90 deg]
     *
     * @param dec latitude en degré à vérifier
     *
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidDec(double dec){
        return SphericalCoordinates.LATITUDE_RAD_INTERVAL.contains(Angle.ofDeg(dec));
    }

    /**
     * Retourne l'ascesion droite en radian
     *
     * @return l'ascension droite en radian, qui correspond à la longitude
     */
    public double ra(){
        return super.lon();
    }

    /**
     * Retourne l'ascesion droite en degrés
     *
     *  @return l'ascension droite en degrés, qui correspond à la longitude
     */
    public double raDeg(){ return super.lonDeg(); }

    /**
     * Retourne l'ascesion droite en heure
     *
     *  @return l'ascension droite en heure, qui correspond à la longitude
     */
    public double raHr(){ return Angle.toHr(super.lon());
    }

    /**
     * Retourne la déclinaison en radian
     *
     * @return declinaison en radian
     */
    public double dec(){ return super.lat(); }

    /**
     * Retourne la déclinaison en degré
     *
     * @return declinaison en degré
     */
    public double decDeg(){
        return super.latDeg();
    }

    /**
     * Transforme en string les coordonées équatoriales
     *
     * @return une string de type (ra=x.xxxx°, dec=x.xxxx°)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }


}
