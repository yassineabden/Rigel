package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

import static java.lang.Math.floor;

/**
 * Un intervalle semi-ouvert à droite
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double low, double high) {
        super(low, high);
    }

    /**
     * Méthode de contruction d'un intervalle semi-ouvert à droite avec une borne inférieure et une borne supérieure
     *
     * @param low  borne inférieur
     * @param high borne supérieur
     *
     * @throws IllegalArgumentException si la borne inférieur est plus grande ou égale à la borne supérieur
     * @return intervalle semi-ouvert construit
     */
    public static RightOpenInterval of(double low, double high) {

        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high); }

    /**
     * Méthode de construction d'un intervalle semi-ouvert, symmetrique autour de zéro de taille donnée
     *
     * @param size taille de l'intervalle
     *
     * @throws IllegalArgumentException si la borne inférieur est plus grande ou égale à la borne supérieur
     * @return l'intervalle semi ouvert construit
     */
    public static RightOpenInterval symmetric(double size) {

        Preconditions.checkArgument(size > 0);
        return new RightOpenInterval(-size / 2, size / 2); }

    /**
     * Vérifie qu'une valeur donnée est contenue dans l'intervalle
     *
     * @param v valeur à vérifier
     *
     * @return vrai si la valeur est dans l'intervalle, faux sinon
     */
    @Override
    public boolean contains(double v) { return v >= low() && v < high(); }

    /**
     * Réduit une valeur à l'intervalle, assigne la valeur à une valeur correspondante dans l'intervalle si elle est en dehors
     *
     * @param v valeur à réduire
     *
     * @return la valeur réduite
     */
    public double reduce(double v) {

        return low() + (v - low()) - (size() * floor((v - low()) / size())); }

    /**
     * Transforme l'intervalle en string
     *
     * @return une string de type [borne inférieur, borne supérieur[
     */
    @Override
    public String toString() {

        return String.format(Locale.ROOT, "[%.2f , %.2f[", low(), high()); }


}
