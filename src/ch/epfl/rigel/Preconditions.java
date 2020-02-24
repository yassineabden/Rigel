package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Preconditions
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Preconditions {

    /**
     * Constructeur privé et sans attribut
     *Object ininstansiable
     */
    private Preconditions() {}

    /**
     * Vérifie qu'une condition nécessaire soit valide
     * @param isTrue condition booléene qu'on veut veérifer
     * @throws IllegalArgumentException si elle la condition est fausse
     *
     */
    public static void checkArgument(boolean isTrue){

    if (isTrue != true ){
        throw new IllegalArgumentException() ; }
    }

    /**
     * Vérifie qu'une valeur est contenue dans un interval donné
     *
     * @param interval interval dans lequel on veut chercher la valeur
     * @param value valeur à tester
     * @return la valeur si elle appartient à l'intervalle
     * @throws IllegalArgumentException si la valeur n'appartient pas à l'interval donné
     */
    public static double checkInInterval (Interval interval, double value){
        if (interval.contains(value)){
            return value;
        } else {
            throw new IllegalArgumentException();
        }

    }


}
