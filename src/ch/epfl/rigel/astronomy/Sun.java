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
    private static final float MAGNITUDE_SUN= (float)-26.7;

    /**
     * Unique constructeur visible dans le package.
     *
     * @param eclipticPos position écliptique
     * @param equatorialPos Position de l'objet en cordonées equatoriales
     * @param angularSize   taille angulaire
     * @param meanAnomaly   anomalie moyenne
     * @throws IllegalArgumentException si la taille angulaire est négative
     * @throws NullPointerException     si le nom ou la position equatoriale et écliptiques sont nuls
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize,MAGNITUDE_SUN);

        this.meanAnomaly = meanAnomaly;
        this.eclipticPos = eclipticPos;
        Objects.requireNonNull(eclipticPos);
    }

    /**
     * Retourner la position écliptique
     * @return la position écliptique
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * Retourne l'anomalie moyenne
     * @return l'anomalie moyenne
     */
    public double meanAnomaly(){
        return meanAnomaly;
    }













}
