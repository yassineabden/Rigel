package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;
/**
 * L'astérisme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Asterism  {

    private  final List<Star> stars;

    /**
     * Constructeur qui initialise la liste d'étoiles
     * @param stars liste d'étoiles
     * @throws IllegalArgumentException si la liste d'étoiles est vide.
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars=stars; }

    /**
     * Méthode d'accés permettant d'obtenir la liste d'étoiles
     * Retourne la liste des étoiles formant l'astérisme.
     *  @return la liste des étoiles formant l'astérisme.
     */
    public List<Star> stars(){
        return stars;
    }































}
