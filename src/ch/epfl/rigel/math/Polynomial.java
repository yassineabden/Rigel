package ch.epfl.rigel.math;

public final class Polynomial {

    private double [] coeff;

    /**
     * Créer un tableau de taille N contenant les coefficients du polynôme à l'index de leur puissance (:= par odre croissant)
     *
     * @param coefficientN coefficient de la puissance N
     *
     * @param coefficients tableau des coeffficient N-1 à 0 par odre décroissant
     */
    private Polynomial(double coefficientN , double... coefficients){

        coeff = new double [coefficients.length +1];
        int i=0;
        while (i<= coefficients.length) {
            for (double c : coefficients) {
                coeff[i] = c;
                i++;
            }
        }
        coeff[coefficients.length + 1] = coefficientN;



    };

    public static Polynomial of(double coefficientN , double... coefficients){

     if (coefficientN==0) {
        // vérifier la condition
         throw new IllegalArgumentException();
     }
    // question de style?...
     else return new Polynomial(coefficientN,coefficients);
    }

    /**
     * TODO implémenter selon pdf
     * @return
     */
    @Override
    public String toString() {

        return super.toString();
    }
}
