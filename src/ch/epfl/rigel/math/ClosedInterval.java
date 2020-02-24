package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * ClosedInterval, sous classe d'interval
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class ClosedInterval extends Interval {

    /**
     * constructeur privé pour assurer l'immuabilité de la classe
     * fait appel au super pour la cosntruction
     * @param low borne inférieure
     * @param high borne supérieure
     */
    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    /** TODO il faut mettre un @throw si la methode appelle une methode qui peut lancer un truc?
     * permet de créer un interval fermé en appleant le constructeur
     * vérifie que l'interval est contructible
     * @param low borne inférieure
     * @param high borne supérieure
     * @return intervalle fermé construit
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low<high);
            ClosedInterval ci1 = new ClosedInterval(low,high);

            return ci1;
        }

    /**TODO il faut mettre un @throw si la methode appelle une methode qui peut lancer un truc?
     * permet la création d'un interval symmetrique autour de zéro de taille donnée
     * @param size taille de l'intervalle
     * @return interval fermé créé
     */
    public static ClosedInterval symmetric (double size) {

        Preconditions.checkArgument(size>0);
            ClosedInterval ci2 = new ClosedInterval(-size / 2, size / 2);
            return ci2;

        }

    /**
     *vérifie qu'une valeur est contenue dans l'interval
     * @param v Valeur à vérifier
     * @return vraie si elle est dans l'interval, faux sinon
     */
    @Override
    public boolean contains(double v) {
        if (v >= low() && v <= high()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * ecrêtage d'une valeur à l'interval, empêche la valeur d'être en dehors de l'interval
     * @param v valeur à ecrêter
     * @return la valeure apràs ecrêtage
     */
    public double clip (double v) {
        if (v>=high()) {
            v=high(); }
        if (v<=low()) {
            v=low(); }
      return v;
    }

    /**
     * transforme l'interavl en format ecrit, lisible
     * @return intervale lisible de type [borne inférieure, borne supérieur]
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%.2f , %.2f]",low(),high());


    }














        }

















