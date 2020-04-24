package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;
/**
 * Les Soleil
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Sun extends CelestialObject {

    private final float meanAnomaly;
    private final EclipticCoordinates eclipticPos;
    private static final float MAGNITUDE_SUN = (float) -26.7;

    /**
     * Unique constructeur visible dans le package.
     *
     * @param eclipticPos position exprimé en coordonnées écliptique
     * @param equatorialPos position exprimé en coordonnées equatoriales
     * @param angularSize   taille angulaire
     * @param meanAnomaly   anomalie moyenne
     *
     * @throws IllegalArgumentException si la taille angulaire est négative
     * @throws NullPointerException     si le nom ou la position equatoriale ou la position écliptique sont nuls
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize,MAGNITUDE_SUN);

        Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
        this.eclipticPos = eclipticPos;
    }

    /**
     * Retourne la position écliptique.
     *
     * @return la position écliptique
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * Retourne l'anomalie moyenne.
     *
     * @return l'anomalie moyenne
     */
    public double meanAnomaly(){
        return meanAnomaly;
    }
}
