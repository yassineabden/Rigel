package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Des coordonnées sphériques
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

abstract class SphericalCoordinates {
    /**
     * Interval d'angles en radians acceptés pour la longitude
     *
     * [0°,360°[, en radians: [0, 2*pi[
     */
    public final static RightOpenInterval LONGITUDE_RAD_INTERVAL_TAU = RightOpenInterval.of(0,Angle.TAU);
    /**
     * Deuxième interval d'angles en radians valides pour la longitudes, interval symmetric
     *
     * [-180°,180°[, en radians: [-pi,pi[
     */
    public final static RightOpenInterval LONGITUDE_RAD_INTERVAL_SYMMETRIC = RightOpenInterval.symmetric(Angle.TAU);
    /**
     * Interval d'angles en radians valides pour la latitude
     *
     * [-90°,90°], en radians: [-pi/2, pi/2]
     */
    public final static ClosedInterval LATITUDE_RAD_INTERVAL = ClosedInterval.symmetric(Angle.TAU/2);

    private final double longitude,latitude;

    /**
     * Constructeur de la classe
     *
     * @param lon Longitude
     * @param lat Latitude
     */
    SphericalCoordinates(double lon, double lat){
        latitude =lat;
        longitude =lon;
    }

    /**
     * Retourne la longitude
     *
     * @return la longitude
     */
    double lon() { return longitude;}

    /**
     * Retourne la longitude en degrés
     *
     * @return la longitude en degrés
     */
    double lonDeg() { return Angle.toDeg(longitude);}

    /**
     * Retourne la latitude
     *
     * @return la latitude
     */
    double lat() { return latitude;}

    /**
     * Retourne la latitude en degrés
     *
     *  @return la latitude en degrés
     */
    double latDeg () { return Angle.toDeg(latitude);}

    /**
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     * @param obj Objet arbitraire
     *
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */
    @Override
    public final int hashCode (){
        throw new UnsupportedOperationException();
    }

}
