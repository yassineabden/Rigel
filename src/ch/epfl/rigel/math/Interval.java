package ch.epfl.rigel.math;

/**
 * Un intervalle
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public abstract class Interval {
    private final double UpperBound;  // double?
    private final double LowerBound;


    /**
     *
     * @param LowerBound Borne supérieure de l'intervalle
     * @param UpperBound Borne inférieure de l'intervalle
     */

    protected Interval (double LowerBound , double UpperBound){
        this.UpperBound= LowerBound;
        this.LowerBound= UpperBound;
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
    public double high () {
        return UpperBound; }

    /**
     * Retourne la taille de l'intervalle
     * @return la taille de l'intervalle
     */
    public double size() {
        return UpperBound-LowerBound;
    }


    public abstract boolean contains (double v);

    @Override
    public final boolean equals(Object obj) {
        super.equals(obj);
        throw new UnsupportedOperationException(); }

    @Override
    public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }

    }















