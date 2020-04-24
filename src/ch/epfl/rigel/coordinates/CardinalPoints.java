package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.gui.SkyCanvasPainter;

import java.util.Arrays;
import java.util.List;

/**
 *  Enumération des points cardinaux principaux
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum CardinalPoints {

    NORTH_TEXT (HorizontalCoordinates.of(0,-0.5), "N"),
    NORTH_EAST_TEXT (HorizontalCoordinates.of(45,-0.5), "NE"),
    EAST_TEXT (HorizontalCoordinates.of(90,-0.5), "E"),
    SOUTH_EAST_TEXT (HorizontalCoordinates.of(135,-0.5), "SE"),
    SOUTH_TEXT (HorizontalCoordinates.of(180,-0.5), "S"),
    SOUTH_WEST_TEXT (HorizontalCoordinates.of(225,-0.5), "SW"),
    WEST_TEXT (HorizontalCoordinates.of(270,-0.5), "W"),
    NORTH_WEST_TEXT (HorizontalCoordinates.of(315,-0.5), "NW");

    private final HorizontalCoordinates coordinates;
    private final String name;

    /**
     * Liste contenant tous les points cardinaux
     */
    public final static List<CardinalPoints> ALL = Arrays.asList(CardinalPoints.values());

    CardinalPoints(HorizontalCoordinates coordinates, String name) {
        this.coordinates = coordinates;
        this.name = name;
    }

    /**
     * Retourne les initiale du nom du point cardinal
     *
     * @param cardinalPoint point cardinal dont on veut obtenir le nom
     *
     * @return les initiales du point cardinal (ie. "N", "S", "E", "W", ...)
     */
    public String name (CardinalPoints cardinalPoint){
        //TODO imuuabilité
        return this.name;
    }

    /**
     * Retourne les coordonées horizontales du point cardinal afin de les afficher
     *  un demi degré sous la ligne de l'horizon à l'ecran
     *
     * @param cardinalPoint point cardinal dont on cherche les coordonées horizontales
     *
     * @return les coordonées horozontales du point cardinal, un demi degré sous la ligne de l'horizon
     */
    public HorizontalCoordinates coordinates (CardinalPoints cardinalPoint){
        //TODO imuuabilité
        return this.coordinates;
    }

}

