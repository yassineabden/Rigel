package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Des coordonnées sphériques
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

abstract class SphericalCoordinates {
    private final double Longitude;
    private final double Latitude;

    /**
     *
     * @param lon Longitude
     * @param lat Latitude
     */
    SphericalCoordinates(double lon, double lat){
        this.Latitude=lat;
        this.Longitude=lon;
    }

    /**
     * Retourne la longitude
     * @return la longitude
     */
    double lon() { return Longitude;}

    /**
     * Retourne la longitude en degrés
     * @return la longitude en degrés
     */
    double lonDeg() { return Angle.toDeg(Longitude);}

    /**
     * Retourne la latitude
     * @return la latitude
     */
    double lat() { return Latitude;}

    /**
     * Retourne la latitude en degrés
     * @return la latitude en degrés
     */
    double latDeg () { return Angle.toDeg(Latitude);}

    /**
     *
     * @param obj Objet arbitraire
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException(); }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */
    @Override
    public final int hashCode (){
        throw new UnsupportedOperationException(); }










}
