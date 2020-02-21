package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Classe Polynôme
 *
 * @author Juliette Aerni (296670)
 */
public final class Polynomial {

    private double[] coeff;

    /**
     * Créer un tableau de taille N contenant les coefficients du polynôme à l'index de leur puissance (:= par odre croissant)
     *
     * @param coefficientN coefficient de la puissance N
     * @param coefficients tableau des coeffficient N-1 à 0 par odre décroissant
     */
    private Polynomial(double coefficientN, double... coefficients) {

        coeff = new double[coefficients.length + 1];
        coeff[0] = coefficientN;
        System.arraycopy(coefficients, 0, coeff, 1, coefficients.length);

    }


    /**
     * @param coefficientN
     * @param coefficients
     * @return
     */


    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgumemt(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * @param x
     * @return
     */
    public double at(double x) {

        double polatx = 0;
        for (int i = coeff.length; i >= 0; i--) {
            polatx = polatx * x + coeff[i];

        }
        return polatx;

    }

    /**
     * TODO implémenter selon pdf
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coeff.length; i++) {


            sb.append(coeff[i] + "x^" + (coeff.length -1));

            if (coeff[i] == 0) {
                continue;
            }else if (coeff[i]  >0) {
                sb.append(" + " + coeff[i] + "x^" + (coeff.length - i));
            } else {
                    sb.append(" - " + coeff[i] +"x^"+ (coeff.length-i));
            }



        }
        return sb.toString();
    }
    @Override
    public final boolean equals(Object obj) {
        super.equals(obj);
        throw new UnsupportedOperationException(); }

    @Override
    public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }

}




