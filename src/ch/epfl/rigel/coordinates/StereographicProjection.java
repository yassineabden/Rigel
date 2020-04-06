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
     * Constructeur public qui retourne la projection stéréographique centrée en center.
     *
     * @param center coordonnées horizontales
     */
    public  StereographicProjection(HorizontalCoordinates center){
        this.center = center;
        cosLat = Math.cos(center.lat());
        sinLat = Math.sin(center.lat()); }

    /**
     * Détermine les coordonnées cartésiennes de la projection du point de coordonnées horizontales donné en arguments.
     *
     * @param azAlt un point exprimé en coordonnées horizontales
     * @return les coordonnées cartésiennes de la projection du point de coordonnées horizontales donné en arguments
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double cLat =Math.cos(azAlt.lat());
        double sLat = Math.sin(azAlt.lat());

        double cLon = Math.cos(azAlt.lon()-center.lon());
        double sLon = Math.sin(azAlt.lon()-center.lon());

        double d = 1.0 / (1+sLat*sinLat + cosLat*cLat*cLon);
        return  CartesianCoordinates.of(d*cLat*sLon, d*(sLat*cosLat - cLat*sinLat*cLon)); }

    /**
     * Détermine les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes.
     *
     * @param xy un point exprimé en coordonnées cartésiennes
     * @return les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double p = Math.sqrt(xy.x()*xy.x() + xy.y()*xy.y());
        double sin_c = (2*p)/(p*p + 1);
        double cos_c = (1 - p*p)/ (p*p + 1);

        double az = Angle.normalizePositive(Math.atan2(xy.x()*sin_c , (p*cosLat*cos_c - xy.y()*sinLat*sin_c) ) + center.lon());
        double alt = Math.asin(cos_c*sinLat + (xy.y()*sin_c*cosLat)/ p);

        return HorizontalCoordinates.of(az,alt); }

    /**
     * Détermine le centre du cercle sur lequel on projette un parallèle passant par un point
     * L'ordonnée du centre du cerle peut être infinie
     *
     * @param hor point appartenant au parallèle
     * @return centre du cerle de projection du parallèle
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double sLat = Math.sin(hor.lat());

        return CartesianCoordinates.of(0.0,(cosLat)/(sinLat+ sLat)); }

    /**
     * Détermine le rayon du cercle de projection d'un parallèle déterminé par un point
     *
     * @param parallel coordonées horizontales d'un point du parralèle
     * @return Rayon de projection du parallèle qui peut être infini
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        double cLat =Math.cos(parallel.lat());
        double sLat = Math.sin(parallel.lat());

        return (cLat / (sLat + sinLat)); }

    /**
     * Calcul du diamètre projeté d'une sphère de taille angulaire donnée  centrée au centre de projection, en admettant que celui-ci soit sur l'horizon
     *
     * @param rad taille angulaire de la sphère en radians
     *
     * @return diamètre projeté
     */
    public double applyToAngle(double rad){
        return 2*Math.tan(rad/4); }

    /**
     * Retourne une chaîne de caractères
     *
     * @return une chaîne de caractères de type : Centre de StereographicProjection (x.xxxx, x.xxxx)
     */

    @Override
    public String toString() {
        return  String.format(Locale.ROOT, "Centre de StereographicProjection (%.4f, %.4f) ", center.az(),center.alt()); }
    /**
     *Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     * @param obj Objet arbitraire
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException(); }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     * @throws UnsupportedOperationException si une super-classe la redéfinit.
     */

    @Override
    public int hashCode() {
        throw new  UnsupportedOperationException(); }
}
