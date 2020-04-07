package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Un polynôme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Polynomial {

    private final double[] coefficients;


    private Polynomial(double coefficientN, double... coefficients) {

        this.coefficients = new double[coefficients.length + 1];
        this.coefficients[0] = coefficientN;
        System.arraycopy(coefficients, 0, this.coefficients, 1, coefficients.length); }


    /**
     * Méthode de construction d'un polynôme de degré N
     *
     * @param coefficientN coefficient de degré N
     * @param coefficients coefficients des autres degrés par ordre decroissant
     *
     * @throws IllegalArgumentException si le coefficient de degré N est nul
     * @return le polynôme construit
     */
    public static Polynomial of(double coefficientN, double... coefficients) {

        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients); }

    /**
     * Evalue le polynôme à une valeur donnée x
     *
     * @param x valeur à laquelle on évalue le polynôme
     *
     * @return le polynôme évalué à x (valeur ccalculée)
     */
    public double at(double x) {

        double polAtx = 0;
        // Polynôme évalué en x au moyen de la forme de Horner
        for (double coefficient : coefficients) {
            polAtx = polAtx * x + coefficient; }
        return polAtx; }

    /**
     * Transforme le polynôme en string
     *
     * @return le polynôme lisible, le plus réduit possible
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        int power = coefficients.length -1;

        for (double c : coefficients) {
            if (c != 0) {

                if (power != coefficients.length-1 && c > 0) sb.append("+");

                if (c != 1.0 && c != -1.0) sb.append(c);
                else if (c < 0) sb.append("-");

                if (power != 0){
                    sb.append("x");
                    if(power != 1){
                        sb.append("^").append(power); } }
                }
            power--;
        }

        return sb.toString(); }


    /**
     * Lance une exception car on ne peut pas utiliser cette méthode avec un polynôme immuable
     *
     * @param obj object à évaluer
     *
     * @throws UnsupportedOperationException car cette méthode de peut pas être appelée pour un polynôme
     */
    @Override
    public final boolean equals(Object obj) { throw new UnsupportedOperationException(); }

    /**
     * Lance une exception car on ne peut pas utiliser cette méthode avec un polynôme immuable
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour un polynôme
     */
    @Override
    public final int hashCode (){
        throw new UnsupportedOperationException();
    }

}




