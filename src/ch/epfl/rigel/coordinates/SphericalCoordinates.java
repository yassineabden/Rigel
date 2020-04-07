package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Des coordonnées sphériques
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

abstract class SphericalCoordinates {

    private final double longitude,latitude;;

    /**
     * Constructeur de la classe
     * @param lon Longitude
     * @param lat Latitude
     */
    SphericalCoordinates(double lon, double lat){
        latitude =lat;
        longitude =lon; }

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
        throw new UnsupportedOperationException(); }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */
    @Override
    public final int hashCode (){
        throw new UnsupportedOperationException(); }

}
