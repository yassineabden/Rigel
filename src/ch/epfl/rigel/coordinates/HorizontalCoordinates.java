package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Coordonnées horizontales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    private static final RightOpenInterval INTERVAL_LONG_RAD = RightOpenInterval.of(0,Angle.TAU);
    private static final ClosedInterval INTERVAL_LAT_RAD = ClosedInterval.symmetric(Math.PI);

    private static final RightOpenInterval INTERVAL_LONG_DEG = RightOpenInterval.of(0, 360);
    private static final ClosedInterval INTERVAL_LAT_DEG = ClosedInterval.symmetric(180);


    private HorizontalCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Construit des coordonnées horizontales à partir d'angle en radians
     *
     * @param az l'azimuth qui correspond à la longitude pour les coordonnées horizontales en radians
     * @param alt la hauteur qui correspond à la hauteur pour les coordonnées horizontales en radians
     * @return les coordonnées horizontales dont l'azimuth vaut az et la hauteur alt.
     */
    public static HorizontalCoordinates of(double az, double alt) {

        Preconditions.checkInInterval(INTERVAL_LONG_RAD, az);
        Preconditions.checkInInterval(INTERVAL_LAT_RAD, alt);

        return new HorizontalCoordinates(az, alt); }

    /**
     * Construit des coordonnées horizontales à partir d'angle en degrés
     *
     * @param azDeg l'azimuth qui correspond à la longitude pour les coordonnées horizontales en degrés
     * @param altDeg la hauteur qui correspond à la hauteur pour les coordonnées horizontales en degrés
     * Retourne les coordonnées horizontales dont l'azimuth vaut azDeg et la hauteur altDeg
     * @return les coordonnées horizontales dont l'azimuth vaut azDeg et la hauteur altDeg
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {

        Preconditions.checkInInterval(INTERVAL_LONG_DEG, azDeg);
        Preconditions.checkInInterval(INTERVAL_LAT_DEG, altDeg);

        return new HorizontalCoordinates(Angle.ofDeg(azDeg),Angle.ofDeg(altDeg)); }

    /**
     * Retourne l'azimut
     *
     * @return l'azimut
     */

    public double az() {
        return super.lon();
    }

    /**
     * Retourne l'azimut en degrés
     *
     *  @return l'azimut en degrés
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Retourne une chaine correspondant à l'octant dans lequel se trouve l'azimuth du récepteur.
     *
     * @param n Chaine de caractères correspondant au point cardinal Nord
     * @param e Chaine de caractères correspondant au point cardinal Est
     * @param s Chaine de caractères correspondant au point cardinal Sud
     * @param w Chaine de caractères correspondant au point cardinal Ouest
     * @return une chaine correspondant à l'octant dans lequel se trouve l'azimuth du récepteur.
     */
    public String azOctantName(String n, String e, String s, String w) {

        double phi= azDeg();
        int Quadrant= (int) Math.round(phi/360*8);

        switch (Quadrant) {
            case (0):
            case (8):
                return n;
            case (1):
                return n+e;
            case (2):
                return e;
            case (3):
                return s+e;
            case (4):
                return s;
            case (5):
                return s+w;
            case (6):
                return w;
            case (7):
                return n+w;
            default:
                throw new IllegalArgumentException(); }
    }

    /**
     * Retourne la hauteur
     *
     * @return la hauteur
     */

    public double alt() {return super.lat();}


    /**
     * Retourne la hauteur en degrés
     *
     * @return la hauteur en degrés
     */
    public double altDeg (){return super.latDeg();}

    /**
     * Calcule la distance angulaire entre le récepteur et le point donné en argument
     *
     * @param that Point qui a une longitude et une latitude
     * @return la distance angulaire séparant deux points de coordonnées
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        return Math.acos(Math.sin(that.alt())*Math.sin(this.alt())+Math.cos(that.alt())*Math.cos(this.alt())*Math.cos(this.az()-that.az())); }

    /**
     * Transforme en string les coordonées horizontales
     *
     * @return une string de type (az=x.xxxx°, alt=x.xxxx°)
     */
    @Override
    public String toString () {
        return String.format(Locale.ROOT,"(az=%.4f°, alt=%.4f°)",azDeg(),altDeg()); }




}

