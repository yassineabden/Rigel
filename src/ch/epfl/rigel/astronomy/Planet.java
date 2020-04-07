package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
/**
 * Une planète
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
    public final class Planet extends CelestialObject {

    /**
     * Constructeur de la classe.
     *
     * @param name          nom de l'objet céleste
     * @param equatorialPos Position de l'objet en coordonnées equatoriales
     * @param angularSize   taille angulaire
     * @param magnitude     magnitude
     *
     * @throws IllegalArgumentException si la taille angulaire est négative
     * @throws NullPointerException     si le nom ou la position equatoriale sont nuls
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude); }

}
