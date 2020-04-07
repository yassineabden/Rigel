package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées géographiques
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class GeographicCoordinates  extends SphericalCoordinates{

    private static final RightOpenInterval INTERVAL_LONG_RAD = RightOpenInterval.symmetric(Angle.TAU);
    private static final ClosedInterval INTERVAL_LAT_RAD =  ClosedInterval.symmetric(Angle.TAU/2);



    private GeographicCoordinates(double lon, double lat){
        super(lon,lat); }

    /**
     * Méthode de construction de coordonées géographiques
     *
     * @param lonDeg longitude en degré
     * @param latDeg latitude en degré
     *
     * @throws IllegalArgumentException si lonDeg n'est pas inclus entre [-180 deg, 180 deg[
     * @throws IllegalArgumentException si latDeg n'est pas inclus entre [-90 deg, 90 deg]
     * @return les coordonées géographique construites
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){

        Preconditions.checkInInterval(INTERVAL_LONG_RAD,Angle.ofDeg(lonDeg));
        Preconditions.checkInInterval(INTERVAL_LAT_RAD,Angle.ofDeg(latDeg));

        return new GeographicCoordinates(Angle.ofDeg(lonDeg),Angle.ofDeg(latDeg)); }

    /**
     * Vérifie que la longitude est contenue entre [-180 deg, 180 deg[
     *
     * @param lonDeg longitude en degré à vérifier
     *
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidLonDeg(double lonDeg){
        return INTERVAL_LONG_RAD.contains(Angle.ofDeg(lonDeg));
    }

    /**
     * Vérifie que la latitude est contenue entre [-90 deg, 90 deg]
     *
     * @param latDeg latitude en degré à vérifier
     *
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidLatDeg(double latDeg){
        return INTERVAL_LAT_RAD.contains(Angle.ofDeg(latDeg));
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
     *  Retourne la latitude en radian
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
     * Transforme en string les coordonées géographique
     *
     * @return une string de type (lon=x.xxxx°, lat=x.xxxx°)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg()); }

}
