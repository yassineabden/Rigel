package ch.epfl.rigel.math;

/**
 * Un intervalle
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public abstract class Interval {

    private final double upperBound;
    private final double lowerBound;


    /**
     * Construit un intervalle avec une borne inférieur et une borne supérieur
     *
     * @param lowerBound Borne supérieure de l'intervalle
     * @param upperBound Borne inférieure de l'intervalle
     */

    protected Interval (double lowerBound , double upperBound){

        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    /**
     * Retourne la borne inférieure de l'intervalle
     *
     * @return la borne inférieure de l'intervalle
     */
    public double low (){
        return lowerBound;
    }

    /**
     *  Retourne la borne supérieure de l'intervalle
     *
     * @return la borne supérieure de l'intervalle
     */
    public double high () { return upperBound; }

    /**
     * Retourne la taille de l'intervalle
     *
     * @return la taille de l'intervalle
     */
    public double size() {
        return upperBound - lowerBound;
    }

    /**
     * vérifie si une valeur est contenue dans l'interval ou non
     *
     * @param v Valeur à vérifier
     * @return vrai si la valeur est contenue dans l'intervalle, faux sinon
     */
    public abstract boolean contains (double v);

    /**
     * Lève une exception car la méthode equals ne peut pas être appelée par un intervalle
     *
     * @param obj objet (interval généralement) à évaluer
     * @throws UnsupportedOperationException si une sous-classe la redéfinit.
     */
    @Override
    public final boolean equals(Object obj) {

        throw new UnsupportedOperationException(); }

    /**
     * Lève une exception car la méthode hashCode ne peut pas être applée par un intervalle
     *
     * @throws UnsupportedOperationException si une super-classe la redéfinit
     */
    @Override
    public final int hashCode (){

        throw new UnsupportedOperationException(); }

    }















