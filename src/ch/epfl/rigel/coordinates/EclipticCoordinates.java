package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées écliptiques
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */


public final class EclipticCoordinates extends SphericalCoordinates{

    private EclipticCoordinates(double lon, double lat){ super(lon,lat); }

    /**
     * Méthode de construction de coordonées ecliptique
     *
     * @param lon longitude en radians
     * @param lat latitude en radians
     *
     * @throws IllegalArgumentException si lonDeg n'est pas inclus entre [0°, 360°[
     * @throws  IllegalArgumentException si latDeg n'est pas inclus entre [-90 deg, 90 deg]
     * @return des coordonnées écliptiques
     */

    public static EclipticCoordinates of(double lon, double lat){

        Preconditions.checkInInterval(SphericalCoordinates.LONGITUDE_RAD_INTERVAL_TAU,lon);
        Preconditions.checkInInterval(SphericalCoordinates.LATITUDE_RAD_INTERVAL,lat);

         return new EclipticCoordinates(lon,lat);
    }

    /**
     * Vérifie qu'un angle est une logitude valide, contenue entre [0°, 360°[
     *
     * @param lonDeg longitude en degré à vérifier
     *
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidLonDeg(double lonDeg){
        return SphericalCoordinates.LONGITUDE_RAD_INTERVAL_TAU.contains(Angle.ofDeg(lonDeg));
    }

    /**
     * Vérifie qu'un angle est une latitude valide, contenue entre [-90 °, 90 °]
     *
     * @param latDeg latitude en degré à vérifier
     *
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidLatDeg(double latDeg){
        return SphericalCoordinates.LATITUDE_RAD_INTERVAL.contains(Angle.ofDeg(latDeg));
    }

    /**
     * Retourne la longitude en radian
     *
     * @return longitude en radian
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * Retourne la longitude en degré
     *
     * @return longitude en degré
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }


    /**
     * Retourne la latitude en radian
     *
     * @return latitude en radian
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * Retourne la latitude en degré
     *
     * @return latitude en degré
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * Transforme en string les coordonées ecliptiques
     *
     * @return une string de type (λ=x.xxxx°, β=x.xxxx°)
     */
    @Override
    public String toString(){
        return(String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg()));
    }


}
