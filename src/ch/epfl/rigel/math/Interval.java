package ch.epfl.rigel.math;

/**
 * Un intervalle
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public abstract class Interval {
    private final double UpperBound;
    private final double LowerBound;


    /**
     * constructeur d'intervalle, attribue la borne inferieure et supérieure
     * @param LowerBound Borne supérieure de l'intervalle
     * @param UpperBound Borne inférieure de l'intervalle
     */

    protected Interval (double LowerBound , double UpperBound){
        this.UpperBound= UpperBound;
        this.LowerBound= LowerBound;
        ;
    }

    /**
     * Retourne la borne inférieure de l'intervalle
     * @return la borne inférieure de l'intervalle
     */
    public double low (){
        return LowerBound;
    }

    /**
     *  Retourne la borne supérieure de l'intervalle
     * @return la borne supérieure de l'intervalle
     */
    public double high () { return UpperBound; }

    /**
     * Retourne la taille de l'intervalle
     * @return la taille de l'intervalle
     */
    public double size() {
        return UpperBound-LowerBound;
    }

    /**
     * vérifie si une valeur est contenue dans l'interval ou non (à implémenter par les sous-clases)
     * @param v Valeur à vérifier
     * @return vrai si la valeur est contenue dans l'intervalle
     */
    public abstract boolean contains (double v);

    /**
     * permet de comparer si un obljet donné correspond à l'interval
     * lance une exception pour assurer qu'aucunes sou-classe ne la redéfinira
     * fait appelle au super pour la comparaison
     * @param obj objet (interval généralement) à évaluer
     * @throws UnsupportedOperationException si une sous-classe la redéfinit.
     */
    @Override
    public final boolean equals(Object obj) {
        super.equals(obj);
        throw new UnsupportedOperationException(); }

    /**
     *  Retourne une array contenant le hashcode de l'interval
     *  Cette méthode lève l'exception UnsupportedOperationException pour garantir qu'aucune sous-classe ne les redéfinira
     *
     *  @throws UnsupportedOperationException si une super-classe la redéfinit
     * @return une array contenant le hashcode de l'interval
     */
    @Override
    public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }

    }















