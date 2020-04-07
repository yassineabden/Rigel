package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;
/**
 * Un astérisme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Asterism  {

    private  final List<Star> stars;

    /**
     * Constructeur d'astérisme
     *
     * @param stars liste d'étoiles appartenant à l'asterisms
     *
     * @throws IllegalArgumentException si la liste d'étoiles est vide.
     */
    public Asterism(List<Star> stars){

        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars); }

    /**
     * Retourne la liste d'étoiles contenues dans l'astersims
     *
     *  @return la liste des étoiles formant l'astérisme.
     */
    public List<Star> stars(){
        return stars;
    }































}
