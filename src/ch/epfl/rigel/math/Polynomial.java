package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Classe Polynôme
 *
 * @author Juliette Aerni (296670)
 */
public final class Polynomial {

    private final double[] coefficient ;

    /**
     * Créer un tableau de taille N contenant les coefficients du polynôme à l'index de leur puissance (:= par odre croissant)
     *
     * @param coefficientN coefficient de la puissance N
     * @param coefficients tableau des coeffficient N-1 à 0 par odre décroissant
     */
    private Polynomial(double coefficientN, double... coefficients) {

        coefficient = new double[coefficients.length + 1];
        coefficient[0] = coefficientN;
        int i = 1;

        }
        // Problème ...!
        //System.arraycopy()



    }

    /**
     *
     * @param coefficientN
     * @param coefficients
     * @return
     */


    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgumemt(coefficientN == 0);

        return new Polynomial(coefficientN, coefficients);
    }

    /**
     *
     * @param x
     * @return
     */
    public double at (double x){

        double polatx = 0;
        for (int i = coefficient.length; i >=0 ; i--) {
            polatx = polatx*x +coefficient[i];

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

        StringBuilder coeff = new StringBuilder();

        // est-ce que si je fais <coeff.append(coeffi)> j'airai le coeff en string ou juste en double?
        //
        for (double coeffi : this.coefficient){
            if(coeffi == 0);
            //si on laisse comme ça ça fait rien non?
                else {
                    // but: ajouter le coefficient[i] + "x" + ^(coeff.length-i) + " + "
                    coeff.append(coeffi);
            }
    }
}
