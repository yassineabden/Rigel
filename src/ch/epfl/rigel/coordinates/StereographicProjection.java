package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;
/**
 * Classe modélisant la projection stéréographique
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final HorizontalCoordinates center;
    // Cosinus et sinus de la latitude de center
    private final double  cosLat, sinLat;

    /**
     * Constructeur public qui retourne la projection stéréographique centrée en center
     *
     * @param center coordonnées horizontales
     */
    public  StereographicProjection(HorizontalCoordinates center){
        this.center = center;
        cosLat = Math.cos(center.lat());
        sinLat = Math.sin(center.lat());
    }

    /**
     * Détermine les coordonnées cartésiennes de la projection du point de coordonnées horizontales donnés en arguments
     *
     * @param azAlt un point exprimé en coordonnées horizontales
     *
     * @return les coordonnées cartésiennes de la projection du point de coordonnées horizontales donnés en arguments
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double azAltLat = azAlt.lat();
        double cLat = Math.cos(azAltLat);
        double sLat = Math.sin(azAltLat);

        double lonDistance = azAlt.lon() - center.lon();
        double cLon = Math.cos(lonDistance);
        double sLon = Math.sin(lonDistance);

        double d = 1.0 / (1+sLat*sinLat + cosLat*cLat*cLon);
        return  CartesianCoordinates.of(d*cLat*sLon, d*(sLat*cosLat - cLat*sinLat*cLon));
    }

    /**
     * Détermine les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes
     *
     * @param xy un point exprimé en coordonnées cartésiennes
     *
     * @return les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){

        double x = xy.x();
        double y = xy.y();

        if (y == 0 && x ==0) return HorizontalCoordinates.of(center.lon(),center.lat());

        else {

            double p = Math.hypot(x, y);
            double pSquared = p*p;
            double sin_c = (2 * p) / (pSquared + 1);
            double cos_c = (1 - pSquared) / (pSquared + 1);

            double az = Angle.normalizePositive(Math.atan2(x *sin_c,
                                                 (p*cosLat * cos_c - y * sinLat * sin_c)) + center.lon());
            double alt = Math.asin(cos_c * sinLat + (y * sin_c * cosLat) / p);

            return HorizontalCoordinates.of(az, alt);
        }
    }

    /**
     * Détermine le centre du cercle sur lequel on projette un parallèle passant par un point (l'ordonnée du centre du cerle peut être infinie)
     *
     * @param hor point appartenant au parallèle
     *
     * @return centre du cerle de projection du parallèle
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){

        return CartesianCoordinates.of(0.0,(cosLat)/(sinLat+ Math.sin(hor.lat())));
    }

    /**
     * Détermine le rayon du cercle de projection d'un parallèle déterminé par un point
     *
     * @param parallel coordonées horizontales d'un point du parralèle
     *
     * @return Rayon de projection du parallèle qui peut être infini
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){

        double lat = parallel.lat();
        return (Math.cos(lat) / (Math.sin(lat) + sinLat));
    }

    /**
     * Calcul du diamètre projeté d'une sphère de taille angulaire donnée  centrée au centre de projection, en admettant que celui-ci soit sur l'horizon
     *
     * @param rad taille angulaire de la sphère en radians
     *
     * @return diamètre projeté
     */
    public double applyToAngle(double rad){ return 2*Math.tan(rad/4); }

    /**
     * Retourne une chaîne de caractères
     *
     * @return une chaîne de caractères de type : Centre de StereographicProjection (x.xxxx, x.xxxx)
     */

    @Override
    public String toString() {
        return  String.format(Locale.ROOT, "Centre de StereographicProjection (%.4f, %.4f) ", center.az(),center.alt());
    }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException lorsqu'on fait appel à celle-ci
     *
     * @param obj Objet arbitraire
     *
     * @throws UnsupportedOperationException si cette méthode est appelée
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException lorsqu'on fait appel à celle-ci
     *
     * @throws UnsupportedOperationException si cette méthode est appelée
     */

    @Override
    public int hashCode() {
        throw new  UnsupportedOperationException();
    }
}
