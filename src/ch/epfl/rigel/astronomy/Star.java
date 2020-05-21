package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Une étoile
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Star extends CelestialObject {

    private static final ClosedInterval COLOR_INTERVAL = ClosedInterval.of(-0.5, 5.5);
    private final int hipparcosId, colorTemperature;


    /**
     * Construcetur d'une étoile
     *
     * @param name          nom de l'objet céleste
     * @param equatorialPos Position de l'objet en coordonées equatoriales
     * @param magnitude     magnitude
     * @param colorIndex
     * @throws IllegalArgumentException si le numéro d'hipparcos est négatif ou si l'indice de couleur n'est pas compris dans l'intervalle [-0.5, 5.5]
     * @throws NullPointerException     si le nom ou la position equatoriale sont nuls
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0f, magnitude);

        Preconditions.checkInInterval(COLOR_INTERVAL, colorIndex);
        Preconditions.checkArgument(hipparcosId >= 0);

        this.hipparcosId = hipparcosId;
        colorTemperature = colorIndexToTemperature(colorIndex);
    }

    /**
     * Retourne le numéro d'Hipparcos de l'étoile
     *
     * @return numéro d'Hipparcos, un int
     */

    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Calcul la couleur de la température de l'étoile en Kelvin
     *
     * @return la couleur de la température en Kelvin à l'entier inférieur le plus proche
     */
    public int colorTemperature() {
        return colorTemperature;
    }

    private int colorIndexToTemperature (float colorIndex){
        double c = 0.92 * colorIndex;
        double T = 4600 * (1 / (c + 1.7) + 1 / (c + 0.62));

        return (int) (Math.floor(T));
    }
}
