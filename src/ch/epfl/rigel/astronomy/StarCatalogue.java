package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Classe rasssemblant un catalogue d'étoiles
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class StarCatalogue {

    private final Map <Asterism, List<Integer>> asterismsMap;
    private final List<Star> stars ;
   // private final List<Star> stars = new ArrayList<>();

    /**
     * Constructeur d'un catalogue d'étoiles. Pour chaque asterism donné est lié une liste
     * d'index correspondant à la position des étoiles dans une liste.
     *
     * @param stars     Liste d'étoiles contenues dans les asterisms
     * @param asterisms Liste d'asterisms
     * @throws IllegalArgumentException si un asterisms contient une étoile qui ne se trouve pas dans la liste donnée
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        this.stars = List.copyOf(stars);

        //this.stars.addAll(List.copyOf(stars));

        HashMap <Asterism, List<Integer>> aMap = new HashMap<>();

        HashMap <Star, Integer> starsIndex = new HashMap<>();
        int i= 0;
        for (Star s : stars) {
            starsIndex.put(s, i);
          i++; }

        for (Asterism a : asterisms) {

            List<Integer> aIndex = new ArrayList<>(a.stars().size());

            for (Star s : a.stars()) {
                Preconditions.checkArgument(starsIndex.get(s) != null);
                aIndex.add(starsIndex.get(s)); }
            aMap.put(a, aIndex);
        }
        asterismsMap = Collections.unmodifiableMap(aMap);
    }

    /**
     * Retourne la liste d'étoiles contenues dans les asterisms
     *
     * @return la liste d'étoiles contenues dans les asterims
     */

    public List<Star> stars() { return stars; }

    /**
     * Retourne la liste d'asterisms
     *
     * @return la liste d'asterisms
     */

    public Set<Asterism> asterisms() { return asterismsMap.keySet(); }

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

    /**
     * Un bâtisseur de catalogue d'étoiles
     *
     * @author Yassine Abdennadher (299273)
     * @author Juliette Aerni (296670)
     */


    public final static class Builder {
        private final List<Star> stars = new ArrayList<>();
        private final List<Asterism> asterisms= new ArrayList<>();

        /**
         * Constructeur par défaut qui initialise le bâtisseur de manière à ce que le catalogue
          en construction soit initialment vide.
         */
        public Builder() {}


        /**
         * Ajoute l'étoile donnée au catalogue en cours de construction
         * @param star l'étoile
         * @return le bâtisseur
         */
        public Builder addStar(Star star) {
           stars.add(star);
           return this; }

        /**
         * Retourne une vue non modifiable sur les étoiles du catalogue en cours de construction.
         * @return une vue non modifiable sur les étoiles du catalogue en cours de construction.
         */
        public List<Star> stars(){
            return Collections.unmodifiableList(stars); }

        /**
         * Ajoute l'astérisme donné au catalogue en cours de construction.
         * @param asterism l'astérisme
         * @return le bâtisseur
         */
        public Builder addAsterism (Asterism asterism){
            asterisms.add(asterism);
            return this; }

        /**
         * Retourne une vue non modifiablesur les astérismes du catalogue en cours de construction.
         * @return une vue non modifiablesur les astérismes du catalogue en cours de construction.
         */
        public List<Asterism> asterisms (){
            return Collections.unmodifiableList(asterisms); }


        /**
         * Méthode qui charge un input d'asterism et/ou d'étoiles au builder grâce à un chargeur d'étoiles et/ou d'asterims
         *
         * @param inputStream input d'asterisms et/ou d'étoiles
         * @param loader chargeur d'asterisms et/ou d'étoiles
         * @return le builder chargé
         * @throws IOException s'il y a une erreur d'entrée/sortie
         */
        //TODO vérifier que l'exception est bien lancée quand il faut

        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            loader.load(inputStream,this);
            return this;}


        /**
         * Retourne le catalogue contenant les étoiles et astérismes ajoutés jusqu'alors au bâtisseur.
         * @return le catalogue contenant les étoiles et astérismes ajoutés jusqu'alors au bâtisseur.
         */
        public StarCatalogue build(){
            return new StarCatalogue(stars,asterisms);
        }

    }

    /**
     * Interface imbriquée qui charge des données
     *
     * @author Yassine Abdennadher (299273)
     * @author Juliette Aerni (296670)
     */
    public interface Loader{

        /**
         * Charge les étoiles et/ou asterism du flot d'entrée et les ajoute au catalogue en cours de construction du bâtisseur
           lève une IOException s'il y a une erreur
         *
         * @param inputStream Flot d'entrée contenant des asterism et/ou des étoiles
         * @param builder Catalogue en cours de construction
         *
         * @throws IOException S'il y a une erreur d'entrée /sortie
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException ;

    }


}