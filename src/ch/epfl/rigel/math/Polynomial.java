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
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * @param x
     * @return
     */
    public double at(double x) {

        double polatx = 0;
        for (int i = 0; i<coeff.length; i++) {
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

        if (coeff.length==1){
        sb.append(coeff[0]);

        }else if (coeff.length == 2){
            if (coeff[1] == 1.0){
                sb.append("x");
            }else if (coeff[1] == -1.0) {
                sb.append("-x");
            }else {
                sb.append(coeff[0] + "x");
            }

        }else {
            if  (coeff[0] == 1.0 ){
                sb.append("x^" + (coeff.length - 1));
            }else if (coeff[0]== -1.0) {
                sb.append("-x^" + (coeff.length - 1));
            }else{
                sb.append(coeff[0] + "x^" + (coeff.length - 1));
            }
        }


        for (int i = 1; i < coeff.length; i++) {

            if (coeff[i] ==0) {
                continue;

            }else if (i == coeff.length-1) {
                if (coeff[i] > 0) {
                    sb.append("+" + coeff[i]);
                } else {
                    sb.append(coeff[i]);
                }

            } else if ( i == coeff.length-2){

                if (coeff[i] > 0) {
                    if (coeff[2] == 1.0){
                        sb.append("+x");
                    }else if (coeff[2]==-1.0) {
                        sb.append("-x");
                    }else{
                        sb.append("+" + coeff[i] + "x");
                    }

                } else {
                    if (coeff[2] == 1.0) {
                        sb.append("x");
                    }else if (coeff[2] == -1.0){
                        sb.append("-x");
                    }else{
                        sb.append(coeff[i] + "x");

                    }
                }

            }else {
                if (coeff[i] > 0) {
                    if (coeff[i] == 1.0){
                        sb.append("+x^" + (coeff.length - 1- i));
                    }else if (coeff[i] == -1.0) {
                        sb.append("-x^" + (coeff.length - 1- i));
                    }else{
                        sb.append("+" + coeff[i] + "x^" + (coeff.length - 1 -i));
                    }

                } else {
                    if (coeff[i] == 1.0) {
                        sb.append("x^" + (coeff.length -1 - i));
                    }else if (coeff[i] == -1.0){
                        sb.append("-x^" + (coeff.length - 1 - i));
                    }else{
                        sb.append(coeff[i] + "x^" + (coeff.length - 1 - i));

                    }
                }
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




