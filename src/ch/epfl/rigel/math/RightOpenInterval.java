package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

import static java.lang.Math.floor;

/**
 * RightOpenInterval, sous classe d'interval
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class RightOpenInterval extends Interval{

    /**
     * constructeur privé pour assurer l'immuabilité de la classe
     * @param low bonre inférieure
     * @param high borne supérieur
     */
    private RightOpenInterval(double low, double high){
        super(low,high);
    }

    /**TODO il faut mettre un @throw si la methode appelle une methode qui peut lancer un truc?
     * méthode de contruction d'un interval quelconque qui appelle le constructeur
     * @param low borne inférieur
     * @param high borne supérieur
     * @return interval semi-ouvert construit
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low<high);
        RightOpenInterval roi1 = new RightOpenInterval(low,high);
            return roi1;
        }

    /**TODO il faut mettre un @throw si la methode appelle une methode qui peut lancer un truc?
     * méthode de contrustion d'un interval symmetrique autour de zéro de taille donnée, fait appel au construcetur privé
     * @param size taille de l'interval
     * @return l'interval semi ouvert construit
     */
    public static RightOpenInterval symmetric (double size) {
        Preconditions.checkArgument( size >0);
            RightOpenInterval roi2 = new RightOpenInterval(-size / 2, size / 2);
            return roi2;

        }

    /**
     * vérifie qu'une valeur donée est contenue dans l'interval
     * @param v Valeur à vérifier
     * @return vrai si elle est dans l'interval, faux sinon
     */
    @Override
    public boolean contains(double v) {
        if (v >= low() && v < high() ){
            return true;
        } else {
            return false;
        }
    }

    /**
     * réduit une valeur à l'interval, assigne la valeur à une valeur correspondante dans l'interval si elle est en dehors
     * @param v valeur à réduire
     * @return éa valeur poste réduction
     */
    public double reduce(double v){

        return low() + (v-low()) - (size()*floor((v-low())/size()));
    }

    /**
     * transforme l'interval en string lisible
     * @return l'interval de manière lisible sous la form [borne inférieur, borne supérieur[
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%.2f , %.2f[",low(),high());


    }




}
