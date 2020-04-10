package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Un intervalle fermé
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class ClosedInterval extends Interval {

    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    /**
     * Méthode de construction d'un intervalle fermé avec une borne inférieur et une borne supérieur
     *
     * @param low borne inférieure
     * @param high borne supérieure
     *
     * @throws IllegalArgumentException si la borne inférieur est plus grande ou égale à la borne supérieur
     * @return l'intervalle fermé construit
     */
    public static ClosedInterval of(double low, double high) {

        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low,high); }

    /**
     * Méthode de construction d'un intervalle fermé symmetrique autour de zéro d'une taille donée
     *
     * @param size la taille de l'intervalle
     *
     * @throws IllegalArgumentException si la taille est inférieure ou égale à zéro
     * @return l'intervalle fermé construit
     */
    public static ClosedInterval symmetric (double size) {

        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size / 2, size / 2); }

    /**
     * Vérifie qu'une valeur est contenue dans l'intervalle
     *
     * @param v valeur à vérifier
     *
     * @return vraie si la valeur est dans l'intervalle, faux sinon
     */
    @Override
    public boolean contains(double v) { return v >= low() && v <= high(); }



    /**
     * Ecrêtage d'une valeur à l'intervalle, empêche la valeur d'être en dehors de l'interval
     *
     * @param v valeur à ecrêter
     *
     * @return la valeure après ecrêtage
     */
    public double clip (double v) {

        if (v > high()) v = high();
        if (v < low()) v = low();
        return v; }

    /**
     * Transforme en string l'intervalle
     *
     * @return une string de type [borne inférieure, borne supérieur]
     */
    @Override
    public String toString() {

        return String.format(Locale.ROOT, "[%.2f , %.2f]", low(), high()); }

}

















