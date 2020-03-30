package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.InputStream;
import java.util.*;

/**
 * Classe rasssemblant un catalogue d'étoiles
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class StarCatalogue {

    private final HashMap<Asterism, List<Integer>> asterismsMap = new HashMap<>();
    private final List<Star> stars = new ArrayList<>();

    /**
     * Constructeur d'un catalogue d'étoiles. Pour chaque asterism donné est lié une liste
     * d'index correspondant à la position des étoiles dans une liste.
     *
     * @param stars     Liste d'étoiles contenues dans les asterisms
     * @param asterisms Liste d'asterisms
     * @throws IllegalArgumentException si un asterisms contient une étoile qui ne se trouve pas dans la liste donnée
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        this.stars.addAll(List.copyOf(stars));

        HashMap<Asterism, List<Integer>> aMap = new HashMap<>();

        HashMap<Star, Integer> starsIndex = new HashMap<>();
        for (Star s : stars) {
            starsIndex.put(s, this.stars.indexOf(s));
        }

        for (Asterism a : asterisms) {

            List<Integer> aIndex = new ArrayList<>(a.stars().size());

            for (Star s : a.stars()) {
                Preconditions.checkArgument(!(Objects.isNull(aIndex.add(starsIndex.get(s)))));
            }
            aMap.put(a, aIndex);
        }

        asterismsMap.putAll(Map.copyOf(aMap));

    }

    /**
     * Retourne la liste d'étoiles contenues dans les asterisms
     *
     * @return la liste d'étoiles contenues dans les asterims
     */
    public List<Star> stars() {
        return stars;

    }

    /**
     * Retourne la liste d'asterisms
     *
     * @return la liste d'asterisms
     */
    public Set<Asterism> asterisms() {
        return asterismsMap.keySet();
    }

    /**
     * Retourne la liste d'index des étoiles contenues dans l'asterism donné
     *
     * @param asterism asterism donné
     * @return la liste d'index des étoiles faisant partie de l'asterism
     * @throws IllegalArgumentException si l'asterism ne fait pas parti du catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(asterismsMap.containsKey(asterism));
        return asterismsMap.get(asterism);
    }


    public final static class Builder {
        private List<Star> stars = new ArrayList<>();
        private List<Asterism> asterisms= new ArrayList<>();

        public Builder() {}

        public Builder addStar(Star star) {
           stars.add(star);
           return this; }

        public List<Star> stars(){
            return Collections.unmodifiableList(stars); }

        public Builder addAsterism (Asterism asterism){
            asterisms.add(asterism);
            return this; }

        public List<Asterism> asterisms (){
            return Collections.unmodifiableList(asterisms); }

            // A faire!!

        public Builder loadFrom(InputStream inputStream){
            return null;
        }

        public StarCatalogue build(){
            return new StarCatalogue(stars,asterisms);
        }









    }
}