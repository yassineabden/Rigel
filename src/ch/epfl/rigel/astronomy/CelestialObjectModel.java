package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Interface modélisant un object céleste
 *
 * @param <O> object céleste
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public interface CelestialObjectModel<O> {

    /**
     * Retourne l'objet modélisé par le modèle pour un nombre de jours et une une conversion de coordonées ecliptique en coordonées équatoriales données
     *
     * @param daysSinceJ2010 nombre de jour après J2010
     * @param eclipticToEquatorialConversion conversion de coordonées ecliptique en cpordonées équatoriale donée
     *
     * @return objects modélisé après le nombre de jours données et la conversion de coordonées ecliptique en cpordonées équatoriales
     */

    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
