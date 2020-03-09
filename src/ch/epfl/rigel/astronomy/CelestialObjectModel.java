package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Interface modélisant un object céleste
 *
 * @param <O> object céleste
 *
 * @author Yassine Abdennadher (299273)
 *  @author Juliette Aerni (296670)
 */
public interface CelestialObjectModel<O> {


    /**
     * Retourne l'objet modélisé par le modèle pour le nombre (éventuellement négatif) de jours après l'époque J2010 donné, en utilisant la conversion donnée pour obtenir ses coordonnées équatoriales à partir de ses coordonnées écliptiques
     * @param daysSinceJ2010 nombre de jour après J2010 double
     * @param eclipticToEquatorialConversion coordonées équatoriales à J2010
     * @return objects modélisé après le nombre de jours données depuis J2010
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
