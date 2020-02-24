package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Classe Polynôme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Polynomial {

    private double[] coeff;

    /**
     * constructeur privé pour assurer l'immuabilité de la classe
     * Créer un tableau de taille N+1 contenant tous les coefficicents par ordre decroissant
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
     * mlthode de construction appelant le contrucetur
     * vérifie que le polynôme ne soit pas nul
     *
     * @param coefficientN coefficient du polynôme de degré n
     * @param coefficients tableau dynamique contenant les autres coefficients par ordre decroissant
     * @return le polynôme construit := tableau unique regroupant les coefficients
     */


    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * évalue le polynôme à une valeur donnée pour x
     * calculé grâce à la focntion de Horner
     *
     * @param x valeur à laquelle on évalue le polynôme
     * @return le polynôme évalué à x (valeur ccalculée)
     */
    public double at(double x) {

        double polatx = 0;
        for (int i = 0; i<coeff.length; i++) {
            polatx = polatx * x + coeff[i];

        }
        return polatx;

    }

    /**
     * override de toString, transforme le polynôme en string lisible, sous forme habituelle
     *
     * @return le polynôme lisible, le plus réduit possible
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

    /**
     * permet de regarder si un objet est equal au polynôme ou non
     * lance une exception pour garantir qu'aucune sous-classe ne redéfinira la méthode
     * @param obj object à évaluer
     * @return vrai si l'objet est égale  au polynôme, faux sinon
     */
    @Override
    public final boolean equals(Object obj) {
        super.equals(obj);
        throw new UnsupportedOperationException(); }

    /**
     * lève une exception pour garantir qu'aucune sous classe ne les redéfinria
     *
     * @return une array conetant le hashCode du polynôme
     */
    @Override
    public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }

}




