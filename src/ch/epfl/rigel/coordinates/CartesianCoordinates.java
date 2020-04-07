package ch.epfl.rigel.coordinates;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Classe modélisant des coordonées carthésiennes
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class CartesianCoordinates {

    private final double x,y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;}

    /**
     * Methode de construction de coordonées cartésiennes
     *
     * @param x abscisse
     * @param y ordonnée
     *
     * @return Les coordonées carthésiennes construites
     */
    public static CartesianCoordinates of(double x,double y){

        return new CartesianCoordinates(x, y); }

    /**
     * Retourne l'abscisse
     *
     * @return l'abscisse x
     */
    public double x(){return x;}

    /**
     * Retourne l'ordonnée
     *
     * @return l'ordonnée y
     */
    public double y(){return y;}

    /**
     *
     * Retourne les coordonées sous une forme lisible
     *
     * @return les coordonées cartésiennes sous la fomre (x, y)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"(%.4f , %.4f)",x(),y()); }

    /**
     * Cette méthode lève l'exception UnsupportedOperationException lorsqu'on fait appel à celle-ci
     *
     * @throws UnsupportedOperationException si cette méthode est appelée
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException(); }

    /**
     *Cette méthode lève l'exception UnsupportedOperationException lorsqu'on fait appel à celle-ci
     *
     * @param obj Objet arbitraire
     *
     * @throws UnsupportedOperationException si cette méthode est appelée
     */

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException(); }


}
