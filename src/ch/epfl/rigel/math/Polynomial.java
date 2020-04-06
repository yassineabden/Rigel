package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Un polynôme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Polynomial {


    private final double[] coefficients;

    //TODO estc-ce que l'immuabilité est conservée avec la méthode arraycopy?
    // est-ce qu'on doit spécifier que la méthode arraycopy peut lancer des exceptions?
    private Polynomial(double coefficientN, double... coefficients) {

        this.coefficients = new double[coefficients.length + 1];
        this.coefficients[0] = coefficientN;
        System.arraycopy(coefficients, 0, this.coefficients, 1, coefficients.length);
    }


    /**
     * Méthode de construction d'un polynôme de degré N
     *
     * @param coefficientN coefficient de degré N
     * @param coefficients coefficients des autres degrés par ordre decroissant
     * @throws IllegalArgumentException si le coefficient de degré N est nul
     * @return le polynôme construit
     */
    public static Polynomial of(double coefficientN, double... coefficients) {

        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * Evalue le polynôme à une valeur donnée x
     *
     * @param x valeur à laquelle on évalue le polynôme
     * @return le polynôme évalué à x (valeur ccalculée)
     */
    public double at(double x) {

        double polAtx = 0;
        // Polynôme évalué en x au moyen de la form de Horner
        for (double coefficient : coefficients) {
            polAtx = polAtx * x + coefficient; }
        return polAtx;
    }

    /**
     * Transforme le polynôme en string
     *
     * @return le polynôme lisible, le plus réduit possible
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (coefficients.length == 1){
            sb.append(coefficients[0]);

        }else if (coefficients.length == 2){
            if (coefficients[1] == 1.0){
                sb.append("x");
            }else if (coefficients[1] == -1.0) {
                sb.append("-x");
            }else {
                sb.append(coefficients[0] + "x");
            }

        }else {
            if  (coefficients[0] == 1.0 ){
                sb.append("x^" + (coefficients.length - 1));
            }else if (coefficients[0]== -1.0) {
                sb.append("-x^" + (coefficients.length - 1));
            }else{
                sb.append(coefficients[0] + "x^" + (coefficients.length - 1));
            }
        }


        for (int i = 1; i < coefficients.length; i++) {

            if (coefficients[i] ==0) {
                continue;

            }else if (i == coefficients.length-1) {
                if (coefficients[i] > 0) {
                    sb.append("+" + coefficients[i]);
                } else {
                    sb.append(coefficients[i]);
                }

            } else if ( i == coefficients.length-2){

                if (coefficients[i] > 0) {
                    if (coefficients[2] == 1.0){
                        sb.append("+x");
                    }else if (coefficients[2]==-1.0) {
                        sb.append("-x");
                    }else{
                        sb.append("+" + coefficients[i] + "x");
                    }

                } else {
                    if (coefficients[2] == 1.0) {
                        sb.append("x");
                    }else if (coefficients[2] == -1.0){
                        sb.append("-x");
                    }else{
                        sb.append(coefficients[i] + "x");

                    }
                }

            }else {
                if (coefficients[i] > 0) {
                    if (coefficients[i] == 1.0){
                        sb.append("+x^" + (coefficients.length - 1- i));
                    }else if (coefficients[i] == -1.0) {
                        sb.append("-x^" + (coefficients.length - 1- i));
                    }else{
                        sb.append("+" + coefficients[i] + "x^" + (coefficients.length - 1 -i));
                    }

                } else {
                    if (coefficients[i] == 1.0) {
                        sb.append("x^" + (coefficients.length -1 - i));
                    }else if (coefficients[i] == -1.0){
                        sb.append("-x^" + (coefficients.length - 1 - i));
                    }else{
                        sb.append(coefficients[i] + "x^" + (coefficients.length - 1 - i));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec un polynôme immuable
     *
     * @param obj object à évaluer
     * @throws UnsupportedOperationException car cette méthode de peut pas être appelée pour un polynôme
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec un polynôme immuable
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour un polynôme
     */
    @Override
    public final int hashCode (){
        throw new UnsupportedOperationException();
    }

}




