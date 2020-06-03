package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Un astérisme
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class AsterismWithName {

    private  final List< List<Star>> stars;
    private final String name;

    /**
     * Constructeur d'astérisme
     *
     * @param stars liste d'étoiles appartenant à l'asterisms
     *
     * @throws IllegalArgumentException si la liste d'étoiles est vide
     */
    public AsterismWithName(List<List<Star>> stars, String name){

        Preconditions.checkArgument(!stars.isEmpty());
        Preconditions.checkArgument(name != null);
        this.stars = List.copyOf(stars);
        //TODO immuabilité
        this.name = name;
    }

    /**
     * Retourne la liste d'étoiles contenues dans l'astersims
     *
     *  @return la liste d'étoiles formant l'astérisme
     */
    public List<List<Star>> stars(){
        return stars;
    }

    /**
     * TODO
     * @return
     */
    public String name() { return name;}


    //TODO
    public final static class Builder {

        private List < List<Star>> stars = new ArrayList<>();
        private  String name = new String();


        public Builder(){};

        public Builder setName ( String name){
            this.name = name;
            return this;
        }

        public Builder addStars (List<Star> stars){
            this.stars.add(List.copyOf(stars));
            return this;
        }

        public AsterismWithName build(){
            return new AsterismWithName(stars, name);
        }
    }

}
