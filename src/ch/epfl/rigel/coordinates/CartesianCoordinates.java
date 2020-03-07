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
public  final class CartesianCoordinates {

    private final double x,y;

    private CartesianCoordinates(double x, double y){
    this.x = x;
    this.y = y;
    }

    /**
     * Methode de construction de coordonées cartésiennes
     *
     * @param x abscisse
     * @param y ordonnée
     *
     * @return Les coordonées carthésiennes construites
     */
    public static CartesianCoordinates of(double x,double y){
    return new CartesianCoordinates(x, y);
    }

    /**
     * Retourne l'abscisse
     *
     * @return l'abscisse x
     */
    double x(){return x;}

    /**
     * Retourne l'ordonnée
     *
     * @return l'ordonnée y
     */
    double y(){return y;}

    //TODO il faut appeler le super ou pas?!
    @Override
    public int hashCode() {
        return super.hashCode();
       // throw new UnsupportedOperationException();
    }

    //TODO il faut appeler le super ou pas?!

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
        //throw new UnsupportedOperationException();

    }

    /**
     * Retourne les coordonées sous forme lisible
     *
     * @return les coordonées carthésiennes sous la fomre (x, y)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"(%.2f , %.2f)",x(),y());
    }
}
